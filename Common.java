package big2;

import java.io.*;
import java.util.*;

public class Common 
{
	private String[] alph;	
	private final String dictLocation = "wordList.txt";
	private List<String> dictionary;
	private static String[] accepted = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r", "s"
		,"t","u","v","w","x","y","z","#",",",".",";",":","?","!","(",")","-","\"","\'","@"," "};
	
	
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
				
				toReturn.add(s);
				/*
				//System.out.println(s);
				if(!toReturn.contains(s))
				{
					toReturn.add(s);
				}*/
			}
		}
		//System.out.println(toReturn.size());
		return toReturn;
	}
	public void getNgramsBytes(Byte[] bytes, int size, Map<String,Integer>ngrams)
	{
		
	}
	
	
	//Find ngrams in the passed text with the passed size, place found ngrams into the map
	public void getNgrams(String text, int size, Map<String,Integer> ngrams)
	{
		int end;
		String toAdd;
		
		for(int start = 0; start < text.length()-size; start++)
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
			toAdd = toAdd.replaceAll("[0-9]", "#");
			
			//Replace incorrect char
			List<String> accept = Arrays.asList(accepted);
			for(int i = 0; i <toAdd.length(); i++)
			{
				Character c = toAdd.charAt(i);
				String sc = c.toString();
				if(!accept.contains(sc))
				{
					toAdd = toAdd.replace(c.charValue(),'@');
				}
			}
			
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
	
	public void buildModel(String[][] model,int order,File book,String[] accepted)
	{
		System.out.println(book.getAbsolutePath());
		try {
			String sub;
			String current;
			int stringLength;
			BufferedReader br = new BufferedReader(new FileReader(book));
			int row = 0;
			int col = 0;
			String toFind;
			String next;
			String currentVal;
			
			if(model[0][0] == null)
			{
				//Initialize values in array to 1 if needed
				for(int i = 1; i < model.length; i++)
				{
					for(int j = 1; j < model[0].length; j++)
					{
						if(model[i][j] == null)
						{
							model[i][j] = "1";
						}
					}
					model[0][0] = "Initialized";
				}
			}

			while((current = br.readLine()) != null)
			{
				//Convert to all lower case
				current = current.toLowerCase();
				
				//Replace numbers with #
				current = current.replaceAll("[0-9]", "#");
				
				
				//Replace incorrect char
				List<String> accept = Arrays.asList(accepted);
				for(int i = 0; i <current.length(); i++)
				{
					Character c = current.charAt(i);
					String sc = c.toString();
					if(!accept.contains(sc))
					{
						//System.out.println("Replacing: " + sc);
						current = current.replace(c.charValue(),'@');
					}
				}
				
				//Extract ngrams and put their frequency in the array
				stringLength = current.length();
				if(stringLength != 0)
				{
					//System.out.println(current);
					for(int i = 0; i < stringLength - order+1; i++)
					{
						sub = current.substring(i, i+order);
						
						toFind = sub.substring(0, order-1);
						next = sub.substring(order-1);
						
						
						//System.out.println(sub);
						
						
						row = getRow(toFind, model);
						col = getCol(next,model);
						
						//System.out.println(toFind + " : " + row + "    " + next + " : " + col);
						
						//Increase number of occurances of an ngram
						currentVal = model[row][col];
						if(currentVal == null)
						{
							model[row][col] = new Integer(1).toString();
						}else
						{
							model[row][col] = new Integer(Integer.parseInt(currentVal) + 1).toString();
						}
						
					}
					//Scanner inp = new Scanner(System.in);
					//inp.next();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getRow(String toFind, String[][] model)
	{
		int toReturn = 0;
		for(int i = 1; i < model.length; i++)
		{
			if(model[i][0].equals(toFind))
			{
				toReturn = i;
				break;
			}
		}
		
		if(toReturn == 0)
		{
			System.out.println("toFind:<" + toFind+">");
			//System.out.println("");
			Scanner in = new Scanner(System.in);
			in.next();
		}
		
		return toReturn;
	}
	public int getCol(String toFind, String[][] model)
	{
		int toReturn = 0;
		for(int i = 1; i < model[0].length; i++)
		{
			if(model[0][i].equals(toFind))
			{
				toReturn = i;
				break;
			}
		}
		return toReturn;
	}
	public void setResolution(String[][] model, double resolution)
	{
		int total;
		for(int i = 1; i < model.length;i++)
		{
			total = 0;
			for(int j = 1; j < model[0].length; j++)
			{
				total = total + Integer.parseInt(model[i][j]);
			}
			
			for(int j = 1; j<model.length;j++)
			{
				if(Integer.parseInt(model[i][j]) < resolution)
				{
					model[i][j] = "0";
				}
			}
		}
	}
	
}