package big2;

import java.text.DecimalFormat;
import java.util.*;
import java.io.*;


public class I 
{
	public static void main(String[] args)
	{
		int n = 5; //ngram length
		int l = 2000; //Profile length
		Common c = new Common();
		String bookFileLocation = "books";
		Map<String,Profile> profiles = new LinkedHashMap<String,Profile>();
		DecimalFormat df = new DecimalFormat("#####.##");
		
		
		//Read files and create profiles for each author
		File f = new File(bookFileLocation);
		File[] books = f.listFiles();
		
		//Build profiles
		buildProfiles(bookFileLocation,profiles,c,n);
		
		//Adjust profile lengths
		Set<String> auth = profiles.keySet();
		for(String s:auth)
		{
			profiles.get(s).adjustProfileLength(l);
		}
		
		int matrixSize = auth.size() + 1;
		String[][] compMatrix = new String[matrixSize][matrixSize];
		
		int position = 1;
		//Fill the top row and column
		Iterator<String> it = auth.iterator();
		String a;
		while(it.hasNext())
		{
			a = it.next();
			compMatrix[position][0] = a;
			compMatrix[0][position] = a;
			position++;
		}
		
		for(int i = 1; i < compMatrix.length;i++)
		{
			String searchAuthor = compMatrix[i][0];
			Map<String,Double> similarity = getAuthorSimilarityMap(searchAuthor,profiles);
			for(int j = 1; j < compMatrix.length; j++)
			{
				String toGet = compMatrix[0][j];
				if(toGet.equals(searchAuthor))
				{
					compMatrix[i][j] = "-";
				}else
				{
					compMatrix[i][j] = df.format(similarity.get(toGet));
				}
			}
		}
		
		for(int i = 0; i < compMatrix.length; i++)
		{
			for(int j = 0; j < compMatrix.length; j++)
			{
				System.out.print(compMatrix[i][j] + " , ");
			}
			System.out.println();
		}
		
		//Run comparison between authors
		for(String s:auth)
		{
			//String similar = compareAuthor(s,profiles);
			//System.out.println("Author: " + s);
			//System.out.println("Similar to: " + similar);
			//System.out.println("");
			//////Map<String,Double> similarity = getAuthorSimilarityMap(s,profiles);
			
			/*
			System.out.println(s +"\n");
			
			Set<String> keys = similarity.keySet();
			for(String a:keys)
			{
				System.out.print(a + " , ");
			}
			System.out.println();
			for(String a:keys)
			{
				System.out.print(Double.valueOf(df.format(similarity.get(a))) + " , ");
			}
			System.out.println();
			System.out.println();*/
		}
		
	}
	public static String compareAuthor(String author, Map<String,Profile> profiles)
	{		
		String similar = "";
		
		Profile authorProfile = profiles.get(author);
		
		Set<String> allAuthors = profiles.keySet();
		Profile toCompare;
		
		double dis = Double.MAX_VALUE; //Dissimilarity between authors; want to minimize this
		double foundDis;
		
		for(String auth:allAuthors)
		{
			if(auth != author)
			{
				toCompare = profiles.get(auth);
				foundDis = CNG(authorProfile,toCompare);
				
				if(foundDis == dis)
				{
					similar = similar + auth + " ";
					dis = foundDis;
				}else if(foundDis < dis)
				{
					similar = auth + " ";
					dis = foundDis;
				}
			}
		}
		
		return similar;
	}
	
	public static Map<String,Double> getAuthorSimilarityMap(String author, Map<String,Profile> profiles)
	{
		Map<String,Double> similarityMap = new LinkedHashMap<String,Double>();
		Profile authorProfile = profiles.get(author);
		
		Set<String> allAuthors = profiles.keySet();
		Profile toCompare;
		
		double dis = Double.MAX_VALUE;
		double foundDis;
		
		for(String auth:allAuthors)
		{
			if(auth != author)
			{
				toCompare = profiles.get(auth);
				similarityMap.put(auth, CNG(authorProfile,toCompare));
			}
		}
		
		return similarityMap;
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
