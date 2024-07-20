package com.bookshop.controller;

import com.bookshop.dto.RegisterDTO;
import com.bookshop.filter.JwtUtil;

import com.bookshop.model.User;
import com.bookshop.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	@GetMapping("/loginpage")
	public String form() {
		return "login.html";

	}

	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
	                    Model model, HttpSession session) {
        logger.info("Entry point of login {}", username);
		try {

	        User user = userService.validateUser(username);
	        
	        if (user != null && user.getPassword().equals(password)) {
	        	String token = JwtUtil.generateToken(user.getUsername(), user.getRole().getRoleName());
	            session.setAttribute("token", token);
	            logger.info("token"+token);
	            model.addAttribute("token", token);
	            model.addAttribute("username", user.getUsername());
	            model.addAttribute("userId", user.getUserId());
	            model.addAttribute("rolename", user.getRole().getRoleName());

	            session.setAttribute("username", username);
	            session.setAttribute("userId", user.getUserId());
	            session.setAttribute("roleId", user.getRole().getRoleId());
	            session.setAttribute("rolename", user.getRole().getRoleName());
	            logger.info("Exit point #1 of login");
	            return "redirect:/home";
	        } else {
	            model.addAttribute("error", "Invalid username or password.");
	            return "login.html";
	        }
	    } catch (Exception e) {
	        logger.error("Exit point #2 of username ", username, e);	        
	        return "login.html";
	    }
	}

	@GetMapping("/home")
	public ResponseEntity<String> home(HttpSession session, Model model,@RequestHeader HttpHeaders headers) {
		logger.info("Entry point of home ");
		try {					
		String username = (String) session.getAttribute("username");
		Integer userId = (Integer) session.getAttribute("userId");
		Integer roleId = (Integer) session.getAttribute("roleId");
		String rolename = (String) session.getAttribute("rolename");
		String token = (String) session.getAttribute("token");
		
		headers.add(HttpHeaders.AUTHORIZATION,token);
		headers.setContentType(MediaType.TEXT_HTML);
		session.setAttribute("token", token);
		session.setAttribute("userId", userId);
		model.addAttribute("username", username);
		model.addAttribute("userId", userId);
		model.addAttribute("roleId", roleId);
		model.addAttribute("rolename", rolename);
		model.addAttribute("token",token);
		logger.info("rolename" + rolename);
		if (roleId != null) {
			switch (roleId) {
			case 1:
				ClassPathResource resource = new ClassPathResource("templates/admin.html");
                String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                htmlContent = htmlContent.replaceAll("username", username);
                htmlContent = htmlContent.replaceAll("rolename", rolename);
                logger.info("Exit point #1 of home");
                logger.info(htmlContent);
                return new ResponseEntity<>(htmlContent, headers, HttpStatus.OK);
			case 2:
				ClassPathResource resource1 = new ClassPathResource("templates/seller.html");
                String htmlContent1 = StreamUtils.copyToString(resource1.getInputStream(), StandardCharsets.UTF_8);
                htmlContent1 = htmlContent1.replaceAll("username", username);
                htmlContent1 = htmlContent1.replaceAll("rolename", rolename);
                htmlContent1 = htmlContent1.replace("userId", String.valueOf(userId));
                logger.info("Exit point #1 of home");
                return new ResponseEntity<>(htmlContent1, headers, HttpStatus.OK);
			case 3:
				ClassPathResource resource2= new ClassPathResource("templates/customer.html");
                String htmlContent2 = StreamUtils.copyToString(resource2.getInputStream(), StandardCharsets.UTF_8);
                htmlContent2 = htmlContent2.replaceAll("username", username);
                htmlContent2 = htmlContent2.replaceAll("rolename", rolename);
                htmlContent2 = htmlContent2.replace("userId", String.valueOf(userId));
                logger.info(htmlContent2);
                logger.info("Exit point #1 of home");
                return new ResponseEntity<>(htmlContent2, headers, HttpStatus.OK);
			default:
				logger.info("Exit poinnt #1 of home");
				return ResponseEntity.ok("login");				
			}
		} else {
			logger.info("Exit poinnt #2 of home");
			return ResponseEntity.ok("login");
		}
		}catch(Exception e){
			logger.info("Exit point #3 of home ",e);
			return ResponseEntity.ok("login");
		}
				
	}

	@GetMapping("/profilepage/{username}")
	public String profile(@PathVariable("username") String username, Model model) {
		logger.info("Entry point of profile page {}",username);
		try {		
			User user = userService.validateUser(username);
			model.addAttribute("rolename", user.getRole().getRoleName());
			model.addAttribute(user);
			logger.info("Exit point #1 of profilepage");
			return "profile.html";
		} catch (Exception e) {
			logger.info("Exit point #2 of profile page",username);
			return null;
		}
	}
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		logger.info("Entry point of register page");
		try {
		
		model.addAttribute("user", new User());
		logger.info("Entry point #1 of regitser page");
		return "register.html";
	}catch(Exception e) {
		logger.info("Exit point #2 of register page");
		return "login.html";
	}
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO) {
        try {
        	User user = new User();
        	user.setUsername(registerDTO.getUsername());
        	user.setPassword(registerDTO.getPassword());
        	user.setMobile(registerDTO.getMobile());
        	user.setRoleId(registerDTO.getRole());
            userService.save(user);
            return ResponseEntity.ok("{\"message\": \"Registration successful!\"}");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Username already exists. Please choose another username.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Error while registering user.\"}");
        }
    }

	@GetMapping("/seller")
	public String sellerPage(HttpSession session, Model model) {
		logger.info("Entry point of seller");
		try {		
		String username = (String) session.getAttribute("username");
		if (username != null) {
			User user = userService.validateUser(username);
			if (user != null) {
				model.addAttribute("user", user);
				model.addAttribute("userId",user.getUserId());
				model.addAttribute("username", username);
				model.addAttribute("rolename", user.getRole().getRoleName());
				model.addAttribute("mobile", user.getMobile());
				logger.info("Exit point #1 of seller");
			}
		}
		}catch(Exception e) {
			logger.error("Error while redirect to seller");
			logger.info("Exit point #2 of seller");
		}
		return "seller.html";
	}
}