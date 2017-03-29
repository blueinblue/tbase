package org.bib.tbase.web.security;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

/**
 * Custom InvalidSessionStrategy that will forward to configured URLs based on the URL requested by the user when the invalid session error occured.
 * 
 * For example, if the user's session has expired, and they click the "Logout" link, we won't want to show them the invalid session error message.  So, 
 * this strategy looks at the requested url, /logout for example, and redirects to the login page without any noticeable error.
 * 
 * This strategy also handles session expired events that occur during AJAX requests.  If an AJAX request is made during an invalid session, the strategy returns
 * error code "902" to the client.  The client can inspect the error code and trigger a redirect to the login page if 902 is found.  Handling invalid sessions with
 * AJAX calls isn't well supported by any AJAX library (mostly because the server returns a HTTP 200 and a redirect instead of an error code), so this is a valid way
 * to deal with invalid sessions.
 */
public class PathMatchingRedirectInvalidSessionStrategy implements InvalidSessionStrategy {

// Constructors
	public PathMatchingRedirectInvalidSessionStrategy(String defaultInvalidSessionUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultInvalidSessionUrl), "defaultInvalidSessionUrl must start with '/' or with 'http(s)'");
		
		PathMatchingRedirectHandler defaultHandler = new PathMatchingRedirectHandler("/**", defaultInvalidSessionUrl);
		this.defaultInvalidSessionUrl = defaultInvalidSessionUrl;
		
		handlers.push(defaultHandler);
	}

// Public Methods
	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		/*
		 * Handle AJAX Requests
		 */
		String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");

		if ("XMLHttpRequest".equals(ajaxHeader)) {
			logger.debug("Detected an AJAX REQUEST to an invalid http session.  Sending error response to client.");
			
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.sendError(902);
			
			return;
		}
		
		/*
		 * Handle non-AJAX Requests
		 */
		if(createNewSession) {
			request.getSession();
		}
		
		logger.trace("request = {}", request.getRequestURI());
		
		String redirectUrl = defaultInvalidSessionUrl;
		
		// Iterate through handlers and see if requested url matches any of the ant paths of the configured handlers
		for(PathMatchingRedirectHandler handler : handlers) {
			if(handler.matches(request)) {
				redirectUrl = handler.getInvalidSessionUrl();
				
				break;
			}
		}
		
		logger.trace("Redirecting to: {}", redirectUrl);
		
		redirectStrategy.sendRedirect(request, response, redirectUrl);
	}
	
	/**
	 * Add a handler to the top of the chain.
	 * @param handler The handler instance, never null
	 */
	public void addHandler(PathMatchingRedirectHandler handler) {
		handlers.push(handler);
	}

// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(PathMatchingRedirectInvalidSessionStrategy.class);
	
	/**
	 * Redirect strategy 
	 */
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/**
	 * Create a new session before redirecting?
	 */
	private boolean createNewSession = true;
	
	/**
	 * Default redirect URL
	 */
	private String defaultInvalidSessionUrl = "";
	
	/**
	 * A stack for the handlers
	 */
	private final Deque<PathMatchingRedirectHandler> handlers = new ArrayDeque<PathMatchingRedirectHandler>();
	
	
// Helper Class
	public static class PathMatchingRedirectHandler {
		// Constructors
		/**
		 * If the request path is matched by the argument antPath, then redirect to the argument invalidSessionUrl.
		 * @param antPath The ant style path to match to the request, never empty/null
		 * @param invalidSessionUrl The url to redirect to, never empty/null, must start with a /
		 */
		public PathMatchingRedirectHandler(String antPath, String invalidSessionUrl) {
			Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "invalidSessionUrl must start with '/' or with 'http(s)'");
			
			this.antPathRequestMatcher = new AntPathRequestMatcher(antPath);
			this.invalidSessionUrl = invalidSessionUrl;
		}
		
		// Public Methods
		public boolean matches(HttpServletRequest request) {
			boolean matches = false;
			
			if(request != null) {
				matches = antPathRequestMatcher.matches(request);
			}
			
			return matches;
		}
		
		// Getters
		public String getInvalidSessionUrl() {
			return invalidSessionUrl;
		}
		
		public String getAntPath() {
			return antPathRequestMatcher.getPattern();
		}
		
		// Attributes
		/**
		 * If the request path matches this ant path, then we redirect to the invalidSessionUrl
		 */
		private final AntPathRequestMatcher antPathRequestMatcher;
		
		/**
		 * URL to redirect to if the request path matches the any path request matcher - must start with a /
		 */
		private final String invalidSessionUrl;
	}
}
