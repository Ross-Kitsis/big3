package big2;

import java.util.*;

public class Gram 
{
	private String Sb; //Current
	private Map<String,Double> probability;
	
	public Gram(String Sb)
	{
		this.Sb = Sb;
	}
	
	public String getCurrent()
	{
		return this.Sb;
	}
	
	//Compute probabilities using formulas from presentation
	public void buildProbability(Map<String,Integer> small, Map<String,Integer> large)
	{
		//
	}
}
