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
				
				int pos = files[i].getName().indexOf('.');
				String name = files[i].getName().substring(0,pos);
				
				//buildFirstOrderMatrix(unigrams,name);
				buildSecondOrderMatrix(bigrams,name);
				
				unigrams.clear();
				bigrams.clear();
				trigrams.clear();
				
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
		char sep = File.separatorChar;
		
		String directory = "matrix"+sep+"FirstOrder"+sep;
		File dir = new File(directory);
		if(!dir.exists())
		{
			dir.mkdir();
		}
		
		
		File f = new File(directory + title + ".csv");
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
			//System.out.println(toWrite.toString());
			
			pw = new PrintWriter(f);
			pw.write(toWrite + "\n");
			pw.write(numGram + "\n");
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void buildSecondOrderMatrix(Map<String,Integer> bigrams, String title)
	{
		
		char sep = File.separatorChar;
		
		String directory = "matrix"+sep+"SecondOrder"+sep;
		File dir = new File(directory);
		
		if(!dir.exists())
		{
			dir.mkdir();
		}
		
		
		File f = new File(directory + title + ".csv");
		if(!f.exists())
		{
			try {
				f.createNewFile();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String[] accepted = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r", "s"
			,"t","u","v","w","x","y","z","#",",",".",";",":","?","!","(",")","-","\"","\'","@"," "};
		
		
		try {
			PrintWriter pw = new PrintWriter(f);
			
			String bi = "";
			Integer numBi = 0;
			String base= "\t";
			for(int i = 0; i < accepted.length; i++)
			{
				base = base + accepted[i]+"\t";
			}
			pw.write(base+"\n");
			
			String toWrite = "";
			for(int i = 0; i < accepted.length; i++)
			{
				toWrite = toWrite + accepted[i] + "\t";
				for (int j = 0; j < accepted.length;j++)
				{
					bi = accepted[i]+accepted[j];
					numBi = bigrams.get(bi);
					if(numBi == null)
					{
						numBi = 0;
					}
					toWrite = toWrite + numBi + "\t";
				}
				pw.write(toWrite + "\n");
				toWrite = "";
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
