package big2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * This class will do the same function as C exception resolution of ngrams will be adjusted to remove ngrams
 * which do not occur very often.
 */
public class D 
{
	static int uniSize = 1;
	static int biSize = 2;
	static int triSize = 3;
	static int numWord = 1000;
	static Common c = new Common();
	static Map<String,Integer> unigrams = new HashMap<String,Integer>();
	static Map<String,Integer> bigrams = new HashMap<String,Integer>();
	static Map<String,Integer> trigrams = new HashMap<String,Integer>();
	static Map<String,Gram> alphS = new HashMap<String,Gram>();
	static Map<String,Gram> alphT = new HashMap<String,Gram>();
	static double resolution = 0.001;
	
	public static void main(String[] args)
	{
		
		
		c.buildDictionary();
		
		
		File f = new File("books");
		
		File[] files = f.listFiles();
		for(int i = 0; i < files.length/*i<2*/; i++)
		{
			//System.out.println(files[i].getAbsolutePath());
			BufferedReader br = null;
			
			try {
				String current;
				br = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
				
				while( (current = br.readLine()) != null)
				{
					c.getNgrams(current, uniSize, unigrams);
					c.getNgrams(current, biSize, bigrams);
					c.getNgrams(current, triSize, trigrams);
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Second Order ##############
		secondOrder();
		//Third Order ##############
		thirdOrder();
	}
	public static void secondOrder()
	{
		Set<String> uni = unigrams.keySet();
		Gram g;
		for(String s:uni)
		{
			g = new Gram(s);
			try {
				g.buildProbabilitySet(unigrams, bigrams);
				alphS.put(s, g);
				g.changeResolution(resolution);
				//System.out.println(g.getCurrent() + " : " + g.getNext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Build words Start with the first term in the alphS to initialize (second order)
		String last = alphS.keySet().iterator().next();
		String next = null;
		StringBuilder toAdd = new StringBuilder();
		Gram current = null;
		int numWordMade = 0;
		String[] words = new String[numWord];
		while(numWordMade < numWord)
		{
			try{
			current = alphS.get(last);
			next = current.getNext();
			}catch (Exception e)
			{
				//System.out.println("Last:" + last);
				
				toAdd.setLength(0);
				last = alphS.keySet().iterator().next();
				current = alphS.get(last);
				next = current.getNext();
			}
			if(next.equals(" ")) //Word made
			{
				words[numWordMade] = toAdd.toString();
				last = next;
				numWordMade++;
				toAdd.setLength(0);
			}else
			{
				toAdd.append(next);
				last = next;
			}
		}
		List<String> validWords = c.findWords(words);
		
		System.out.println("Number of words created: " + validWords.size());
		System.out.println("% Words valid: " + validWords.size() / (double)(numWord) * 100);
		
		System.out.println("---DONE Second Order---");
	}
	
	public static void thirdOrder()
	{
		Set<String> bi = bigrams.keySet();
		Gram g;
		for(String s:bi)
		{
			g = new Gram(s);
			try {
				g.buildProbabilitySet(bigrams, trigrams);
				g.changeResolution(resolution);
				alphT.put(s, g);
				//System.out.println(s + " : " + g.getCurrent() + " : " + g.getNext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		String last = alphT.keySet().iterator().next();
		String next = null;
		StringBuilder toAdd = new StringBuilder(last);
		Gram current = null;
		int numWordMade = 0;
		String[] words = new String[numWord];
		String add = null;
		while(numWordMade < numWord)
		{
			try
			{
				current = alphT.get(last);
				next = current.getNext();
				add = next.substring(next.length()-1,next.length());
				//System.out.println(current.getCurrent() + "  " + next + " : " + add);
				
				if(add.equals(" "))
				{
					words[numWordMade] = toAdd.toString();
					numWordMade++;
					last = next;
					toAdd.setLength(0);
				}else
				{
					toAdd.append(add);
					last = next;
				}
				
			}catch (Exception e)
			{
				System.out.println("Not great");
				e.getMessage();
				break;
			}
		}
		
		List<String> validWords = c.findWords(words);
		
		System.out.println("Number of words created: " + validWords.size());
		System.out.println("% Words valid: " + validWords.size() / (double)(numWord) * 100);
		
		System.out.println("---DONE Third Order---");
			
	}
}
