package big2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Cv2 
{
	public static void main(String[] args)
	{
		Map<String,Integer> unigrams = new HashMap<String,Integer>();
		Map<String,Integer> bigrams = new HashMap<String,Integer>();
		Map<String,Integer> trigrams = new HashMap<String,Integer>();
		Map<String,Gram> alphS = new HashMap<String,Gram>();
		Map<String,Gram> alphT = new HashMap<String,Gram>();
		int uniSize = 1;
		int biSize = 2;
		int triSize = 3;
		int numWord = 1000;
		Common c = new Common();
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
		
		/*
		Set<String> keys = unigrams.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext())
		{
			System.out.println(it.next());
		}
		*/
		
		/*
		Gram g = new Gram("a");
		try {
			g.buildProbabilitySet(unigrams, bigrams);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, Double> m = g.getProbabilitySet();
		Set<String> s = m.keySet();
		double i = 0;
		for(String temp: s)
		{
			System.out.println(temp + " : " + m.get(temp));
			i += m.get(temp);
		}
		System.out.println(i);
		
		System.out.println(g.getNext());
		System.out.println(g.getNext());
		System.out.println(g.getNext());
		*/
		
		//Build second order probabilities 
		Set<String> uni = unigrams.keySet();
		Gram g;
		for(String s:uni)
		{
			g = new Gram(s);
			try {
				g.buildProbabilitySet(unigrams, bigrams);
				alphS.put(s, g);
				System.out.println(g.getCurrent() + " : " + g.getNext());
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
		
		/*
		for(int i = 0; i < numWord;i++)
		{
			System.out.println(words[i]);
		}*/
		List<String> validWords = c.findWords(words);
		
		System.out.println("Number of words created: " + validWords.size());
		System.out.println("% Words valid: " + validWords.size() / (double)(numWord) * 100);
		
		System.out.println("---DONE Second Order---");
		
		
		
		//############################################
		/*
		//Build third order probabilities 
		Set<String> bi = bigrams.keySet();
		for(String s:bi)
		{
			g = new Gram(s);
			try {
				g.buildProbabilitySet(bigrams, trigrams);
				alphT.put(s, g);
				System.out.println(g.getCurrent() + " : " + g.getNext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/

	}
}
