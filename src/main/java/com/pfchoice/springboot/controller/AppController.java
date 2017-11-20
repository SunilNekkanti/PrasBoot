package com.pfchoice.springboot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pfchoice.springboot.model.Role;
import com.pfchoice.springboot.model.CurrentUser;
import com.pfchoice.springboot.model.LoginForm;
import com.pfchoice.springboot.service.CurrentUserService;

@Controller
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class AppController {

	@Autowired
	CurrentUserService currentUserService;

	/**
	 * @param modal
	 * @return
	 */
	@RequestMapping(value = { "/", "/login" })
	String login(ModelMap modal) {
		modal.addAttribute("title", "CRUD Example");
		LoginForm loginForm = new LoginForm();
		modal.addAttribute("loginForm", loginForm);
		return "login";
	}

	/**
	 * @param modal
	 * @return
	 */
	@RequestMapping("/home")
	String home(HttpSession session, ModelMap modal, @ModelAttribute("username") String username) {
		LoginForm loginForm ;
		if (!modal.containsAttribute("username")) {
			modal.addAttribute("username", username);
		}
		CurrentUser user = currentUserService.findByCurrentUsername(username);
		
		if (user != null && !modal.containsAttribute("userId")) {
			loginForm = new LoginForm();
			loginForm.setUsername(username);
			modal.addAttribute("userId", user.getId());
			loginForm.setUserId(user.getId());
			Role role = user.getRole();
			modal.addAttribute("roleId", role.getId());
			modal.addAttribute("roleName", role.getRole());
			loginForm.setRoleName(role.getRole());
			loginForm.setRoleId(role.getId());
			loginForm.setEffectiveYear(user.getEffectiveYear());
			if(user.getInsurance() != null)
				loginForm.setInsuranceId(user.getInsurance().getId());
			modal.addAttribute("loginUser", loginForm);
			session.setAttribute("loginUser", loginForm);
		}
		

		return "home";
	}

	/**
	 * @param page
	 * @return
	 */
	@RequestMapping("/partials/{page}")
	String partialHandler(ModelMap modal, @PathVariable("page") final String page,
			@ModelAttribute("username") String username, @ModelAttribute("roleName") String roleName) {
		modal.addAttribute("roleName", roleName);
		return page;
	}

	/**
	 * for 403 access denied page
	 * 
	 * @return
	 */
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accesssDenied() {
		System.out.println("inside /accessDenied section/");
		return "403";
	}

	@RequestMapping(value = "/getloginInfo", method = RequestMethod.GET)
	@ResponseBody
	public LoginForm getUserInfo(HttpSession session) {
		LoginForm loginUser = (LoginForm) session.getAttribute("loginUser");
		return loginUser;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, null);
		}
		new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
        .logout(request, response, null);

		return "redirect:/login";// You can redirect wherever you want,
										// but generally it's a good practice to
										// show login screen again.
	}
}
