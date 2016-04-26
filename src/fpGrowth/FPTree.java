package fpGrowth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FPTree{
	private FPNode root = new FPNode();
	public Map<String,ArrayList<FPNode>> headerTable = new HashMap<String,ArrayList<FPNode>>();
	public Map<String,Integer> headerNum = new HashMap<String,Integer>();
	
	public FPTree(){
		root.setChild(null);
		root.setNum(0);
		root.setString(null);
	}
	
	/*public void insertTree(String s,FPNode temp){
		if(temp.getChild() == null){
			FPNode newNode = new FPNode();
			newNode.setString(s);
			newNode.addNum();
			newNode.setChild(null);
			newNode.setSibling(null);
			temp.setChild(newNode);
			return;
		}
		temp = temp.getChild();
		if(temp.getItem().equals(s))
			temp.addNum();
		else{
			while(temp.getSibling() != null){
				if(temp.getItem().equals(s)){
					temp.addNum();
				}
				temp = temp.getSibling();
			}
			FPNode tempNode = new FPNode();
			tempNode.setString(s);
			tempNode.addNum();
			tempNode.setChild(null);
			tempNode.setSibling(null);
			temp.setSibling(tempNode);
		}
	}*/
	
	public FPNode getRoot(){
		return root;
	}
	
}