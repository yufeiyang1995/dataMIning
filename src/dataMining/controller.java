package dataMining;
import apriori.apriori;
import fpGrowth.fpGrowth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Dummy.baseMethod;

public class controller{
	static public Map<Integer,Set<String>> transaction = new HashMap<Integer,Set<String>>();
	
	public static void main(String[] Args){
		System.out.println("=====Reading The File=====");
		readFile.readFileByLines();
		System.out.println("=====Using The Apriori=====");
		long startTime = System.currentTimeMillis();
		apriori.Apriori();
		long endTime = System.currentTimeMillis();
		System.out.println("Using Time:" + (endTime - startTime) + "ms"); 
		System.out.println("=====Apriori finish=====");
		System.out.println("=====Using The FPGrowth=====");
		startTime = System.currentTimeMillis();
		fpGrowth.FpGrowth();
		endTime = System.currentTimeMillis();
		System.out.println("Using Time:" + (endTime - startTime) + "ms"); 
		System.out.println("=====FPGrowth finish=====");
		System.out.println("=====Using A dummy the baseline method=====");
		startTime = System.currentTimeMillis();
		baseMethod.BaseMethod();;
		endTime = System.currentTimeMillis();
		System.out.println("Using Time:" + (endTime - startTime) + "ms"); 
		System.out.println("=====A dummy the baseline method finish=====");
	}
}