package big2;

import java.util.*;

public class Gram 
{
	private String current; //Current
	private Map<String,Double> probability = new LinkedHashMap<String,Double>();
	Random r = new Random(System.currentTimeMillis());
	
	public Gram(String current)
	{
		this.current = current;
	}
	
	public String getCurrent()
	{
		return this.current;
	}
	
	//Compute probabilities using formulas from presentation
	public void buildProbabilitySet(Map<String,Integer> small, Map<String,Integer> large) throws Exception
	{
		int total = 0;
		
		Integer numCurrent = small.get(current);
		if(numCurrent == null)
		{
			//Indicates value was not in level n-1; should never get here
			throw new Exception("Value not in map; value searched is: " + current);
		}else
		{
			int num = numCurrent.intValue();
			Set<String> ngrams = large.keySet();
			
			//Check each key if it is in the form of current* and get total ngrams that match
			for(String s:ngrams)
			{
				if(s.startsWith(this.current))
				{
					//Ngram starts with the the n-1 gram
					total = total + large.get(s); //Get the total number of ngrams with the correct prefix
				}
			}
			
			int numGram = 0;
			//Check each key if it is in the form of current* and get probabilities
			for(String s:ngrams)
			{
				if(s.startsWith(this.current))
				{					
					numGram=large.get(s);
					//System.out.println(numGram/(double)total);
					probability.put(s, numGram/(double)total);
				}
			}
			
			
		}
	}
	public String getNext()
	{
		String next = null;
		Double rand = r.nextDouble();
		Set<String> keys = probability.keySet();
		double total = 0;
		
		for(String s:keys)
		{
			total +=probability.get(s);
			if(rand <= total)
			{
				next = s.substring(s.indexOf(this.current)+1, s.length());
				break;
			}
		}
		//Failsafe in case s is empty
		if(next == null)
		{
			next = " ";
		}
		
		return next;
	}
	public Map<String,Double> getProbabilitySet()
	{
		return this.probability;
	}
	
	public void changeResolution(double resolution)
	{
		List<String> toRemove = new ArrayList<String>();
		Set<String> keys = this.probability.keySet();
		double total = 0;
		//Find all ngrams which do not meet the resolution
		for(String s:keys)
		{
			double p = probability.get(s);
			if(p<resolution)
			{
				toRemove.add(s);
				total += p;
			}
		}
		
		//Remove elements
		for(String s:toRemove)
		{
			this.probability.remove(s);
		}
		
		
		//Shift probabilities to other values
		keys = this.probability.keySet();
		double inc = total/keys.size();
		double old;
		for(String s:keys)
		{
			old = probability.get(s);
			probability.put(s, old + inc);
		}
	}
}
