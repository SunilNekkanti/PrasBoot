package com.pfchoice.springboot.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
		implements AuthenticationSuccessHandler {

	private List<String> leadHomePageRoles = Arrays.asList("ROLE_ADMIN", "ROLE_AGENT", "ROLE_CARE_COORDINATOR");

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	 
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
		clearAuthenticationAttributess(request);

		// since we have created our custom success handler, its up to us to
		// where
		// we will redirect the user after successfully login
		/*if (redirectUrl != null) {
			response.sendRedirect(redirectUrl);
		} else {
			if (authUser.getAuthorities().stream().filter(auth -> leadHomePageRoles.contains(auth.getAuthority()))
					.findAny().isPresent()) {
				response.sendRedirect("home#/lead/");
			} else {
				response.sendRedirect("home#/event");
			}

		}*/

	}
	
	protected void handle(HttpServletRequest request, 
		      HttpServletResponse response, Authentication authentication)
		      throws IOException {
		  
		        String targetUrl = determineTargetUrl(authentication);
		    	HttpSession session = request.getSession();
		 		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				session.setAttribute("username", authUser.getUsername());
				session.setMaxInactiveInterval(30 * 60);
				// set our response to OK status
				response.setStatus(HttpServletResponse.SC_OK);
		        if (response.isCommitted()) {
		            logger.debug(
		              "Response has already been committed. Unable to redirect to "
		              + targetUrl);
		            return;
		        }
		 
		        redirectStrategy.sendRedirect(request, response, targetUrl);
		    }
		 
		    protected String determineTargetUrl(Authentication authentication) {
		        boolean isLead = false;
		        boolean isEvent = false;
		        
		        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		        if (authUser.getAuthorities().stream().filter(auth -> leadHomePageRoles.contains(auth.getAuthority()))
						.findAny().isPresent()) {
		        	isLead = true;
		        	isEvent = false;
				} else {
					isLead = false;
					isEvent = true;
				}
		        
		 
		        if (isLead) {
		            return "/home#/membership";
		        } else if (isEvent) {
		            return "/home#/event";
		        } else {
		            throw new IllegalStateException();
		        }
		    }
		 
		    protected void clearAuthenticationAttributess(HttpServletRequest request) {
		        HttpSession session = request.getSession(false);
		        if (session == null) {
		            return;
		        }
		        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		    }
		 
		    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		        this.redirectStrategy = redirectStrategy;
		    }
		    protected RedirectStrategy getRedirectStrategy() {
		        return redirectStrategy;
		    }
}