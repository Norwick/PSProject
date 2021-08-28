package com.github.norwick.reciperodeo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Tag;
import com.github.norwick.reciperodeo.domain.User;
import com.github.norwick.reciperodeo.service.RecipeService;
import com.github.norwick.reciperodeo.service.TagService;
import com.github.norwick.reciperodeo.service.UserService;

/**
 * General controller for web pages
 * @author Norwick Lee
 *
 */
@Controller
public class GeneralController {
	
	//ironically bad names but i want lint to shush
	private static final String RR = "redirect:/";
	private static final String PN = "page_name";
	private static final String R = "register";
	private static final String RE = "recipe";
	private static final String RES = "recipes";
	private static final String LI = "login";
	private static final String LO = "logout";
	private static final String P = "profile";
	private static final String U = "username";
	private static final String I = "index";
	private static final String T = "title";
	private static final String ART = "addRecipeTags";
	

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
	
	/**
	 * Maps profile
	 * @param username username for user of current session
	 * @param model model shared with html page
	 * @return string that leads to web page for profile
	 */
	@GetMapping("/" + P)
	public String profile(Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) return RR + LO;
		User u = ou.get();
		model.addAttribute(U, u.getUsername());
		model.addAttribute("email", u.getEmail());
		boolean s = u.getSearchable();
		model.addAttribute("schecked",s);
		String sS = s ? "On" : "Off";
		model.addAttribute("searchable", sS);
		model.addAttribute(PN, P); //redundant but oh well works for this
		return P;
	}
	
	/**
	 * Takes requests to alter user information by user and saves or rejects it
	 * @param request contains post information about user change property
	 * @param model way to share info with web page
	 * @return profile page
	 */
	@PostMapping("/" + P)
	public String profileSave(HttpServletRequest request, Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) return RR + LO;
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
			if (ou.isEmpty()) return RR + LO;
			u = ou.get();
		}
		
		model.addAttribute(U, u.getUsername());
		model.addAttribute("email", u.getEmail());
		boolean s = u.getSearchable();
		model.addAttribute("schecked",s);
		String sS = s ? "On" : "Off";
		model.addAttribute("searchable", sS);
		model.addAttribute(PN, P); //redundant but oh well works for this
		return P;
	}
	
	
	/**
	 * Maps index
	 * @param model model to store values to pass to web page
	 * @return string that leads to web page for index
	 */
	@GetMapping({"/", "/" + I})
	public String index(Model model) {
		List<Recipe> lr = recipeService.findByStateOrderByCreationTimestamp(Recipe.Visibility.PUBLIC);
		model.addAttribute("recipeList", lr);
		model.addAttribute(PN, I);
		return I;
	}
	
	/**
	 * Maps contact
	 * @param username username for user of current session
	 * @param model model shared with html page
	 * @return string that leads to web page for contact
	 */
	@GetMapping("/contact")
	public String contact(@RequestParam(name=U, required=false, defaultValue="Anon") String username, Model model) {
		model.addAttribute(U,username);
		model.addAttribute(PN, "contact");
		return "contact";
	}
	
	/**
	 * Displays registration page
	 * @param user to represent a valid user for form purposes
	 * @param model model used to pass infor to web page
	 * @return string that leads to web page for register
	 */
	@GetMapping("/" + R)
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
	@PostMapping("/" + R)
	public String checkUser(@Valid User user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(PN, R);
			return R;
		}
		if(userService.findByUsername(user.getUsername()).isPresent()) {
			model.addAttribute(PN, R);
			bindingResult.rejectValue(U, "error.username.exists", "The username is already in use.");
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
		return RR + I;
	}
	
	/**
	 * login mapping for get requests
	 * @param model model passed to page
	 * @return location of login page
	 */
	@RequestMapping(value = "/" + LI, method = { RequestMethod.GET, RequestMethod.POST })
	public String loginGet(Model model) {
		model.addAttribute(PN, LI);
		return LI;
		//probably can delete the postmapping but don't want to bugcheck for it rn
	}
	
	/**
	 * Lets user delete their account. Currently doesn't check where post request came from...
	 * @param request shares post variables
	 * @return redirect page
	 */
	//prolly bad security
	@PostMapping("/user/delete")
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
		return RR + LI + "?delete=true";
		
	}
	
	/**
	 * Provides page where you can create a recipe
	 * @param r recipe to be bound to by page's create recipe form
	 * @param model way to pass info to page
	 * @return page that handles recipe creation
	 */
	@GetMapping("/" + RE)
	public String recipe(Recipe r, Model model) {
		model.addAttribute(PN, RE);
		return RE;
	}
	
	@Autowired
	RecipeService recipeService;
	
	@Autowired
	TagService tagService;
	
	/**
	 * Handles creation of recipe
	 * @param r recipe that is being edited/saved
	 * @param bindingResult issues with binding provided form info to a recipe
	 * @param request way to access post parameters
	 * @param model way to pass info to page
	 * @return page that handles request
	 */
	@PostMapping("/" + RE)
	public String recipeCreate(@Valid Recipe r, BindingResult bindingResult, HttpServletRequest request, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(PN, RE);
			return RE;
		}
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) {
			return RR + RE;
		}
		User u = ou.get();
		Optional<Recipe> or =( (r.getId() != null) ? recipeService.findById(r.getId()) : Optional.empty());
		if (or.isPresent()) {
			Recipe orig = or.get();
			if (!orig.getUser().equals(u)) {
				return RR + RE;
			}
			orig.setRecipeJSON(r.getRecipeJSON());
			orig.setTitle(r.getTitle());
			orig.setState(r.getState());
			orig.setEmoji(r.getEmoji());
			r = orig;
		} else {
			r.setCreationTimestamp(new Date()); //stopgap
			r.setUser(u);
		}
		r.setEditTimestamp(new Date()); //unsure if needed
		r = recipeService.saveRecipe(r);
		model.addAttribute("rsaved", true);
		model.addAttribute(PN, RE);
		model.addAttribute(RE, r);
		model.addAttribute("recipeJson", r.getRecipeJSON());
		model.addAttribute("recipeid", r.getId().toString());
		return RE;
	}
	
	/**
	 * Place user can look up their owned recipes
	 * @param model way to pass info to page
	 * @return page that handles request
	 */
	@GetMapping("/" + RES)
	public String recipes(Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) {
			return RR + LI;
		}
		User u = ou.get();
		model.addAttribute("recipeSet", u.getRecipes());
		model.addAttribute(PN, RES);
		return RES;
	}
	
	/**
	 * Place to provide info on current wanted tags for recipe
	 * @param id id of recipe
	 * @param model way to pass info to web page
	 * @return web page that handles request
	 */
	@GetMapping("/" + RE + "/tag/{id}")
	public String addTags(@PathVariable UUID id, Model model) {
		Optional<Recipe> or = recipeService.findById(id);
		Optional<User> ou = getAuthenticatedUser();
		if (or.isEmpty() || ou.isEmpty()) {
			return RR + LI;
		}
		Recipe r = or.get();
		if (!r.getUser().equals(ou.get())) {
			return RR + LI;
		}
		
		Set<Tag> ts = r.getTags();
		StringJoiner sj = new StringJoiner(",");
		for (Tag t : ts) {
			sj.add(t.getName());
		}
		
		model.addAttribute("id", id.toString());
		model.addAttribute(T, r.getTitle());
		model.addAttribute("tags", sj.toString());
		model.addAttribute(PN, ART);
		return ART;
	}
	

	/**
	 * Saves tags to specified recipe if user is valid for recipe
	 * @param id id of recipe
	 * @param request request containing tags to save
	 * @param model way to pass info to web page
	 * @return page that handles request
	 */
	@PostMapping("/" + RE + "/delete/{id}")
	public String deleteRecipe(@PathVariable UUID id, Model model) {
		Optional<User> ou = getAuthenticatedUser();
		Optional<Recipe> or = recipeService.findById(id);
		if (ou.isEmpty() || or.isEmpty()) {
			return RR + I;
		}
		Recipe r = or.get();
		if (!r.getUser().equals(ou.get())) {
			return RR + I;
		}
		recipeService.removeRecipe(r);
		return RR + RES + "?delete=true";
		
	}
	/**
	 * Saves tags to specified recipe if user is valid for recipe
	 * @param id id of recipe
	 * @param request request containing tags to save
	 * @param model way to pass info to web page
	 * @return page that handles request
	 */
	@PostMapping("/" + RE + "/tag/{id}")
	public String saveTags(@PathVariable UUID id, HttpServletRequest request, Model model) {
		Optional<Recipe> or = recipeService.findById(id);
		Optional<User> ou = getAuthenticatedUser();
		if (or.isEmpty() || ou.isEmpty()) {
			return RR + LI;
		}
		Recipe r = or.get();
		if (!r.getUser().equals(ou.get())) {
			return RR + LI;
		}
		String tags = request.getParameter("tags");
		String trimTags = tags.trim().replaceAll(" +", " ");
		String[] splitTags = trimTags.split(",");

		StringJoiner sj = new StringJoiner(",");
		for (int i = 0; i < splitTags.length; ++i) {
			splitTags[i] = splitTags[i].trim().toUpperCase();
			Optional<Tag> ot = tagService.findByName(splitTags[i]);
			if (ot.isEmpty()) {
				ot = Optional.of(new Tag(splitTags[i]));
			}
			Tag t = ot.get();
			t.addRecipe(r);
			tagService.saveTag(t);
			sj.add(splitTags[i]);
		}
		model.addAttribute("id", id.toString());
		model.addAttribute(T, r.getTitle());
		model.addAttribute("saved",true);
		model.addAttribute("tags", sj.toString());
		model.addAttribute(PN, ART);
		return ART;
	}
	
	
	/**
	 * Lets user view public and owned recipes
	 * @param id id of recipe
	 * @param r recipe itself
	 * @param model way to pass info to web site
	 * @return web page that handles request
	 */
	@GetMapping("/" + RE + "/{id}")
	public String recipeLookup(@PathVariable UUID id, Recipe r, Model model) {
		Optional<Recipe> or = recipeService.findById(id);
		if (or.isEmpty()) {
			return RR + LI;
		}
		r = or.get();
		Optional<User> ou = getAuthenticatedUser();
		boolean editor = false;
		if (ou.isPresent() && r.getUser().equals(ou.get())) {
			editor = true;
			model.addAttribute("editor", editor);
		}
		if (r.getState() != Recipe.Visibility.PUBLIC && !editor) {
			return RR + LI;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");
		String createDate = sdf.format(r.getCreationTimestamp());
		String editDate = sdf.format(r.getEditTimestamp());
		
		StringJoiner sj = new StringJoiner(",");
		Set<Tag> tags = r.getTags();
		for (Tag t : tags) {
			sj.add(t.getName());
		}
		
		model.addAttribute("creator", r.getUser().getUsername());
		model.addAttribute("createDate", createDate);
		model.addAttribute("editDate", editDate);
		model.addAttribute("tags", sj.toString());
		model.addAttribute(T, r.getTitle());
		model.addAttribute("r", r);
		model.addAttribute("emoji", r.getEmoji());
		model.addAttribute("recipeJson", r.getRecipeJSON());
		model.addAttribute(PN, "viewRecipe");
    	return "viewRecipe";
	}
	
	/**
	 * Lets user edit previously created recipe
	 * @param id id of recipe
	 * @param r recipe to populate and pass
	 * @param model passes info to the page
	 * @return page that handles request
	 */
	@PostMapping("/" + RE + "/{id}")
	public String knownRecipeEdit(@PathVariable UUID id, Recipe r, Model model) {
		Optional<User> ou = getAuthenticatedUser();
		Optional<Recipe> or = recipeService.findById(id);
		if (ou.isEmpty() || or.isEmpty()) {
			return RR + I;
		}
		r = or.get();
		User u = ou.get();
		if (!r.getUser().equals(u)) {
			return RR + RE + "/" + id;
		}
		model.addAttribute(RE, r);
		model.addAttribute(T, r.getTitle());
		model.addAttribute("emoji", r.getEmoji());
		model.addAttribute("state", r.getState());
		model.addAttribute("loadOnly", true);
		model.addAttribute(PN, RE);
		model.addAttribute("recipeJson", r.getRecipeJSON());
		model.addAttribute("recipeid", r.getId().toString());
		return RE;
	}
}
