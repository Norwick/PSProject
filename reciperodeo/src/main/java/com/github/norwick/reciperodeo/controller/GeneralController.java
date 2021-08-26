package com.github.norwick.reciperodeo.controller;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.norwick.reciperodeo.domain.User;
import com.github.norwick.reciperodeo.service.UserService;

/**
 * General controller for web pages
 * @author Norwick Lee
 *
 */
@Controller
public class GeneralController {
	
	//ironically bad names but i want lint to shush
	private static final String PN = "page_name";
	private static final String R = "register";
	private static final String LI = "login";
	private static final String LO = "logout";
	

	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	UserService userService;
	

	private Optional<User> getAuthenticatedUser() {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return Optional.empty();
		}
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		return userService.findByUsername(username);
	}
	
	
	//TODO edit to actually have username and also maybe directly have username
	/**
	 * Maps profile
	 * @param username username for user of current session
	 * @param model model shared with html page
	 * @return string that leads to web page for profile
	 */
	@GetMapping("/profile")
	public String profile(Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) return "redirect:/" + LO;
		User u = ou.get();
		model.addAttribute("username", u.getUsername());
		model.addAttribute("email", u.getEmail());
		boolean s = u.getSearchable();
		model.addAttribute("schecked",s);
		String sS = s ? "On" : "Off";
		model.addAttribute("searchable", sS);
		model.addAttribute(PN, "profile"); //redundant but oh well works for this
		return "profile";
	}
	
	@PostMapping("/profile")
	public String profileSave(HttpServletRequest request, Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) return "redirect:/" + LO;
		User u = ou.get();
		String newUsername = request.getParameter("newusername");
		String newEmail = request.getParameter("newemail");
		String newPassword = request.getParameter("newpassword");
		String newSearchable = request.getParameter("newsearchable");
		
		String changeVar = "";
		try {
			if (newUsername != null) {
				changeVar = "Username";
				u.setUsername(newUsername);
			} else if (newEmail != null) {
				changeVar = "Email";
				u.setEmail(newEmail);
			} else if (newPassword != null) {
				changeVar = "Password";
				if (newPassword.length() < 8) throw new DataIntegrityViolationException("Password is too short");
				//directly validated since hashing changes the length
				u.setHash(encoder.encode(newPassword));
			} else {
				changeVar = "Searchable";
				u.setSearchable(newSearchable != null);
			}
			u = userService.saveUser(u);
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("msg",changeVar + " is invalid");
			ou = userService.findById(u.getId());
			if (ou.isEmpty()) return "redirect:/" + LO;
			u = ou.get();
		}
		
		model.addAttribute("username", u.getUsername());
		model.addAttribute("email", u.getEmail());
		boolean s = u.getSearchable();
		model.addAttribute("schecked",s);
		String sS = s ? "On" : "Off";
		model.addAttribute("searchable", sS);
		model.addAttribute(PN, "profile"); //redundant but oh well works for this
		return "profile";
	}
	
	
	/**
	 * Maps index
	 * @param model model to store values to pass to web page
	 * @return string that leads to web page for index
	 */
	@GetMapping({"/", "/index"})
	public String index(Model model) {
		model.addAttribute(PN, "index");
		return "index";
	}
	
	/**
	 * Maps contact
	 * @param username username for user of current session
	 * @param model model shared with html page
	 * @return string that leads to web page for contact
	 */
	@GetMapping("/contact")
	public String contact(@RequestParam(name="username", required=false, defaultValue="Anon") String username, Model model) {
		model.addAttribute("username",username);
		model.addAttribute(PN, "contact");
		return "contact";
	}
	
	/**
	 * Displays registration page
	 * @param user to represent a valid user for form purposes
	 * @param model model used to pass infor to web page
	 * @return string that leads to web page for register
	 */
	@GetMapping("/register")
	public String register(User user, Model model) {
		model.addAttribute(PN, R);
		return R;
	}
	
	/**
	 * Checks registration page values
	 * @param user submitted user from registration form
	 * @param bindingResult results of form binding
	 * @param model model used to pass info to web page
	 * @return string that leads to correct web page
	 */
	@PostMapping("/register")
	public String checkUser(@Valid User user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(PN, R);
			return R;
		}
		if(userService.findByUsername(user.getUsername()).isPresent()) {
			model.addAttribute(PN, R);
			bindingResult.rejectValue("username", "error.username.exists", "The username is already in use.");
			return R;
		}
		
		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setUsername(user.getUsername());
		newUser.setSearchable(user.getSearchable());
		newUser.setHash(encoder.encode(user.getHash()));
		
		//somewhat blank the data
		user.setEmail("invalid@invalid.invalid");
		user.setUsername("invalid");
		user.setHash("invalidinvalid");
		user.setSearchable(false);
		
		userService.saveUser(newUser);
		
		model.addAttribute("registered", true);
		return "redirect:/index";
	}

	
	/**
	 * login mapping for get requests
	 * @param model model passed to page
	 * @return location of login page
	 */
	@GetMapping("/login")
	public String loginGet(Model model) {
		model.addAttribute(PN, LI);
		return LI;
	}

	/**
	 * login mapping for post requests
	 * @param model model passed to page
	 * @return location of login page
	 */
	@PostMapping("/login")
	public String login(Model model) {
		model.addAttribute(PN, LI);
		return LI;
	}
	
	//prolly bad security
	@PostMapping("/delete")
	public String delete(HttpServletRequest request) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isPresent()) {
			User u = ou.get();
			userService.removeUser(u);
		}
		HttpSession session= request.getSession(false);
        SecurityContextHolder.clearContext();
        if(session != null) {
            session.invalidate();
        }
		return "redirect:/login?delete=true";
		
	}
}
