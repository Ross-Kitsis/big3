package big2;
import java.util.*;
import java.io.*;

public class A 
{
	private static String[] alph = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r", "s"
			,"t","u","v","w","x","y","z","#",",",".",";",":","?","!","(",")","-","\"","\'","@"," "};
	
	private static int numWord = 10000;
	
	public static void main(String[] args)
	{
		Common c = new Common();
		c.setAlph(alph);
		c.buildDictionary();
		
		String[] words = c.makeWords(numWord);
		List<String> validWords = c.findWords(words);
		
		File f = new File("a1.txt");
		if(!f.exists())
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			for(String s:words)
			{
				out.println(s);
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("----------------VALID WORDS:-------------");
		for(String s:validWords)
		{
			System.out.println(s);
		}
		System.out.println("--------------------------");

		
		System.out.println("Number of words created: " + validWords.size());
		System.out.println("% Words valid: " + validWords.size() / (double)(numWord) * 100);
		
		System.out.println("---DONE---");
	}
	
	
	public static void print(String[] toPrint)
	{
		for(int i=0; i < toPrint.length; i++)
		{
			System.out.print(toPrint[i] + "\n");
		}
		System.out.println();
	}
	
	public static void print(List<String> toPrint)
	{
		for(int i=0; i < toPrint.size(); i++)
		{
			System.out.print(toPrint.get(i) + "\n");
		}
		System.out.println();
	}
}