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

	public void setProfile(Map<String, Integer> profile) 
	{
		this.profile = profile;
	}
	public void adjustProfileLength(int l)
	{
		Map<String,Integer> newProfile = new HashMap<String,Integer>();
		
		
		String keyMaxGram = "";
		int numMaxGram = -1;
		Set<String> grams;
		
		
		int numSort = 0;
		while(numSort < l)
		{
			int i;
			grams = profile.keySet();
			
			for(String s:grams)
			{
				i = profile.get(s);
				if(i > numMaxGram)
				{
					numMaxGram = i;
					keyMaxGram = s;
				}
			}
			
			newProfile.put(keyMaxGram, numMaxGram);
			profile.remove(keyMaxGram);
			keyMaxGram = "";
			numMaxGram = -1;
			numSort++;
			
		}
		
		this.profile = newProfile;
		System.out.println(profile.keySet().size());
	}
}
