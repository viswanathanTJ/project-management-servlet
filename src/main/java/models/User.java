package models;

public class User {
	private String username;
	private String email;
	private String role;
	private String joined;

	public User(String username, String email, String role, String joined) {
		super();
		this.username = username;
		this.email = email;
		this.role = role;
		this.joined = joined;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getJoined() {
		return this.joined;
	}

	public void setJoined(String joined) {
		this.joined = joined;
	}

}
