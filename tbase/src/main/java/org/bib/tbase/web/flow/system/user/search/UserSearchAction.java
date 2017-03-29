package org.bib.tbase.web.flow.system.user.search;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.pagination.PageRequestFactory;
import org.bib.pagination.PaginationCalculator;
import org.bib.pagination.PaginationWindow;
import org.bib.tbase.domain.user.User;
import org.bib.tbase.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.RequestContext;

@Component("userSearchAction")
public class UserSearchAction implements Serializable {
// Constructors

// Public Methods
	public String deleteUser(Long userId, RequestContext context) {
		String outcome = "success";
		
		try {
			userService.delete(userId);
			
			context.getFlashScope().put("msg", "Deleted user.");
		}
		catch(Exception e) {
			logger.error("Failed to delete user.", e);
			
			outcome = "error";
			
			context.getFlashScope().put("errMsg", "Failed to delete user.");
		}
		
		return outcome;
	}

	public String executeSearch(UserSearchForm userSearchForm, Pageable pageRequest, RequestContext context) {
		String outcome = "success";
		
		try {
			Page<User> userPage = userService.search(userSearchForm, pageRequest);
			
			context.getFlowScope().put("userPage", userPage);
			
			PaginationWindow pagerWindow = pagerCalc.calculateWindow(userPage);
			
			context.getFlowScope().put("window", pagerWindow);
		}
		catch(Exception e) {
			logger.error("Failed to execute search.", e);
			
			outcome = "error";
			
			context.getFlashScope().put("errMsg", "Search failed.");
		}
		
		return outcome;
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
	private static final Logger logger = LogManager.getLogger(UserSearchAction.class);
	
	/**
	 * User Service
	 */
	@Inject
	private UserService userService;
	
	/**
	 * Page Request Factory
	 */
	@Inject
	private PageRequestFactory pageRequestFactory;
	
	/**
	 * Pagination Window Calc
	 */
	@Inject
	private PaginationCalculator pagerCalc;
}
