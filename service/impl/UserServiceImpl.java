package com.application.backend.service.impl;

import com.application.backend.dao.*;
import com.application.backend.dao.mongdb.FavouriteListDao;
import com.application.backend.document.FavouriteList;
import com.application.backend.model.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.backend.service.UserService;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private StateDao stateDao;

	@Autowired
	private AuthProviderDao authProviderDao;
	@Autowired
	CartDao cartDao;
	@Autowired
	FavouriteListDao favouriteListDao;

	@Autowired
	OrderDao orderDao;


	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	} 

	public void login(String email) {
		User user = userDao.findByEmail(email);
		user.setLoginDate(new Date());
		userDao.save(user);
	}

	public User addUser(User userInfo) {
		return userDao.save(userInfo);
	}

	@Override
	public void activeAccount(String email) {
		State enable = stateDao.findByStateName("USER_ENABLE");
		User user = userDao.findByEmail(email);
		user.setState(enable);
		userDao.save(user);
		
	}

	@Override
	public void changePassword(String email, String newPassword) {
		User user = userDao.findByEmail(email);
		user.setPassword(newPassword);
		userDao.save(user);
	}

	@Override
	public void updateProfile(Map<String, String> data) {
		User user = userDao.findByEmail(data.get("email"));
		user.setName(data.get("name"));
		user.setPhone(data.get("phone"));
		userDao.save(user);
	}

	// -------admin
	@Override
	public long count() {
		return userDao.count();
	}

	public Map<String, Object> getListData(List<User> users, long total){
		Map<String, Object> results = new HashMap<>();

		List<Map<String, String>> userData = new ArrayList<>();


		for(User user : users){
			Map<String, String> data = new HashMap<>();

			data.put("_id",String.valueOf(user.getId()) );
			data.put("name", user.getName());
			data.put("email", user.getEmail());
			data.put("phone", user.getPhone());
			data.put("role", user.getRole().getRoleName());
			data.put("state", user.getState().getStateName());
			data.put("loginDate", user.getLoginDate() != null ? user.getLoginDate().toString() : null);

			userData.add(data);
		}
		results.put("datas", userData);
		results.put("total", total);

		return results;
	}

	@Override
	public Map<String, Object> getUsers(Integer page, Integer limit, Date datePrevious, String searching) {
		if(searching == null) searching = "";
		List<User> users = userDao.findPageAndLimit(page * limit, limit , datePrevious, searching);
		long total = userDao.totalPageAndLimit(datePrevious, searching);
		return getListData(users,total);
	}

	@Override
	public Map<String, Object> findById(Long userId) {
		User user = userDao.findById((long)userId);
		if(user == null) return getListData(new ArrayList<>(),0);

		List<User> users = new ArrayList<>(Arrays.asList(user));
		return getListData(users,1);
	}

	@Override
	public void deleteUser(String email) {
		User user = userDao.findByEmail(email);
		FavouriteList favouriteList = favouriteListDao.findByEmail(email);
		List<Order> orders = orderDao.findByUser(user.getId());

		if(favouriteList != null) favouriteListDao.deleteById(favouriteList.getId());

		for(Order order : orders){
			order.setUser(null);
			orderDao.save(order);
		}

		userDao.deleteByEmail(email);
	}

	@Override
	public void updateUser(String email, String role, String state) {
		User user = userDao.findByEmail(email);

		if(role != null) {
			Role roleUpdate = roleDao.findByRoleName(role);
			user.setRole(roleUpdate);

		}
		else {
			State stateUpdate = stateDao.findByStateName(state);
			user.setState(stateUpdate);
		}

		userDao.save(user);
	}

	@Override
	public void updateUsers(List<String> emails, String role, String state) {
		if(role != null) {
			Role roleUpdate = roleDao.findByRoleName(role);
			for(String email : emails){
				User user = userDao.findByEmail(email);
				user.setRole(roleUpdate);
				userDao.save(user);
			}

		}
		else {
			State stateUpdate = stateDao.findByStateName(state);
			for (String email : emails) {
				User user = userDao.findByEmail(email);
				user.setState(stateUpdate);
				userDao.save(user);
			}

		}
	}

	@Override
	public Map<String, Object> getUser(String email) {
		User user = userDao.findByEmail(email);

		Map<String, Object> data = new HashMap<>();

		data.put("email", user.getEmail());
		data.put("name", user.getName());
		data.put("password", user.getPassword());
		data.put("phone", user.getPhone());

		data.put("role", user.getRole().getRoleName());
		data.put("state", user.getState().getStateName());

		List<String> auth = new ArrayList<>();
		for(AuthProvider authPro: user.getAuthProvider()){
			auth.add(authPro.getProviderName());
		}
		data.put("authProvider", auth);

		return data;
	}
}
