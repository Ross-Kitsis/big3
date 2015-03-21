package big2;

import java.util.*;

public class Profile 
{
	private String author;
	private Map<String,Integer> profile;
	
	public Profile(String author, Map<String,Integer> profile)
	{
		this.author = author;
		this.profile = profile;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Map<String, Integer> getProfile() {
		return profile;
	}

	public void setProfile(Map<String, Integer> profile) {
		this.profile = profile;
	}
	
}
