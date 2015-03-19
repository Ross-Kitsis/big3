package big2;

import java.util.*;
import java.io.*;

public class E 
{
	public static void main(String[] args)
	{
		int uniSize = 1;
		int biSize = 2;
		int triSize = 3;
		int numWord = 1000;
		Common c = new Common();
		Map<String,Integer> unigrams = new HashMap<String,Integer>();
		Map<String,Integer> bigrams = new HashMap<String,Integer>();
		Map<String,Integer> trigrams = new HashMap<String,Integer>();
		
		File f = new File("matrix");
		if(!f.exists())
		{
			f.mkdir();
		}
		
		File books = new File("books");
		File[] files = books.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			BufferedReader br = null;
			
			String current;
			try 
			{
				br = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
				while( (current = br.readLine()) != null)
				{
					c.getNgrams(current, uniSize, unigrams);
					c.getNgrams(current, biSize, bigrams);
					c.getNgrams(current, triSize, trigrams);
				}
			
				buildFirstOrderMatrix(unigrams,files[i].getName());
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	public static void buildFirstOrderMatrix(Map<String,Integer> unigram, String title)
	{
		File f = new File("matrix\\" + title + ".csv");
		if(!f.exists())
		{
			try {
				f.createNewFile();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String toWrite ="";
		String numGram = "";
		
		Set<String> values = unigram.keySet();
		for(String s:values)
		{
			toWrite=toWrite+s+"\t";
			numGram=numGram+unigram.get(s)+"\t";
		}
		
		PrintWriter pw;
		try {
			System.out.println(toWrite.toString());
			
			pw = new PrintWriter(f);
			pw.write(toWrite + "\n");
			pw.write(numGram + "\n");
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
