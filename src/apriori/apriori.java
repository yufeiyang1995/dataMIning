package apriori;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import dataMining.controller;

public class apriori{
	static public Map<Set<String>, Integer> oneItemset = new HashMap<Set<String>, Integer>();
	static private ArrayList<Map<Set<String>, Integer>> L = new ArrayList<Map<Set<String>, Integer>>();
	static private Set<Set<String>> C = new HashSet<Set<String>>();
	static final private double min_conf = 0.5;
	
	public static void readSet(){
		Set set = controller.transaction.entrySet();
		Iterator i1 = set.iterator();
		while(i1.hasNext()){
			Map.Entry<Integer, Set<String>> entry1=(Map.Entry<Integer, Set<String>>)i1.next();
			Set item = entry1.getValue();
			Iterator<String> i2 = item.iterator();
			while(i2.hasNext()){
				String temp = i2.next();
				insertOneItemSet(temp);
			}
		}
	}
	
	public static void insertOneItemSet(String s){
		Set set = new HashSet();
		set.add(s);
		if(oneItemset.containsKey(set)){
			Integer i = oneItemset.get(set);
			i++;
			oneItemset.put(set,i);
		}
		else{
			oneItemset.put(set,1);
		}
		//System.out.println(oneItemset);
	}
	
	public static void findFrequentOneItemSet(){
		readSet();
		Set set = oneItemset.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()){
			Map.Entry<Set<String>, Integer> entry = (Map.Entry<Set<String>, Integer>)i.next();
			if(entry.getValue() < 100){
				i.remove();
			}
		}
		//System.out.println(oneItemset);
		//System.out.println(oneItemset.size());
	}
	
	public static void Apriori(){
		findFrequentOneItemSet();
		Map s1 = oneItemset;
		System.out.println(oneItemset);
		int k = 2;
		L.add(oneItemset);
		while(true){
			if(L.get(k-2).size() == 0)break;
			aproiri_gen(k-2);
			//System.out.println(C);
			Map<Set<String>, Integer> tempL = new HashMap<Set<String>, Integer>();
			Set set1 = controller.transaction.entrySet();
			Iterator i1 = set1.iterator();
			while(i1.hasNext()){
				Map.Entry<Integer, Set<String>> entry1=(Map.Entry<Integer, Set<String>>)i1.next();
				Set item = entry1.getValue();
				Iterator<Set<String>> i2 = C.iterator();
				while(i2.hasNext()){
					Set set2 = i2.next();
					if(item.containsAll(set2)){
						
						if(tempL.containsKey(set2)){
							Integer i = tempL.get(set2);
							i++;
							tempL.put(set2, i);
						}
						else{
							tempL.put(set2, 1);
						}
					}
				}
			}
			Set set3 = tempL.entrySet();
			Iterator i3 = set3.iterator();
			while(i3.hasNext()){
				Map.Entry<Set<String>, Integer> entry = (Map.Entry<Set<String>, Integer>)i3.next();
				//System.out.println(entry.getKey());
				if(entry.getValue() < 100){
					i3.remove();
				}
			}
			//System.out.println(tempL);
			//System.out.println(tempL.size());
			L.add(tempL);
			C.removeAll(C);
			k++;
		}
		analysis(k);
	}
	
	public static void analysis(int k){
		Map<Set<String>, Integer> tempL = L.get(k-3);
		Set set = tempL.entrySet();
		Iterator i1 = set.iterator();
		while(i1.hasNext()){
			Map.Entry<Set<String>, Integer> entry = (Map.Entry<Set<String>, Integer>)i1.next();
			double totalNum = entry.getValue();
			ArrayList<String> array = new ArrayList<String>();
			array.addAll(entry.getKey());
			for(int i = 0;i < (1<<array.size());i++){
				Set<String> subSet = new HashSet<String>();
				for(int j = 0;j < array.size();j++){
					if((i & (1 << j)) != 0){
						subSet.add(array.get(j));
					}
				}
				if(subSet.size() > 0 && subSet.size() < k-2){
					double subNum = L.get(subSet.size()-1).get(subSet);
					double conf = totalNum/subNum;
					if(conf > min_conf){
						Set<String> s = new HashSet<String>();
						s.addAll(array);
						s.removeAll(subSet);
						System.out.println(subSet + "=>" + s);
					}
				}
			}
			
		}
	}
	
	public static void aproiri_gen(int k){
		Set s1 = L.get(k).entrySet();
		Iterator i1 = s1.iterator();
		while(i1.hasNext()){
			Set s2 = L.get(k).entrySet();
			Iterator i2 = s1.iterator();
			Map.Entry<Set<String>, Integer> entry1 = (Map.Entry<Set<String>, Integer>)i1.next();
			while(i2.hasNext()){
				Map.Entry<Set<String>, Integer> entry2 = (Map.Entry<Set<String>, Integer>)i2.next();
				Set<String> result = new HashSet<String>();
				result.addAll(entry1.getKey());
				result.addAll(entry2.getKey());
				//System.out.println(result);
				if(result.size() == k+2){
					if(!hasInfrequentSubset(result,k)){
						C.add(result);
					}
				}
			}
		}
	}
	
	public static boolean hasInfrequentSubset(Set<String> sub,int k){
		for(int i = 0;i < sub.size();i++){
			ArrayList<String> array = new ArrayList<String>();
			array.addAll(sub);
			array.remove(i);
			Set<String> set = new HashSet<String>();
			set.addAll(array);
			if(!L.get(k).containsKey(set)){
				return true;
			}
		}
		return false;
	}
}