package big2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class B 
{
	
	public static void main(String[] args)
	{	
		int numWord = 10000;
		
		//Space
		String[] space = new String[6934];
		Arrays.fill(space, " ");
		//E
		String[] e = new String[3277];
		Arrays.fill(e, "e");
		//O
		String[] o = new String[2578];
		Arrays.fill(o, "o");
		//t
		String[] t = new String[2557];
		Arrays.fill(t, "t");
		//a
		String[] a = new String[2043];
		Arrays.fill(a, "a");
		//s
		String[] s = new String[1856];
		Arrays.fill(s, "s");
		//h
		String[] h = new String[1773];
		Arrays.fill(h, "h");
		//n
		String[] n = new String[1741];
		Arrays.fill(n, "n");
		//i
		String[] i = new String[1736];
		Arrays.fill(i, "i");
		//r
		String[] r = new String[1593];
		Arrays.fill(r, "r");
		//l
		String[] l = new String[1238];
		Arrays.fill(l, "l");
		//d
		String[] d = new String[1099];
		Arrays.fill(d, "d");
		//u
		String[] u = new String[1014];
		Arrays.fill(u, "u");
		//m
		String[] m = new String[889];
		Arrays.fill(m, "m");
		//y
		String[] y = new String[783];
		Arrays.fill(y, "y");
		//w
		String[] w = new String[716];
		Arrays.fill(w, "w");
		//f
		String[] f = new String[629];
		Arrays.fill(f, "f");
		//c
		String[] c = new String[584];
		Arrays.fill(c, "c");
		//g
		String[] g = new String[478];
		Arrays.fill(g, "g");
		//p
		String[] p = new String[433];
		Arrays.fill(p, "p");
		//b
		String[] b = new String[410];
		Arrays.fill(b, "b");
		//v
		String[] v = new String[309];
		Arrays.fill(v, "v");
		//k
		String[] k = new String[255];
		Arrays.fill(k, "k");
		//.
		String[] per = new String[203];
		Arrays.fill(per, ".");
		//j
		String[] j = new String[34];
		Arrays.fill(j, "j");
		//q
		String[] q = new String[27];
		Arrays.fill(q, "q");
		//x
		String[] x = new String[21];
		Arrays.fill(x, "x");
		//z
		String[] z = new String[14];
		Arrays.fill(z, "z");
		
		//Concatenate all arrays
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(space));
		list.addAll(Arrays.asList(e));
		list.addAll(Arrays.asList(o));
		list.addAll(Arrays.asList(t));
		list.addAll(Arrays.asList(a));
		list.addAll(Arrays.asList(s));
		list.addAll(Arrays.asList(h));
		list.addAll(Arrays.asList(n));
		list.addAll(Arrays.asList(i));
		list.addAll(Arrays.asList(r));
		list.addAll(Arrays.asList(l));
		list.addAll(Arrays.asList(d));
		list.addAll(Arrays.asList(u));
		list.addAll(Arrays.asList(m));
		list.addAll(Arrays.asList(y));
		list.addAll(Arrays.asList(w));
		list.addAll(Arrays.asList(f));
		list.addAll(Arrays.asList(c));
		list.addAll(Arrays.asList(g));
		list.addAll(Arrays.asList(p));
		list.addAll(Arrays.asList(b));
		list.addAll(Arrays.asList(v));
		list.addAll(Arrays.asList(k));
		list.addAll(Arrays.asList(per));
		list.addAll(Arrays.asList(j));
		list.addAll(Arrays.asList(q));
		list.addAll(Arrays.asList(x));
		list.addAll(Arrays.asList(z));
		
		String[] al = list.toArray(new String[0]);
		 
		String[] alph = new String[al.length];
		
		for(int count = 0; count < al.length; count++)
		{
			alph[count] = al[count];
		}
		
		
		Common com = new Common();
		com.setAlph(alph);
		com.buildDictionary();
		
		String[] words = com.makeWords(numWord);
		List<String> validWords = com.findWords(words);
		
		File file = new File("1b.txt");
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			for(String st:words)
			{
				out.println(st);
			}
			out.close();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
		System.out.println("----------------VALID WORDS:-------------");
		for(String st:validWords)
		{
			System.out.println(st);
		}
		System.out.println("--------------------------");

		
		
		System.out.println("Number of words created: " + validWords.size());
		System.out.println("% Words valid: " + validWords.size() / (double)(numWord) * 100 + "%");
		
		System.out.println("---DONE---");
	}
}