package org.bib.tbase.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/**
 * Basically the same behavior as the Spring Security provided SavedRequestAwareAuthenticationSuccessHandler, but this implementation adds a "/" to the 
 * redirect target URL if it points to the context root.
 * 
 * Had to do this because: if a user requests the app without a trailing slash, http://myserver.org/myapp, then spring security stores the authentication
 * information under /myapp/ (with trailing slash).  After a successful auth, spring security then tries to redirect the user back to http://myserver.org/myapp (no trailing slash).
 * It can't find the stored auth information, so it loops them back to the login page.  This is just a little work around to prevent this issue.
 * 
 */
public class CustomSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
// Constructors
	/**
	 * If no saved request is present, then use the argument defaultTargetUrl.  If always use, then use the default no matter what the previous request was.
	 * 
	 * @param defaultTargetUrl
	 * @param alwaysUseDefaultTargetUrl
	 */
	public CustomSavedRequestAwareAuthenticationSuccessHandler(String defaultTargetUrl, boolean alwaysUseDefaultTargetUrl) {
		super.setDefaultTargetUrl(defaultTargetUrl);
		super.setAlwaysUseDefaultTargetUrl(alwaysUseDefaultTargetUrl);
	}

// Public Methods
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,	HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if (savedRequest == null) {
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}
		
		String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}

		clearAuthenticationAttributes(request);

		// Use the DefaultSavedRequest URL
		String targetUrl = savedRequest.getRedirectUrl();
		String contextPath = request.getContextPath() == null ? "" : request.getContextPath();
		
		// If the target url matches the context path (no trailing slash) then add a trailing slash
		if(targetUrl != null && targetUrl.toUpperCase().endsWith(contextPath.toUpperCase())) {
			targetUrl = targetUrl + "/";
		}
		
		logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
		
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
	
// Getters & Setters
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

// Attributes
	/**
	 * Request cache
	 */
	private RequestCache requestCache = new HttpSessionRequestCache();
}
