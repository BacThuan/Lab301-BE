package com.application.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private Long id;
	

	@Email(message="Invalid email!")
	@Column(name="Email")
	private String email;

	@Column(name="Password")
	private String password;
	

	@Column(name="User_Name")
	private String name;
	

	@Column(name="Phone_Number")
	private String phone;


	@Column(name = "Last_Login")
	private Date loginDate;

	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
	@JoinColumn(name="role_id")
	private Role role;

	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH})
	@JoinColumn(name="state_id")
	private State state;

	@ManyToMany
	@JoinTable(
			name = "user_with_auth_provider",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "auth_provider_id"))
	private List<AuthProvider> authProvider;

	@OneToMany(mappedBy="user",fetch = FetchType.EAGER,
			cascade={CascadeType.REFRESH,CascadeType.DETACH, CascadeType.REMOVE})
	private List<ProductReview> reviews;

	@OneToMany(mappedBy="user",fetch = FetchType.EAGER,
			cascade={CascadeType.REFRESH,CascadeType.DETACH, CascadeType.REMOVE})
	private List<Cart> carts;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Date updatedAt;
	
	public User() {
		super();
	}


	public User(String email, String password, String name, String phone, Role role, State state) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.role = role;
		this.state = state;
	}

	// for login
	public User( String email,String password) {
		this.email = email;
		this.password = password;
	}

	// for register
	public User( String email,String password,String name,String phone) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Role getRole() {
		return role;
	}



	public void setRole(Role role) {
		this.role = role;
	}



	public State getState() {
		return state;
	}



	public void setState(State state) {
		this.state = state;
	}

	public List<AuthProvider> getAuthProvider() {
		return authProvider;
	}


	public void setAuthProvider(List<AuthProvider> authProvider) {
		this.authProvider = authProvider;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


	public List<ProductReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews;
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public void addAuthProvider(AuthProvider provider) {
		if(authProvider == null) {
			authProvider = new ArrayList<>();
		}
		authProvider.add(provider);
	}

	public void addReview(ProductReview review) {
		if(reviews == null) {
			reviews = new ArrayList<>();
		}
		reviews.add(review);
	}

	public void addCarts(Cart cart) {
		if(carts == null) {
			carts = new ArrayList<>();
		}
		carts.add(cart);
	}

}
