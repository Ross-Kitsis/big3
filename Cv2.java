package big2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class Cv2 
{
	public static void main(String[] args)
	{
		Map<String,Integer> unigrams = new HashMap<String,Integer>();
		Map<String,Integer> bigrams = new HashMap<String,Integer>();
		Map<String,Integer> trigrams = new HashMap<String,Integer>();
		int uniSize = 1;
		int biSize = 2;
		int triSize = 3;
		int numWord = 10000;
		Common c = new Common();
		
		
		File f = new File("books");
		
		File[] files = f.listFiles();
		for(int i = 0; /*i < files.length*/i<2; i++)
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
		
		Set<String> keys = unigrams.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext())
		{
			System.out.println(it.next());
		}

	}
}
