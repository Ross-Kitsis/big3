package big2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class H 
{
	public static void main(String[] args)
	{
		int n = 3;
		int L = 1500;
		Common c = new Common();
		String bookFileLocation = "books";
		Map<String,Profile> trainingProfiles = new LinkedHashMap<String,Profile>();
		Map<String,Profile> testingProfiles = new LinkedHashMap<String,Profile>();
		
		File f = new File(bookFileLocation);
		File[] books = f.listFiles();
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
		//DS Files removed
		
		//Build the List of files of use for training
		int mod = books.length;
		Random r = new Random(System.currentTimeMillis());
		List<File> forTraining = new ArrayList<File>();
		List<File> forTesting = new ArrayList<File>();
		int start = r.nextInt(mod); //Starting position to test
		int numToTest = (mod/3)*2; //Use 2/3 of files for training (random 2/3)
		
		for(int i = 0;i<numToTest; i++)
		{
			forTraining.add(books[(start+i)%mod]);
			//System.out.print(books[(start+i)%mod] + " ");
		}
		System.out.println();
		for(int i = 0; i<mod-numToTest;i++)
		{
			forTesting.add(books[(((start+numToTest)%mod) + i) % mod]);
			//System.out.print(books[(((start+numToTest)%mod) + i) % mod] + " ");
		}
		//Build profiles for the training files
		buildProfile(forTraining,trainingProfiles,c,n);
		
		/*
		buildProfile(forTesting,testingProfiles,c,n);
		
		for(String s: testingProfiles.keySet())
		{
			System.out.println(s);
		}*/
		
		//Adjust profile lengths
		Set<String> collectedGenre = trainingProfiles.keySet();
		for(String s: collectedGenre)
		{
			trainingProfiles.get(s).adjustProfileLength(L);
		}
		//Done adjusting profiles
		for(File test:forTesting)
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(test.getAbsolutePath()));
				String authorName = br.readLine();
				String bookTitle = br.readLine();
				String[] genres = br.readLine().split(",");
				
				
				Profile p = new Profile(authorName, new LinkedHashMap<String,Integer>());
				
				
				String current;
				while((current=br.readLine()) != null)
				{
					c.getNgrams(current, n, p.getProfile());
				}
				
				//Adjust testing profile length
				p.adjustProfileLength(L);
				
				
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public static void buildProfile(List<File> files, Map<String,Profile> profiles, 
			Common c, int n)
	{
		String authorName;
		String bookTitle;
		String[] genre;
		
		for(File f:files)
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
				authorName = br.readLine();
				bookTitle = br.readLine();
				genre = br.readLine().split(",");
				
				Profile p;
				
				//Loop through and build or add to genre of the book
				for(String g: genre)
				{
					String current;
					p = profiles.get(g);
					if(p ==  null)
					{
						p = new Profile(g,new LinkedHashMap<String,Integer>());
						profiles.put(g, p);
					}
					
					
					Map<String,Integer> ngrams = p.getProfile();
					
					BufferedReader b = new BufferedReader(new FileReader(f.getAbsolutePath()));
					while((current = br.readLine())!=null)
					{
						c.getNgrams(current, n, ngrams);
					}
					
				}
				
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public static String findAuthor(Map<String,Profile> training, Profile testing)
	{
		String genre = "";
		double dis = Double.MAX_VALUE;
		Set<String> trainGenre = training.keySet();
		
		for(String s:trainGenre)
		{
			Profile toCompare = training.get(s);
		}
		
		return genre;
	}
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
