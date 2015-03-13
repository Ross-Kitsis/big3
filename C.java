package big2;
import java.io.*;
import java.util.*;

public class C 
{
	public static void main(String[] args)
	{
		Map<String,Integer> ngrams = new HashMap<String,Integer>();
		int size = 2;
		int numWord = 10000;
		Common c = new Common();
		
		
		File f = new File("books");
		
		File[] files = f.listFiles();
		for(int i = 0; /*i < files.length*/i<6; i++)
		{
			//System.out.println(files[i].getAbsolutePath());
			BufferedReader br = null;
			
			try {
				String current;
				br = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
				
				while( (current = br.readLine()) != null)
				{
					c.getNgrams(current, size, ngrams);
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int numNgram = 0;
		int start = 0;
		int end = 0;
		
		//Find the number of unique ngrams in the map
		Set<String> keys = ngrams.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext())
		{
			numNgram += ngrams.get(it.next());
		}
		String[] alph = new String[numNgram];
		//Fill an array representing ngrams
		
		//Set<String> ks = ngrams.keySet();
		Iterator<String> ng  = keys.iterator();
		while(ng.hasNext())
		{
			String toFill = ng.next();
			//System.out.println(toFill);
			int num = ngrams.get(toFill);
			end = start + num;
			
			Arrays.fill(alph, start, end, toFill);
			
			start = end;
		}
		c.setAlph(alph);
		//System.out.println(alph[100000]);
		
		String[] words = c.makeWordsFromNgrams(numWord);
		List<String> validWords = c.findWords(words);
		
		System.out.println("Number of words created: " + validWords.size());
		System.out.println("% Words valid: " + validWords.size() / (double)(numWord) * 100 + "%");
		
		System.out.println("---DONE---");
		
		for(int i = 0; i < words.length; i++)
		{
			System.out.println(words[i]);
		}
		
	}
}