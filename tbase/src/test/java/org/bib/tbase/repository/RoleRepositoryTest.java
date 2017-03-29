package org.bib.tbase.repository;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.BaseTest;
import org.bib.tbase.domain.user.Role;
import org.junit.Test;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup("classpath:sampleData.xml")
public class RoleRepositoryTest extends BaseTest {
// Constructors

// Public Methods
	@Test
	public void testFindByObjectId() {
		Role adminRole = roleRepo.findByObjectId("ROLE-OBJ-100");
		assertNotNull(adminRole);
		assertEquals("ROLE-OBJ-100", adminRole.getObjectId());
	}

	@Test
	public void test() {
		Role adminRoleOne = new Role();
		adminRoleOne.setObjectId("ROLE-OBJ-100");
		
		Role adminRoleTwo = roleRepo.findByObjectId("ROLE-OBJ-100");
		assertNotNull(adminRoleTwo);
		
		assertTrue(adminRoleOne.equals(adminRoleTwo));
	}
// Getters & Setters

// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(RoleRepositoryTest.class);
	
	/**
	 * Role Repository
	 */
	@Inject
	private RoleRepository roleRepo;
}
