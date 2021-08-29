package com.github.norwick.reciperodeo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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

import com.github.norwick.reciperodeo.domain.ContactMessage;
import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Tag;
import com.github.norwick.reciperodeo.domain.User;
import com.github.norwick.reciperodeo.service.ContactMessageService;
import com.github.norwick.reciperodeo.service.MD5Util;
import com.github.norwick.reciperodeo.service.RecipeService;
import com.github.norwick.reciperodeo.service.TagService;
import com.github.norwick.reciperodeo.service.UserService;

/**
 * General controller for web pages
 * @author Norwick Lee
 *
 */
@Controller
public class GeneralController implements ErrorController {
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	UserService userService;
	
	/**
	 * Error page
	 * @param model used to pass info to web page
	 * @return provided web page
	 */
	@RequestMapping("/" + U.E)
	public String err(Model model) {
		model.addAttribute(U.PN, "simple");
		return U.E;
	}
	
	/**
	 * Access forbidden with no logout
	 * @param model used to pass info to web page
	 * @return provided web page
	 */
	@GetMapping("/" + U.F)
	public String forbid(Model model) {
		model.addAttribute(U.PN, "simple");
		return U.F;
	}

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
	@GetMapping("/" + U.P)
	public String profile(Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) return U.RR + U.LO;
		User u = ou.get();
		model.addAttribute(U.US, u.getUsername());
		model.addAttribute("gravatar", "https://www.gravatar.com/avatar/" + MD5Util.md5Hex(u.getEmail().toLowerCase()));
		model.addAttribute("email", u.getEmail());
		boolean s = u.getSearchable();
		model.addAttribute("schecked",s);
		String sS = s ? "On" : "Off";
		model.addAttribute("searchable", sS);
		model.addAttribute(U.PN, U.P); //redundant but oh well works for this
		return U.P;
	}
	
	/**
	 * Takes requests to alter user information by user and saves or rejects it
	 * @param request contains post information about user change property
	 * @param model way to share info with web page
	 * @return profile page
	 */
	@PostMapping("/" + U.P)
	public String profileSave(HttpServletRequest request, Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) return U.RR + U.LO;
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
			if (ou.isEmpty()) return U.RR + U.LO;
			u = ou.get();
		}
		
		model.addAttribute(U.US, u.getUsername());
		model.addAttribute("email", u.getEmail());
		model.addAttribute("gravatar", "https://www.gravatar.com/avatar/" + MD5Util.md5Hex(u.getEmail().toLowerCase()));
		boolean s = u.getSearchable();
		model.addAttribute("schecked",s);
		String sS = s ? "On" : "Off";
		model.addAttribute("searchable", sS);
		model.addAttribute(U.PN, U.P); //redundant but oh well works for this
		return U.P;
	}
	
	
	/**
	 * Maps index
	 * @param page the page of results wanted to see (e.g. zero'th, first, etc)
	 * @param model model to store values to pass to web page
	 * @return string that leads to web page for index
	 */
	@GetMapping({"/", "/" + U.I})
	public String index(@RequestParam(required=true, defaultValue="0") int page, Model model) {
		if (page < 0) page = 0;
		Page<Recipe> pr = recipeService.searchPublicRecipes("", page, 28);
		setUpSearch(pr, "", page, model);
		model.addAttribute("source", U.I);
		return U.S;
	}
	
	/**
	 * Maps contact
	 * @param cm the filler for the contact message form to fill
	 * @param model model shared with html page
	 * @return string that leads to web page for contact
	 */
	@GetMapping("/" + U.C)
	public String contact(ContactMessage cm, Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) {
			cm.setName("anon");
			cm.setEmail("");
		} else {
			cm.setName(ou.get().getUsername());
			cm.setEmail(ou.get().getEmail());
		}
		model.addAttribute(U.PN, U.C);
		model.addAttribute("cm", cm);
		return U.C;
	}
	
	@Autowired
	ContactMessageService contactMessageService;
	
	/**
	 * Saves a valid contact message to database
	 * @param cm message to save
	 * @param bindingResult result of binding parameters to contact message
	 * @param model way to share info with page
	 * @return page to go to
	 */
	@PostMapping("/" + U.C)
	public String contactSave(@Valid ContactMessage cm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(U.PN, U.C);
			model.addAttribute("cm", cm);
			return U.C;
		}
		contactMessageService.saveContactMessage(cm);
		cm.setCreationTimestamp(null);
		cm.setEmail(null);
		cm.setId(-1);
		cm.setMessage(null);
		cm.setName(null);
		cm.setSubject(null);
		return U.RR + U.C + "?sent=true";
	}
	
	/**
	 * Displays registration page
	 * @param user to represent a valid user for form purposes
	 * @param model model used to pass infor to web page
	 * @return string that leads to web page for register
	 */
	@GetMapping("/" + U.R)
	public String register(User user, Model model) {
		model.addAttribute(U.PN, U.R);
		return U.R;
	}
	
	/**
	 * Checks registration page values
	 * @param user submitted user from registration form
	 * @param bindingResult results of form binding
	 * @param model model used to pass info to web page
	 * @return string that leads to correct web page
	 */
	@PostMapping("/" + U.R)
	public String checkUser(@Valid User user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(U.PN, U.R);
			return U.R;
		}
		if(userService.findByUsername(user.getUsername()).isPresent()) {
			model.addAttribute(U.PN, U.R);
			bindingResult.rejectValue(U.US, "error.username.exists", "The username is already in use.");
			return U.R;
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
		return U.RR + U.LI;
	}
	
	/**
	 * login mapping for get requests
	 * @param model model passed to page
	 * @return location of login page
	 */
	@RequestMapping(value = "/" + U.LI, method = { RequestMethod.GET, RequestMethod.POST })
	public String loginGet(Model model) {
		model.addAttribute(U.PN, U.LI);
		return U.LI;
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
		return U.RR + U.LI + "?delete=true";
		
	}
	
	/**
	 * Provides page where you can create a recipe
	 * @param recipe recipe to be bound to by page's create recipe form
	 * @param model way to pass info to page
	 * @return page that handles recipe creation
	 */
	@GetMapping("/" + U.RE)
	public String recipe(Recipe recipe, Model model) {
		model.addAttribute(U.PN, U.RE);
		return U.RE;
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
	@PostMapping("/" + U.RE)
	public String recipeCreate(@Valid Recipe r, BindingResult bindingResult, HttpServletRequest request, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(U.PN, U.RE);
			return U.RE;
		}
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) {
			return U.RR + U.F;
		}
		User u = ou.get();
		Optional<Recipe> or =( (r.getId() != null) ? recipeService.findById(r.getId()) : Optional.empty());
		if (or.isPresent()) {
			Recipe orig = or.get();
			if (!orig.getUser().equals(u)) {
				return U.RR + U.F;
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
		model.addAttribute(U.PN, U.RE);
		model.addAttribute(U.RE, r);
		model.addAttribute(U.RJ, r.getRecipeJSON());
		model.addAttribute("recipeid", r.getId().toString());
		return U.RE;
	}
	
	/**
	 * Place user can look up their owned recipes
	 * @param model way to pass info to page
	 * @return page that handles request
	 */
	@GetMapping("/" + U.RES)
	public String recipes(Model model) {
		Optional<User> ou = getAuthenticatedUser();
		if (ou.isEmpty()) {
			return U.RR + U.LI;
		}
		User u = ou.get();
		model.addAttribute("recipeSet", u.getRecipes());
		model.addAttribute(U.PN, U.RES);
		return U.RES;
	}
	
	/**
	 * Place to provide info on current wanted tags for recipe
	 * @param id id of recipe
	 * @param model way to pass info to web page
	 * @return web page that handles request
	 */
	@GetMapping("/" + U.RE + "/tag/{id}")
	public String addTags(@PathVariable UUID id, Model model) {
		Optional<Recipe> or = recipeService.findById(id);
		Optional<User> ou = getAuthenticatedUser();
		if (or.isEmpty()) {
			return U.RR + U.E;
		}
		Recipe r = or.get();
		if (ou.isEmpty() || !r.getUser().equals(ou.get())) {
			return U.RR + U.F;
		}
		
		Set<Tag> ts = r.getTags();
		StringJoiner sj = new StringJoiner(",");
		for (Tag t : ts) {
			sj.add(t.getName());
		}
		
		model.addAttribute("id", id.toString());
		model.addAttribute(U.T, r.getTitle());
		model.addAttribute("tags", sj.toString());
		model.addAttribute(U.PN, U.ART);
		return U.ART;
	}
	

	/**
	 * Saves tags to specified recipe if user is valid for recipe
	 * @param id id of recipe
	 * @param request request containing tags to save
	 * @param model way to pass info to web page
	 * @return page that handles request
	 */
	@PostMapping("/" + U.RE + "/delete/{id}")
	public String deleteRecipe(@PathVariable UUID id, Model model) {
		Optional<User> ou = getAuthenticatedUser();
		Optional<Recipe> or = recipeService.findById(id);
		if (or.isEmpty()) {
			return U.RR + U.E;
		}
		Recipe r = or.get();
		if (ou.isEmpty() || !r.getUser().equals(ou.get())) {
			return U.RR + U.F;
		}
		recipeService.removeRecipe(r);
		return U.RR + U.RES + "?delete=true";
		
	}
	/**
	 * Saves tags to specified recipe if user is valid for recipe
	 * @param id id of recipe
	 * @param request request containing tags to save
	 * @param model way to pass info to web page
	 * @return page that handles request
	 */
	@PostMapping("/" + U.RE + "/tag/{id}")
	public String saveTags(@PathVariable UUID id, HttpServletRequest request, Model model) {
		Optional<Recipe> or = recipeService.findById(id);
		Optional<User> ou = getAuthenticatedUser();
		if (or.isEmpty()) {
			return U.RR + U.E;
		}
		Recipe r = or.get();
		if (ou.isEmpty() || !r.getUser().equals(ou.get())) {
			return U.RR + U.F;
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
		model.addAttribute(U.T, r.getTitle());
		model.addAttribute("saved",true);
		model.addAttribute("tags", sj.toString());
		model.addAttribute(U.PN, U.ART);
		return U.ART;
	}
	
	/**
	 * Lets user view public and owned recipes
	 * @param id id of recipe
	 * @param r recipe itself
	 * @param model way to pass info to web site
	 * @return web page that handles request
	 */
	@GetMapping("/" + U.RE + "/{id}")
	public String recipeLookup(@PathVariable UUID id, Recipe r, Model model) {
		Optional<Recipe> or = recipeService.findById(id);
		if (or.isEmpty()) {
			return U.RR + U.E;
		}
		r = or.get();
		Optional<User> ou = getAuthenticatedUser();
		boolean editor = false;
		if (ou.isPresent() && r.getUser().equals(ou.get())) {
			editor = true;
			model.addAttribute("editor", editor);
		}
		if (r.getState() != Recipe.Visibility.PUBLIC && !editor) {
			return U.RR + U.F;
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
		model.addAttribute(U.T, r.getTitle());
		model.addAttribute("r", r);
		model.addAttribute("emoji", r.getEmoji());
		model.addAttribute(U.RJ, r.getRecipeJSON());
		model.addAttribute(U.PN, "viewRecipe");
    	return "viewRecipe";
	}
	
	/**
	 * Lets user edit previously created recipe
	 * @param id id of recipe
	 * @param r recipe to populate and pass
	 * @param model passes info to the page
	 * @return page that handles request
	 */
	@PostMapping("/" + U.RE + "/{id}")
	public String knownRecipeEdit(@PathVariable UUID id, Recipe r, Model model) {
		Optional<User> ou = getAuthenticatedUser();
		Optional<Recipe> or = recipeService.findById(id);
		if (or.isEmpty()) {
			return U.RR + U.E;
		}
		r = or.get();
		if (ou.isEmpty() || !r.getUser().equals(ou.get())) {
			System.out.println("correctly here");
			return U.RR + U.F;
		}
		model.addAttribute(U.RE, r);
		model.addAttribute(U.T, r.getTitle());
		model.addAttribute("emoji", r.getEmoji());
		model.addAttribute("state", r.getState());
		model.addAttribute("loadOnly", true);
		model.addAttribute(U.PN, U.RE);
		model.addAttribute(U.RJ, r.getRecipeJSON());
		model.addAttribute("recipeid", r.getId().toString());
		return U.RE;
	}
	
	/**
	 * Searches for all recipes 
	 * @param title title substring of recipe titles
	 * @param page the page we want in the repository of results
	 * @param model way to pass info to web page
	 * @return search page
	 */
	@GetMapping("/search")
	public String search(@RequestParam String title, @RequestParam(required=true,defaultValue="0") int page, Model model) {
		if (page < 0) {
			return U.R + U.S + "?" + U.T + "=" + title + "&page=0";
		}
		Page<Recipe> pr = recipeService.searchPublicRecipes(title, page, 28);
		setUpSearch(pr, title, page, model);
		return U.S;
	}
	
	/**
	 * Searches for recipes that contain all tags provided that exist
	 * @param tags list of potential tags
	 * @param page page of tags to return
	 * @param model how we pass info to web page
	 * @return web page for tags
	 */
	@GetMapping("/tags")
	public String tagSearch(@RequestParam(required=true,defaultValue="") List<String> tags, @RequestParam(required=true,defaultValue="0") int page, Model model) {
		Page<Recipe> pr;
		StringJoiner sj = new StringJoiner(", ");
		if (tags.isEmpty()) {
			model.addAttribute(U.PN, U.S);
			return U.TS;
		}
		List<String> processed = tags.stream().map(s -> s.trim().replaceAll(" +", " ").toUpperCase()).collect(Collectors.toList());
		List<Tag> validTags = tagService.getValidTags(processed);
		if (tags.size() != validTags.size()) {
			model.addAttribute("nonexistent", true);
		}
		if (validTags.isEmpty()) {
			pr = Page.empty();
		} else {
			pr = recipeService.searchByTag(validTags, page, 28);
		}
		
		if (pr.isEmpty()) {
			for (String s : processed) {
				sj.add(s);
			}
			model.addAttribute(U.PN, U.S);
			model.addAttribute(U.T, sj.toString());
			return U.TS;
			
		}

		for (Tag t : validTags) {
			sj.add(t.getName());
		}
		setUpSearch(pr, sj.toString(), page, model);
		model.addAttribute("source", "tags");
		return U.S;
	}
	
	/**
	 * returns page to search tags when there are no results
	 * @param model model to share info with the page
	 * @return empty tag search page
	 */
	@GetMapping("/" + U.TS)
	public String emptyTagSearch(Model model) {
		model.addAttribute(U.PN, U.S);
		return U.TS;
	}
	
	//searches for public recipes based on title and page
	private void setUpSearch(Page<Recipe> pr, String title, int page, Model model) {
		if (pr.getTotalPages() > page + 1) {
			model.addAttribute("next", true);
		}
		if (page > 0) {
			model.addAttribute("prev", true);
		}
		model.addAttribute(U.T, title);
		model.addAttribute("page", page);
		model.addAttribute("recipeList", pr.getContent());
		model.addAttribute(U.PN, U.S);
	}
	
}
