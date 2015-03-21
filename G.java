package big2;

import java.util.*;
import java.io.*;

public class G 
{
	public static void main(String[] args)
	{
		int n = 4;
		int l = 1000;
		Common c = new Common();
		String trainingFileLocation = "training";
		String testingFileLocation = "testing";
		Map<String,Profile> profiles = new HashMap<String,Profile>();
		Map<String,Profile> testing = new HashMap<String,Profile>();
		
		File f = new File(trainingFileLocation);
		File[] books = f.listFiles();
		Profile p;
		String authorName = "";
		
		//Remove .DS file if on MAC
		for(int i = 0; i < books.length; i++)
		{
			if(books[i].getAbsolutePath().contains(".DS_"))
			{
				books[i] = null;
			}
		}
		
		List<File> bl = new ArrayList<File>();
		for(File fb:books)
		{
			if(fb!=null)
			{
				bl.add(fb);
			}
		}
		
		books = bl.toArray(new File[books.length-1]);
		
		for(File b:books)
		{
			try {
				//Collect ngrams for a single book
				BufferedReader br = new BufferedReader(new FileReader(b.getAbsolutePath()));
				authorName = br.readLine();
				
				p = profiles.get(authorName);
				
				if(p==null)
				{
					//First time the author has been seen; need to create a new profile
					p = new Profile(authorName,new HashMap<String,Integer>());
					profiles.put(authorName, p);
				}
				
				
				Map<String,Integer> pro = p.getProfile();
				String current = "";
				while((current = br.readLine()) != null)
				{
					c.getNgrams(current, n, pro);
				}

				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Ngrams collected but havn't been adjusted for length yet
		Set<String> auth = profiles.keySet();
		for(String s:auth)
		{
			profiles.get(s).adjustProfileLength(l);
		}
		
		
	}
	//Builds a new profile or adds to an existing profile
	public static void buildProfile(Profile p, int l)
	{
		
	}
	
}
