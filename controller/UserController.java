package com.application.backend.controller;

import java.util.*;

import com.application.backend.dao.CartDao;
import com.application.backend.helper.CheckRole;
import com.application.backend.configuration.CustomAuthentication;
import com.application.backend.dao.AuthProviderDao;
import com.application.backend.dao.RoleDao;
import com.application.backend.dao.StateDao;
import com.application.backend.model.AuthProvider;
import com.application.backend.model.Role;
import com.application.backend.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.application.backend.configuration.jwt.JwtUtils;
import com.application.backend.email.EmailHandler;
import com.application.backend.exception.CatchException;
import com.application.backend.helper.Helper;
import com.application.backend.model.User;
import com.application.backend.service.UserService;
import com.application.backend.userdetails.CustomUserDetails;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private AuthProviderDao authProviderDao;
	
	@Autowired
	AuthenticationManager authenticationManager;
	 
	@Autowired
	PasswordEncoder encoder;


	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private StateDao stateDao;

	@Autowired
	private EmailHandler emailHandler;

	@Autowired
	private CartDao cartDao;
	
	/** --------------  Authentication -----------------*/

	public Map<String, Object> getRoleData (Role role) {
		Map<String,Object> info = new HashMap<String,Object>();

		info.put("roleName", role.getRoleName());
		info.put("isCreate", role.isCREATE());
		info.put("isRead", role.isREAD());
		info.put("isUpdate", role.isUPDATE());
		info.put("isDelete", role.isDELETE());

		return info;
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login( @RequestBody  User userInfo) {
		User user = userService.findByEmail(userInfo.getEmail());
		if(user != null && user.getPassword().equals("---------")){
			throw new CatchException("You don't have local account for this email. Try login with another type or sign up and use this email!",HttpStatus.UNAUTHORIZED);
		}


		Authentication authentication = authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword()));

		    SecurityContextHolder.getContext().setAuthentication(authentication);
		    String jwt = jwtUtils.createToken(authentication);
		    
		    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();  

			userService.login(userDetails.getEmail());

		    Map<String,Object> info = new HashMap<String,Object>();
			info.put("email", userDetails.getEmail());
			info.put("name", userDetails.getName());
			info.put("phone", userDetails.getUser().getPhone());
		    info.put("role",getRoleData(userDetails.getRole()));
		    info.put("token", jwt);
			info.put("cartItems", cartDao.countCart(userDetails.getUser().getId()));
		    
		    return Helper.res(info, HttpStatus.OK);
		  
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody User userInfo)  {
		User findUser = userService.findByEmail(userInfo.getEmail());
		if(findUser != null)
		{
			if(findUser.getPassword().equals("---------")) {
				findUser.setPhone(userInfo.getPhone());
				findUser.setName(userInfo.getName());
				findUser.setPassword(encoder.encode(userInfo.getPassword()));

				findUser.getAuthProvider().add(authProviderDao.findByProviderName("LOCAL"));

				userService.addUser(findUser);
			}
			else throw new CatchException("Email has been used!", HttpStatus.CONFLICT);
		}

		else {

			Role role = roleDao.findByRoleName("CLIENT");
			State state = stateDao.findByStateName("USER_PENDING");

			userInfo.setPassword(encoder.encode(userInfo.getPassword()));
			userInfo.setRole(role);
			userInfo.setState(state);
			userInfo.addAuthProvider(authProviderDao.findByProviderName("LOCAL"));

			userService.addUser(userInfo);
		}



		Map<String, String> data = new HashMap<>();
		if(findUser != null) data.put("used", "used");

		// create a thread to send email so that it will not block the process
		new Thread(new Runnable() {
		    public void run() {
		    	try {
					emailHandler.sendActiveEmail(userInfo.getEmail());
				} catch (MessagingException e) {
					e.printStackTrace();
				}
		    }
		}).start();

		return Helper.res(data, HttpStatus.OK);

	}

	@PostMapping("/activeAccount")
	public ResponseEntity<Object> activeAccount(@RequestBody Map<String, String> data) {
		userService.activeAccount(data.get("email"));
		return Helper.res("Activied!", HttpStatus.OK);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<Object> changePassword(@RequestBody Map<String, String> data) {
		String oldPassword = data.get("oldPassword");
		String newPassword = data.get("newPassword");
		String retypePassword = data.get("retypePassword");

		if (!newPassword.equals(retypePassword)) {
			throw new CatchException("New password and retype password must be the same!", HttpStatus.BAD_REQUEST);
		}
		if (newPassword.equals(oldPassword)) {
			throw new CatchException("New password and old password must not be the same!", HttpStatus.BAD_REQUEST);
		}


		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!BCrypt.checkpw(oldPassword, user.getPassword())) {
			throw new CatchException("Old password is not correct!", HttpStatus.UNAUTHORIZED);
		}

		userService.changePassword(user.getEmail(), encoder.encode(newPassword));
		return Helper.res("Success!", HttpStatus.OK);
	}


	@PostMapping("/verifyEmail")
	public ResponseEntity<Object> verifyEmail(@RequestBody Map<String, String> data){

		User user = userService.findByEmail(data.get("email"));

		if(user == null) throw new CatchException("User is not found!", HttpStatus.NOT_FOUND);
		if(user.getPassword().equals("---------")) throw new CatchException("You don't have local account for this email. Try login with another type or sign up and use this email!",HttpStatus.UNAUTHORIZED);

		userService.activeAccount(data.get("email"));

		// send verify email
		String verifyCode = Helper.createVerifyCode();
		// create a thread to send email so that it will not block the process
		new Thread(new Runnable() {
			public void run() {
				try {
					emailHandler.sendVerifyEmail(data.get("email"),verifyCode);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}).start();

		return Helper.res(encoder.encode(verifyCode), HttpStatus.OK);
	}

	@PostMapping("/createNewPassword")
	public ResponseEntity<Object> createNewPassword(@RequestBody Map<String, String> data){
		String email = data.get("email");
		String newPassword = data.get("newPassword");
		String retypePassword = data.get("retypePassword");

		if (!newPassword.equals(retypePassword)) {
			throw new CatchException("New password and retype password must be the same!", HttpStatus.BAD_REQUEST);
		}
		userService.changePassword(email,encoder.encode(newPassword));


		return Helper.res("Success!", HttpStatus.OK);
	}

	@PostMapping("/oauth/login")
	public ResponseEntity<Object> oauthLogin(@RequestBody  Map<String,String>  userInfo) {
		String email = userInfo.get("email");
		String name = userInfo.get("name");
		String type = userInfo.get("type");

		User user = userService.findByEmail(email);

		Role role = roleDao.findByRoleName("CLIENT");
		State enable = stateDao.findByStateName("USER_ENABLE");
		AuthProvider authProvider = authProviderDao.findByProviderName(type);

		// if email not used
		if(user == null) {
			User temp = new User(email,"---------", name, "0000000000", role,enable);
			user = userService.addUser(temp);
		}

		// email has been used
		else {
			user.setState(enable);
			user.addAuthProvider(authProvider);
			user = userService.addUser(user);
		}

		CustomAuthentication authentication = new CustomAuthentication(user);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.createOAuthToken(authentication);

		User userDetails = (User) authentication.getPrincipal();


		userService.login(userDetails.getEmail());
		Map<String,Object> info = new HashMap<String,Object>();
		info.put("email", userDetails.getEmail());
		info.put("name", userDetails.getName());
		info.put("phone", userDetails.getPhone());
		info.put("role",getRoleData(userDetails.getRole()));
		info.put("token", jwt);
		info.put("cartItems", cartDao.countCart(userDetails.getId()));
		return ResponseEntity.ok(info);

	}

	@GetMapping("/users/profile")
	public ResponseEntity<Object> getProfile( @RequestParam String email) {
		User user = userService.findByEmail(email);
		Map<String,String> data = new HashMap<>();
		data.put("name", user.getName());
		data.put("phone", user.getPhone());
		return Helper.res(data, HttpStatus.OK);

	}

	@PostMapping("/users/update-profile")
	public ResponseEntity<Object> updateProfile(@RequestBody Map<String, String> data) {
		userService.updateProfile(data);
		return Helper.res("Success!", HttpStatus.OK);

	}

	//-----------------Admin service

	@GetMapping("/read/users/count")
	public ResponseEntity<Object> count() {
		CheckRole.isRead();

		return Helper.res(userService.count(), HttpStatus.OK);

	}


	@GetMapping("/read/users")
	public ResponseEntity<Object> getUsers(@RequestParam  Integer page,@RequestParam  Integer limit,
										   @RequestParam (required = false)  Integer time,
										   @RequestParam (required = false) String unit ,
										   @RequestParam (required = false) String searching,
										   @RequestParam (required = false) Long userId) {
		CheckRole.isRead();

		Map<String, Object> users = null;
		Date datePrevious = null;

		if(time != null) {
			Integer unitValue = Helper.getTimeUnitValue(unit);

			// get the milestone from today
			Date currentDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(Calendar.DAY_OF_YEAR, - time * unitValue);

			datePrevious = calendar.getTime();
		}

		users = userService.getUsers(page, limit, datePrevious,searching);

		if(userId != null ) users = userService.findById(userId);

		return Helper.res(users, HttpStatus.OK);
	}

	@DeleteMapping("/delete/users")
	public ResponseEntity<Object> deleteUser(@RequestParam String email) {

		CheckRole.isDelete();

		userService.deleteUser(email);
		return ResponseEntity.ok("Success!");
	}

	@PutMapping("/update/users/update-one")
	public ResponseEntity<Object> updateOne(@RequestBody Map<String,String>  info) {

		CheckRole.isUpdate();

		String email = info.get("email");
		String role = info.get("role");
		String state = info.get("state");

		userService.updateUser(email, role,state);
		return ResponseEntity.ok("Success!");
	}

	@PutMapping("/update/users/update-many")
	public ResponseEntity<Object> updateMany(@RequestBody Map<String,Object>  info) {

		CheckRole.isUpdate();

		List<String> emails = info.get("emails")!= null ? (List<String>) info.get("emails") : null;
		String role = info.get("role") != null ? (String) info.get("role") : null;
		String state = info.get("state")!= null ? (String)info.get("state") : null;


		userService.updateUsers(emails, role,state);
		return ResponseEntity.ok("Success!");
	}

	@PostMapping("/create/users/send-email")
	public ResponseEntity<Object> sendEmails(@RequestBody Map<String,Object>  info) throws MessagingException {
		CheckRole.isCreate();

		List<String> emails = (List<String>) info.get("emails");
		Map<String,String> emailContent = (Map<String,String>) info.get("emailData");

		// create a thread to send email so that it will not block the process
		new Thread(new Runnable() {
			public void run() {
				try {
					for(String email: emails){
						emailHandler.sendEmail(email,emailContent.get("subject"), emailContent.get("content"), null);
					}
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}).start();

		return ResponseEntity.ok("Success!");
	}

	@GetMapping("/read/user")
	public ResponseEntity<Object> getUser(@RequestParam String email) {
		CheckRole.isRead();
		Map<String, Object> data = userService.getUser(email);
		return Helper.res(data, HttpStatus.OK);

	}

	@PostMapping("/create/user")
	public ResponseEntity<Object> createUser(@RequestBody Map<String,Object> info) {
		CheckRole.isCreate();

		String email = (String) info.get("email");
		User temp = userService.findByEmail(email);
		if(temp != null) throw new CatchException("This email has been used!", HttpStatus.CONFLICT);


		String name = (String)  info.get("name");
		String password = (String)  info.get("password");
		String phone = (String)  info.get("phone");

		String roleName = (String) info.get("role");
		String stateName = (String)  info.get("state");

		List<String> authProvider = (List<String>) info.get("authProvider");

		Role role = roleDao.findByRoleName(roleName);
		State state = stateDao.findByStateName(stateName);

		User user = new User(email,encoder.encode(password),name,phone,role,state);

		for(String auth : authProvider){
			AuthProvider provider = authProviderDao.findByProviderName(auth);
			user.addAuthProvider(provider);
		}

		userService.addUser(user);

		return ResponseEntity.ok("Success!");
	}

	@PutMapping("/update/user")
	public ResponseEntity<Object> updateUSer(@RequestBody Map<String,Object>  info) {

		CheckRole.isUpdate();
		String oldEmail = (String) info.get("oldEmail");
		boolean changePassword = (boolean)info.get("changePassword") ;

		String newEmail = (String) info.get("email");
		User temp = userService.findByEmail(newEmail);
		if(temp != null && !newEmail.equals(oldEmail)) throw new CatchException("This email has been used!", HttpStatus.CONFLICT);


		String name = (String)  info.get("name");
		String password = (String)  info.get("password");
		String phone = (String)  info.get("phone");

		String roleName = (String) info.get("role");
		String stateName = (String)  info.get("state");

		List<String> authProvider = (List<String>) info.get("authProvider");

		Role role = roleDao.findByRoleName(roleName);
		State state = stateDao.findByStateName(stateName);

		User user = userService.findByEmail(oldEmail);

		user.setEmail(newEmail);
		user.setName(name);
		if(changePassword) user.setPassword(encoder.encode(password));
		user.setPhone(phone);
		user.setRole(role);
		user.setState(state);

		for(String auth : authProvider){
			AuthProvider provider = authProviderDao.findByProviderName(auth);
			if(!user.getAuthProvider().contains(provider)) user.addAuthProvider(provider);
		}

		userService.addUser(user);

		return ResponseEntity.ok("Success!");
	}



}

