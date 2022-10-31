package com.tweetapp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;

public class UserDto {
	
	public static final String USER_SEQUENCE_NAME="user_sequence";
	
	@Id	
	private Long id;
	@NotBlank(message="firstName should not be null or empty")
	private String firstName;
	@NotBlank(message="lastName should not be null or empty")
	private String lastName;
	@Email
	@NotBlank(message="email should not be null or empty")
	private String email;
	@NotBlank(message="userName should not be null or empty")
	private String userName;
	@NotBlank(message="Password should not be null or empty")
	private String password;
	@Pattern(regexp="^\\d{10}$", message="Invalid mobile number entered")
	private String contactNumber;
	private String roles;
	
	public UserDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", userName=" + userName + ", password=" + password + ", contactNumber=" + contactNumber + ", roles="
				+ roles + "]";
	}


}
