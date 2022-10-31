package com.tweetapp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserRegisterDto {

	@NotBlank(message = "firstName should not be null or empty")
	private String firstName;
	@NotBlank(message = "lastName should not be null or empty")
	private String lastName;
	@Email
	@NotBlank(message = "email should not be null or empty")
	private String email;
	@NotBlank(message = "userName should not be null or empty")
	private String userName;
	@NotBlank(message = "Password should not be null or empty")
	private String password;
	@Pattern(regexp = "^\\d{10}$", message = "Invalid mobile number entered")
	private String contactNumber;

	public UserRegisterDto() {
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

	@Override
	public String toString() {
		return "UserRegisterDto [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", userName="
				+ userName + ", password=" + password + ", contactNumber=" + contactNumber + "]";
	}

}
