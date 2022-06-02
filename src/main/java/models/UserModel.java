package models;

import entities.*;
import java.util.*;

import org.json.JSONObject;

public class UserModel {
	public JSONObject find() {
		JSONObject temp = toJSON(new User(1, "viswa", "viswa@gmail.com", "admin"));
		return temp;
	}
	
	public List<JSONObject> findAll() {
		List<JSONObject> result = new ArrayList<JSONObject>();
		result.add(toJSON(new User(1, "viswa", "viswa@gmail.com", "admin")));
		result.add(toJSON(new User(2, "siva", "viswa@gmail.com", "admin")));
		return result;
	}
	
	public JSONObject toJSON(User user) {
		JSONObject result = new JSONObject();
		result.put("ID", user.getU_id());
		result.put("Name", user.getUsername());
		result.put("Email", user.getEmail());
		result.put("Role", user.getRole());
		return result;
	}
}
