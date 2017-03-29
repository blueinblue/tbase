package org.bib.tbase.repository;

import static org.junit.Assert.*;
import static org.bib.tbase.domain.user.RolePredicates.*;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.pagination.PageRequestFactory;
import org.bib.tbase.BaseTest;
import org.bib.tbase.domain.user.QRole;
import org.bib.tbase.domain.user.Role;
import org.bib.tbase.domain.user.User;
import org.bib.tbase.domain.user.UserSearchCriteria;
import org.junit.Test;
import org.springframework.data.domain.Page;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup("classpath:sampleData.xml")
public class UserRepositoryTest extends BaseTest {
// Constructors

// Public Methods
	@Test
	public void testFindByEmailIgnoreCase() {
		User user = userRepo.findByEmailIgnoreCase("user@mail.com");
		assertNotNull(user);
	}
	
	@Test
	public void testFindByObjectId() {
		User user = userRepo.findByObjectId("USER-OBJ-101");
		
		assertNotNull(user);
		assertEquals("USER-OBJ-101", user.getObjectId());
	}
	
	@Test
	public void testSearch() {
		// Search by email - no page request
		Page<User> userPage = userRepo.search(new UserSearchAdapter("admin", null, null, (Role[]) null), null);
		
		assertNotNull(userPage);
		userPage.forEach(new Consumer<User>() {
			@Override
			public void accept(User t) {
				assertTrue(StringUtils.containsIgnoreCase(t.getEmail(), "admin"));
			}
		});
		
		// Search by email - first page
		userPage = userRepo.search(new UserSearchAdapter("admin", null, null, (Role[]) null), pageFactory.newPageRequest(0, 10, "id:ASC"));
		
		assertNotNull(userPage);
		assertEquals(0, userPage.getNumber());
		
		// Search by regular user role's object id - page request
		final Role userRole = roleRepo.findOne(roleNameEq("USER"));
		assertNotNull(userRole);
		
		userPage = userRepo.search(new UserSearchAdapter(null, null, null, userRole), pageFactory.newPageRequest(1, 2, "id:ASC"));
		
		assertNotNull(userPage);
		userPage.forEach(new Consumer<User>() {
			@Override
			public void accept(User u) {
				assertTrue(u.getRoles().contains(userRole));
			}
		});
		
		// Search by admin user role's object id - no page request
		final Role adminRole = roleRepo.findOne(roleNameEq("ADMIN"));
		assertNotNull(adminRole);
		
		userPage = userRepo.search(new UserSearchAdapter(null, null, null, adminRole), null);
		
		assertNotNull(userPage);
		userPage.forEach(new Consumer<User>() {
			@Override
			public void accept(User u) {
				assertTrue(u.getRoles().contains(adminRole));
			}
		});
		
		// Eitehr admin or user role
		userPage = userRepo.search(new UserSearchAdapter(null, null, null, adminRole, userRole), null);
		
		assertNotNull(userPage);
		userPage.forEach(new Consumer<User>() {
			@Override
			public void accept(User u) {
				assertTrue(u.getRoles().contains(adminRole) || u.getRoles().contains(userRole));
			}
		});
	}

// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserRepositoryTest.class);
	
	/**
	 * Repo
	 */
	@Inject
	private UserRepository userRepo;
	
	/**
	 * Page Request Factory
	 */
	@Inject
	private PageRequestFactory pageFactory;
	
	/**
	 * Role Repository
	 */
	@Inject
	private RoleRepository roleRepo;
	
	private static class UserSearchAdapter implements UserSearchCriteria {
		// Constructors
		public UserSearchAdapter(String email, String displayName, Boolean enabled, Role ...roles) {
			this.email = email;
			this.displayName = displayName;
			this.enabled = enabled;
//			this.roleObjectIds = Arrays.asList(roleObjectIds == null ? new String[0] : roleObjectIds);
			
			if(roles != null) {
				for(Role r : roles) {
					this.roles.add(r);
				}
			}
		}
		
		@Override
		public String getEmail() {
			return email;
		}
		
		@Override
		public String getDisplayName() {
			return displayName;
		}

		@Override
		public Boolean getAccountEnabled() {
			return enabled;
		}

		@Override
		public List<Role> getRoles() {
			return roles;
		}
//		public List<String> getRoleObjectIds() {
//			return roleObjectIds;
//		}
		
		// Attributes
		private String email;
		private String displayName;
		private Boolean enabled;
//		private List<String> roleObjectIds;
		private List<Role> roles = new LinkedList<Role>();
	}
}
