package big2;

import java.text.*;
import java.util.*;
import java.io.*;

public class Dv2
{
	public static Random r = new Random(System.currentTimeMillis());
	
	public static void main(String[] args)
	{
		DecimalFormat df = new DecimalFormat("#####.##");
		int numWords = 10000;
		String bookFileLocation = "edgar";
		int base = 96; //Need to add base to convert integer to character
		String[] alph = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r", "s"
			,"t","u","v","w","x","y","z","#",",",".",";",":","?","!","(",")","-","\"","\'","@"," "};
		String[][] secondOrder = new String[alph.length + 1][alph.length +1];
		String[][] thirdOrder = new String[(int)Math.pow(alph.length, 2) +1 ][alph.length + 1];
		
		double resolution = 0.001;
		
		Common c = new Common();
		c.buildDictionary();
		
		//Set top row of both arrays
		for(int i = 1; i < alph.length + 1; i++)
		{
			secondOrder[0][i] = alph[i - 1];
			thirdOrder[0][i] = alph[i - 1];
		}
		
		
		/////Build first column of values (Second)
		for(int i = 1; i < alph.length + 1; i++)
		{
			secondOrder[i][0] = alph[i - 1];
		}
		
		String[] allThirdOrderVals = new String[alph.length * alph.length];
		int arrayPos = 0;
		for(int i = 0; i < alph.length; i++)
		{
			for(int j = 0; j < alph.length; j++)
			{
				//thirdOrder[arrayPos][0] = alph[i] + alph[j];
				allThirdOrderVals[arrayPos] = alph[i] + alph[j];
				arrayPos++;
			}
		}
		
		for(int i = 1; i < thirdOrder.length; i++)
		{
			thirdOrder[i][0] = allThirdOrderVals[i-1];
		}	

		File f = new File(bookFileLocation);
		File[] books = f.listFiles();
		//Remove .DS file if on MAC
		for(int i = 0; i < books.length; i++)
		{
			if(books[i].getAbsolutePath().contains(".DS_"))
			{
				books[i] = null;
			}
		}

		List<File> bl = new ArrayList<File>();
		for(File fb:books)
		{
			if(fb!=null)
			{
				bl.add(fb);
			}
		}

		books = bl.toArray(new File[books.length-1]);
		//DS Files removed
		
		for(int i = 0; i < books.length; i++)
		{
			c.buildModel(secondOrder,2,books[i],alph);
			c.buildModel(thirdOrder,3,books[i],alph);
		}
		
		
		//Need to generate words now
		List<String> wordsSec = generateWords(secondOrder,numWords,2,c);
		List<String> wordsThird = generateWords(thirdOrder,numWords,3,c);
		
		
		System.out.println("Second order results: ");
		printResults(wordsSec,numWords,c,df);
		
		System.out.println("Third order results: ");
		printResults(wordsThird,numWords,c,df);
		
		
		
	}
	public static void printResults(List<String> words,int numWords, Common c, DecimalFormat df)
	{
		List<String> validWords = c.findWords(words.toArray(new String[0]));
		
		System.out.println(validWords);
		System.out.println("Number of words generated with duplicates: " + validWords.size() + " Percentage: " + df.format(((double)validWords.size()/numWords)*100) + "%");
		List<String> noDup = new ArrayList<String>();
		for(int i = 0; i < validWords.size(); i++)
		{
			if(!noDup.contains(validWords.get(i)))
			{
				noDup.add(validWords.get(i));
			}
		}
		System.out.println(noDup);
		System.out.println("Number of words generated with NO duplicates: " + noDup.size() + " Percentage: " + df.format(((double)noDup.size()/numWords)*100) + "%");
	}
	
	
	public static List<String> generateWords(String[][] model, int n,int order,Common c)
	{
		int numWord = 0;
		List<String> words = new ArrayList<String>();
		//Random r = new Random(System.currentTimeMillis());
		String last = model[1][0]; //Initialization string
		StringBuilder sb = new StringBuilder(last);
		String toAdd;
		
		//System.out.println("\n"+last+"\n");
		
		while(numWord < n)
		{
			toAdd = getNext(model,last,c);
			
			
			if(toAdd.equals(" "))//Word created
			{
				//System.out.println(sb.toString());
				words.add(sb.toString());
				sb.setLength(0);
				numWord++;
				//last = last.substring(last.length() -1) + toAdd;
				
			}else //No new word created
			{
				sb.append(toAdd);
				//last = last.substring(last.length() -1) + toAdd;
			}
			
			if(order > 2)
			{
				last = last.substring(last.length() -1) + toAdd;
			}else
			{
				last = toAdd;
			}
			
			
		//	System.out.println("To add:" + toAdd + " length " + toAdd.length());
		//	System.out.println("Last:" + last);
		//	Scanner s = new Scanner(System.in);
		//	s.next();
			//System.out.println(numWord);
		}
		
		return words;
	}
	//Find the next string to add to make a word
	public static String getNext(String[][] model,String last,Common c)
	{
		//Random r = new Random(System.currentTimeMillis());
		
		String toAdd = "";
		
		int row = c.getRow(last, model);
		//Find total occurances to build probabilities
		int total = 0;
		for(int i = 1; i < model[row].length;i++)
		{
			try{
			total = total + Integer.parseInt(model[row][i]);
			}catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("Row: " + row + " Col: " + i);
				Scanner in = new Scanner(System.in);
				in.next();
			}
		}
		
		//Get the next letter
		/*
		double goal = r.nextDouble(); //Iterate through pro
		double goalProgress = 0;
		for(int i = 1; i < model[row].length;i++)
		{
			goalProgress = goalProgress + Double.parseDouble(model[row][i])/total;
			if(goalProgress > goal)
			{
				toAdd = model[0][i];
				break;
			}
		}*/
		
		
		int goal = r.nextInt(total -1) + 1;
		int progress = 0;
		int k = 1;
		while(progress < goal)
		{
			progress = progress + Integer.parseInt(model[row][k]);
			if(progress > goal)
			{
				toAdd = model[0][k];
				break;
			}
			k++;
		}
		
		
		if(toAdd.length() == 0)
		{
			int maxPos = 1;
			int max = 1;
			for(int i = 1; i < model[0].length;i++)
			{
				if(Integer.parseInt(model[row][i]) > max)
				{
					max = Integer.parseInt(model[row][i]);
					maxPos=i;
				}
			}
			
			if(maxPos == 1 && max ==1) //Ngram never occured and couldnt find most probable choice, choose random
			{
				maxPos = r.nextInt(model[0].length -1) + 1;
			}
			
			toAdd = model[0][maxPos];
		}
		
		if(toAdd.length() == 0)
		{
			System.out.println("------------Length is 0---------");
			System.out.println("k: " + k);
			System.out.println("Model at [0][k] = " + model[0][k]);
		}
		
		return toAdd;
	}

}
