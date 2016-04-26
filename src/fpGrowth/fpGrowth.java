package fpGrowth;

import apriori.apriori;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import dataMining.controller;

public class fpGrowth{
	static public Map<Integer,Set<String>> filterTransaction = new HashMap<Integer,Set<String>>();
	static public Map<Integer,ArrayList<String>> sortedTransaction = new HashMap<Integer,ArrayList<String>>();
	static public ArrayList<String> sortedItemList = new ArrayList<String>();
	static public Map<String,ArrayList<FPNode>> headerTable = new HashMap<String,ArrayList<FPNode>>();
	static public Map<String,Integer> headerNum = new HashMap<String,Integer>();
	static public Map<Set<String>, Integer> FrequentSet = new HashMap<Set<String>, Integer>();
	static public FPTree tree = new FPTree();
	static final private double min_conf = 0.5;
	
	static public void FpGrowth(){
		filter();
		sort();
		createTree(tree);
		//print(tree.getRoot());
		//System.out.println("SIZE:"+tree.headerTable.get("yogurt").size());
		FPGrowth(tree,null);
		analysis();
		System.out.println(FrequentSet);
	}
	
	static public void analysis(){
		Set set = FrequentSet.entrySet();
		Iterator i1 = set.iterator();
		while(i1.hasNext()){
			Map.Entry<Set<String>, Integer> entry = (Map.Entry<Set<String>, Integer>)i1.next();
			if(entry.getKey().size() > 2){
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
					if(subSet.size() == 2){
						double subNum = FrequentSet.get(subSet);
						double conf = totalNum/subNum;
						if(conf > min_conf){
							Set<String> s = new HashSet<String>();
							s.addAll(array);
							s.removeAll(subSet);
							System.out.println(subSet + "=>" + s);
						}
					}
					if(subSet.size() == 1){
						double subNum = apriori.oneItemset.get(subSet);
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
	}
	
	static public void filter(){
		Set<String> oneItem = new HashSet<String>();
		//Set<String> set1 = new HashSet<String>();
		Set set1 = apriori.oneItemset.entrySet();
		Iterator i1 = set1.iterator();
		while(i1.hasNext()){
			Map.Entry<Set<String>, Integer> entry1=(Map.Entry<Set<String>, Integer>)i1.next();
			oneItem.addAll(entry1.getKey());
		}
		sortedItemList.addAll(oneItem);
		for(int i = 0;i < sortedItemList.size();i++){
			for(int j = i;j < sortedItemList.size();j++){
				Set<String> s1 = new HashSet<String>();
				Set<String> s2 = new HashSet<String>();
				s1.add(sortedItemList.get(i));
				s2.add(sortedItemList.get(j));
				if(apriori.oneItemset.get(s1) < apriori.oneItemset.get(s2)){
					Collections.swap(sortedItemList,i,j);
				}
			}
		}
		Set set2 = controller.transaction.entrySet();
		Iterator i2 = set2.iterator();
		while(i2.hasNext()){
			Map.Entry<Integer, Set<String>> entry2=(Map.Entry<Integer, Set<String>>)i2.next();
			Set item = entry2.getValue();
			//System.out.println(entry2.getKey() +","+ item.size());
			item.retainAll(oneItem);
			//System.out.println(entry2.getKey() +","+item.size());
			filterTransaction.put(entry2.getKey(), item);
		}
	}
	
	static public void sort(){
		Set set1 = filterTransaction.entrySet();
		Iterator i1 = set1.iterator();
		while(i1.hasNext()){
			Map.Entry<Integer, Set<String>> entry1=(Map.Entry<Integer, Set<String>>)i1.next();
			Set item = entry1.getValue();
			ArrayList<String> itemList = new ArrayList<String>();
			itemList.addAll(item);
			for(int i = 0;i < itemList.size();i++){
				for(int j = i;j < itemList.size();j++){
					int x1 = sortedItemList.indexOf(itemList.get(i));
					int x2 = sortedItemList.indexOf(itemList.get(j));
					if(x1 > x2){
						Collections.swap(itemList,i,j);
					}
				}
			}
			//System.out.println(itemList);
			sortedTransaction.put(entry1.getKey(), itemList);
		}
		//System.out.println(sortedTransaction);
		//System.out.println(sortedItemList.size());
		
	}
	
	static public void createTree(FPTree t){
		
		Set set1 = sortedTransaction.entrySet();
		Iterator i1 = set1.iterator();
		while(i1.hasNext()){
			FPNode temp = t.getRoot();
			Map.Entry<Integer, ArrayList<String>> entry1 = (Map.Entry<Integer, ArrayList<String>>)i1.next();
			ArrayList<String> item = entry1.getValue();
			//System.out.println(item);
			for(int i = 0;i < item.size();i++){
				//System.out.println(temp.getItem());
				temp = insertTree(item.get(i),t,temp);
			}
		}
	}
	
	static public FPNode insertTree(String s,FPTree t,FPNode current){
		FPNode temp = current;
		if(t.headerNum.get(s) == null){
			t.headerNum.put(s, 1);
		}
		else{
			int num = t.headerNum.get(s);
			num++;
			t.headerNum.put(s,num);
		}
		if(temp.getChild() == null){
			FPNode newNode = new FPNode();
			newNode.setString(s);
			newNode.addNum();
			newNode.setChild(null);
			newNode.setSibling(null);
			newNode.setFather(current);
			temp.setChild(newNode);
			if(t.headerTable.get(s) == null){
				ArrayList<FPNode> nodeList = new ArrayList<FPNode>();
				nodeList.add(newNode);
				t.headerTable.put(s, nodeList);
			}
			else{
				t.headerTable.get(s).add(newNode);
			}
			return newNode;
		}
		temp = temp.getChild();
		if(temp.getItem().equals(s)){
			temp.addNum();
			return temp;
		}
		else{
			while(temp.getSibling() != null){
				temp = temp.getSibling();
				if(temp.getItem().equals(s)){
					temp.addNum();
					return temp;
				}
			}
			FPNode tempNode = new FPNode();
			tempNode.setString(s);
			tempNode.addNum();
			tempNode.setChild(null);
			tempNode.setSibling(null);
			tempNode.setFather(current);
			temp.setSibling(tempNode);
			if(t.headerTable.get(s) == null){
				ArrayList<FPNode> nodeList = new ArrayList<FPNode>();
				nodeList.add(tempNode);
				t.headerTable.put(s, nodeList);
			}
			else{
				//System.out.println("1111");
				t.headerTable.get(s).add(tempNode);
			}
			//System.out.println(t.headerTable.get(s).size());
			return tempNode;
		}
	}
	
	static void print(FPNode root){
		FPNode temp = root.getChild();
		if(temp == null)
			return;
		else{	
			print(temp);
			temp = temp.getSibling();
			while(temp != null){
				//System.out.println(temp.getItem()+","+temp.getNum());
				print(temp);
				temp = temp.getSibling();
			}
		}
	}
	
	static public void FPGrowth(FPTree t,Set<String> set){
		int sign = 0;
		FPNode temp = t.getRoot();
		if(t.getRoot().getChild() == null)
			return;
		while(temp.getChild() != null){
			temp = temp.getChild();
			if(temp.getSibling() != null){
				sign = 1;
				break;
			}
		}
		if(sign == 0){
			ArrayList<String> List = new ArrayList<String>();
			FPNode temp1 = t.getRoot();
			while(temp1.getChild() != null){
				temp1 = temp1.getChild();
				List.add(temp1.getItem());
			}
			int num = temp1.getNum();
			//System.out.println(set);
			//System.out.println(List);
			for(int i = 0;i < (1<<List.size());i++){
				Set<String> subSet = new HashSet<String>();
				for(int j = 0;j < List.size();j++){
					if((i & (1 << j)) != 0){
						subSet.add(List.get(j));
					}
				}
				//System.out.println(subSet);
				if(subSet.size() > 0){
					if(set != null){
						subSet.addAll(set);
					}
					//System.out.println(subSet);
					if(subSet.size() > 1 && set.size() > 0)
						FrequentSet.put(subSet, num);
				}
			}
		}
		else{
			for(int i = sortedItemList.size()-1;i >= 0;i--){
				if(t.headerTable.containsKey(sortedItemList.get(i))){
					Set<String> Pattern = new HashSet<String>();
					if(set != null)
						Pattern.addAll(set);
					Pattern.add(sortedItemList.get(i));
					int support = t.headerNum.get(sortedItemList.get(i));
					//System.out.println(Pattern);
					if(Pattern.size() > 1)
						FrequentSet.put(Pattern, support);
					
					/*conditional mode*/
					if(support < 100)continue;
					ArrayList<FPNode> nodeList = t.headerTable.get(sortedItemList.get(i));
					Set<ArrayList<String>> nodeSet = new HashSet<ArrayList<String>>();
					Map<String,Integer> nodeMap = new HashMap<String,Integer>();
					Set<String> deleteNode = new HashSet<String>();
					Map<ArrayList<String>,Integer> fMap = new HashMap<ArrayList<String>,Integer>();
					FPTree newTree = new FPTree();
					for(int j = 0;j < nodeList.size();j++){
						FPNode temp1 = nodeList.get(j);
						ArrayList<String> List1 = new ArrayList<String>();
						int roadNum = temp1.getNum();
						while(temp1.getFather() != null){
							//System.out.println(temp.getItem());
							temp1 = temp1.getFather();
							if(temp1.getFather() == null)break;
							List1.add(temp1.getItem());
							if(!nodeMap.containsKey(temp1.getItem()))
								nodeMap.put(temp1.getItem(),roadNum);
							else{
								int num = nodeMap.get(temp1.getItem());
								num = num + roadNum;
								nodeMap.put(temp1.getItem(),num);
							}
						}
						Collections.reverse(List1);
						//System.out.println(List1);
						fMap.put(List1, roadNum);
						nodeSet.add(List1);
						//for(int k = 0;k < List1.size();k++){
							//insertTree(List1.get(k),newTree);
						//}
					}
					//System.out.println(nodeSet);
					//System.out.println(nodeMap);
					Set nodeSet1 = nodeMap.entrySet();
					Iterator i1 = nodeSet1.iterator();
					while(i1.hasNext()){
						Map.Entry<String,Integer> entry1=(Map.Entry<String,Integer>)i1.next();
						int num = entry1.getValue();
						if(num < 100)
							deleteNode.add(entry1.getKey());
					}
					//System.out.println(deleteNode);
					/*Iterator<ArrayList<String>> i2 = nodeSet.iterator();
					while(i2.hasNext()){
						FPNode n = newTree.getRoot();
						ArrayList<String> List = new ArrayList<String>();
						List = i2.next();
						for(int k = 0;k < List.size();k++){
							System.out.println(deleteNode.contains(List.get(k)));
							if(!deleteNode.contains(List.get(k)))
								n = insertTree(List.get(k),newTree,n);
						}
					}*/
					
					Set fSet = fMap.entrySet();
					Iterator i2 = fSet.iterator();
					while(i2.hasNext()){
						Map.Entry<ArrayList<String>, Integer> entry2 = (Map.Entry<ArrayList<String>, Integer>)i2.next();
						int time = entry2.getValue();
						ArrayList<String> List = new ArrayList<String>();
						List = entry2.getKey();
						for(int l = 0;l < time;l++){
							FPNode n = newTree.getRoot();
							for(int k = 0;k < List.size();k++){
								//System.out.println(deleteNode.contains(List.get(k)));
								if(!deleteNode.contains(List.get(k)))
									n = insertTree(List.get(k),newTree,n);
							}
						}
					}
					
					if(newTree.getRoot().getChild() != null){
						FPGrowth(newTree,Pattern);
					}
				}
			}
		}
	}
	
}
