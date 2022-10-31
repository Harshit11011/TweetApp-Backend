package com.tweetapp.dto;

public class UserDtoSearchResults {

	private String firstName;
	private String lastName;

	public UserDtoSearchResults(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
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

	@Override
	public String toString() {
		return "UserDtoSearchResults [firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
