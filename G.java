package big2;

import java.util.*;
import java.io.*;


public class G 
{
	public static void main(String[] args)
	{
		int n = 8;
		int l = 1500;
		Common c = new Common();
		String trainingFileLocation = "training";
		String testingFileLocation = "testing";
		Map<String,Profile> profiles = new LinkedHashMap<String,Profile>();
		Map<String,Profile> testing = new LinkedHashMap<String,Profile>();
		
		
		buildProfiles(trainingFileLocation, profiles,c,n);
		buildProfiles(testingFileLocation, testing,c,n);
		
		
		//Profile ngrams collected but havn't been adjusted for length yet
		Set<String> auth = profiles.keySet();
		for(String s:auth)
		{
			profiles.get(s).adjustProfileLength(l);
		}
		//Testing ngrams collected but need to adjust length now
		Set<String> testAuth = testing.keySet();
		for(String s:testAuth)
		{
			testing.get(s).adjustProfileLength(l);
		}
		
		//Attempt to test all profiles build from test folder
		Set<String> tests = testing.keySet();
		Profile toTest;
		
		String correctAuthor = "";
		String predictedAuthor = "";
		
		double numCorrect = 0;
		
		for(String s:tests)
		{
			toTest = testing.get(s);
			correctAuthor = toTest.getAuthor();
			predictedAuthor = findAuthor(profiles,toTest);
			
			System.out.println("Correct Author: " + correctAuthor);
			System.out.println("Predicted Author: "+ predictedAuthor);
			System.out.println();
			
			if(predictedAuthor.contains(correctAuthor))
			{
				numCorrect++;
			}
		}
		
		System.out.println("Num tested: " + tests.size() + " Correct " + numCorrect + " Percentage: " + numCorrect/tests.size());
		
		/*
		//Set<String> tests = profiles.keySet();
		Set<String> tests = testing.keySet();
		Iterator<String> it = tests.iterator();
		it.next();
		//it.next();
		Profile test = testing.get(it.next());
		
		String a = findAuthor(profiles,test);
		System.out.println("Correct Author: "+test.getAuthor() );
		System.out.println("Predicted Author: " + a);
		*/

	}
	public static void buildProfilesBytes(String testingFileLocation, Map<String,Profile> profiles, Common c, int n)
	{
		File f = new File(testingFileLocation);
		File[] books = f.listFiles();
		Profile p;
		String authorName = "";
		
	}
	
	public static void buildProfiles(String testingFileLocation, Map<String,Profile> profiles, Common c, int n)
	{
		File f = new File(testingFileLocation);
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
					p = new Profile(authorName,new LinkedHashMap<String,Integer>());
					profiles.put(authorName, p);
				}
				
				
				Map<String,Integer> pro = p.getProfile();
				String current = "";
				while((current = br.readLine()) != null)
				{
					c.getNgrams(current, n, pro);
				}

				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static String findAuthor(Map<String,Profile> training, Profile testing)
	{
		String probableAuthor = "";
		
		//Apply the CNG algorithm to try to minimize the dissimilarity
		double dis = Double.MAX_VALUE;
		Set<String> trainedAuth = training.keySet();
		for(String s: trainedAuth)
		{
			Profile toCompare = training.get(s);
			double d = CNG(toCompare,testing);
			if(d == dis)
			{
				//Found a equally likely match
				probableAuthor = probableAuthor + s + " ";
				dis = d;
			}else if(d < dis)
			{
				probableAuthor = s + " ";
				dis = d;
			}
		}
		
		
		return probableAuthor;
	}
	//Run CNG on 2 profiles
	private static double CNG(Profile p, Profile q)
	{
		//Need to build a map containing the union of the 2 profiles
		List<String> union = new ArrayList<String>();
		
		
		double dis = 0;
		
		Map<String,Integer> pMap = p.getProfile();
		//Set<String> ngrams = pMap.keySet();
		Map<String,Integer> qMap = q.getProfile();
		
		//Build the union (P)
		for(String s:pMap.keySet())
		{
			if(! (union.contains(s)))
			{
				union.add(s);
			}
				
		}
		//Add to the union (Q)
		for(String s:qMap.keySet())
		{
			if(! (union.contains(s)))
			{
				union.add(s);
			}
		}
		
		
		
		
		Integer nump;
		Integer numq;
		
		for(String s:union)
		{
			nump = pMap.get(s);
			numq = qMap.get(s);
			
			if(nump == null)
			{
				nump = 0;
			}else if(numq == null)
			{
				numq = 0;
			}
			
			dis = dis + Math.pow(((2 * (nump - (double) numq))/(double)(nump+numq)),2);
			
		}
		
		return dis;
	}
	
}
