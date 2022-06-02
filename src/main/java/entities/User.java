package entities;

public class User {
	private int u_id;
	private String username;
	private String email;
	private String role;
	
	public User(int u_id, String username, String email, String role) {
		super();
		this.u_id = u_id;
		this.username = username;
		this.email = email;
		this.role = role;
	}
	
	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
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
	
}
