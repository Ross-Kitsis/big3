package big2;

import java.io.*;
import java.util.*;

public class Common 
{
	private String[] alph;	
	private final String dictLocation = "wordList.txt";
	private List<String> dictionary;
	
	public Common()
	{
		this.dictionary = new ArrayList<String>();
	}
	/*Create a list with all words in the english dictionary*/
	public void buildDictionary()
	{
		BufferedReader br = null;
		
		try {
			String current;
			br = new BufferedReader(new FileReader(dictLocation));
			
			while( (current = br.readLine()) != null)
			{
				dictionary.add(current);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String[] makeWordsFromNgrams(int numWords)
	{
		String[] words = new String[numWords];
		StringBuilder sb = new StringBuilder();
		Random r = new Random(System.currentTimeMillis());
		
		String rand;
		int totalWords = 0;
		while(totalWords < numWords)
		{
			rand = alph[r.nextInt(alph.length)];
			sb.append(rand);
			if(rand.contains(" "))
			{
				totalWords++;
			}
		}
		
		/*
		int i = 0;
		StringTokenizer st = new StringTokenizer(sb.toString());
		while(st.hasMoreTokens())
		{
			words[i] = st.nextToken();
			i++;
		}*/
		
		words = sb.toString().split(" ");
		
		
		return words;
	}
	
	/*Create random words based on the given alphabet*/
	public String[] makeWords(int numWords)
	{
		String[] words = new String[numWords];
		StringBuilder sb = new StringBuilder();
		Random r = new Random(System.currentTimeMillis());
		String randChar;
		int totalWords = 0;
		
		
		while(totalWords < numWords)
		{
			randChar = this.alph[r.nextInt(alph.length)];
			if(randChar.equals(" "))
			{
				words[totalWords] = sb.toString();
				sb = new StringBuilder();
				totalWords++;
			}else
			{
				sb.append(randChar);
			}
		}
		
		return words;
	}
	/*Check array of random words for english words*/
	public List<String> findWords(String []genWords)
	{
		List<String> toReturn = new ArrayList<String>();
		//System.out.println(toReturn.size());
		
		for(String s: genWords)
		{
			if(s.length() > 1 && dictionary.contains(s))
			{
				//System.out.println(s);
				toReturn.add(s);
			}
		}
		//System.out.println(toReturn.size());
		return toReturn;
	}
	
	//Find ngrams in the passed text with the passed size, place found ngrams into the map
	public void getNgrams(String text, int size, Map<String,Integer> ngrams)
	{
		int end;
		String toAdd;
		
		for(int start = 0; start < text.length(); start++)
		{
			//Start inclusive
			//End exclusive
			end = start+size;
			if(end < text.length())
			{
				toAdd = text.substring(start,end).toLowerCase();
			}else
			{
				toAdd = text.substring(start, text.length()).toLowerCase();
				while(toAdd.length() < size)
				{
					toAdd = toAdd + " ";
				}	
			}
			
			//Replace numbers with #
			
			//Add to ngram map
			if(ngrams.containsKey(toAdd))
			{
				Integer i = ngrams.get(toAdd);
				ngrams.put(toAdd, i.intValue() + 1);
			}else
			{
				ngrams.put(toAdd, 1);
			}
		}
	}
	//Set the alphabete (used if need to change)
	public void setAlph(String[] alph)
	{
		this.alph = alph;
	}
	//Build profiles of the most common n-grams
	public String[] buildProfile(int length, Map<String,Integer> ngrams)
	{
		String[] profile = new String[length];
		
		
		return profile;
	}
	
	
	
	
}