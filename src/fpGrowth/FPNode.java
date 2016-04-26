package fpGrowth;

public class FPNode{
	private String item = new String();
	private int num;
	private FPNode child;
	private FPNode sibling;
	private FPNode father;
	
	public FPNode(){
		num = 0;
		child = null;
		sibling = null;
	}
	
	public FPNode getFather(){
		return father;
	}
	
	public String getItem(){
		return item;
	}
	
	public int getNum(){
		return num;
	}
	
	public FPNode getChild(){
		return child;
	}
	
	public FPNode getSibling(){
		return sibling;
	}
	
	public void setString(String s){
		item = s;
	}
	
	public void setNum(int n){
		num = n;
	}
	
	public void setChild(FPNode c){
		child = c;
	}
	
	public void setSibling(FPNode s){
		sibling = s;
	}
	
	public void setFather(FPNode f){
		father = f;
	}
	
	public void addNum(){
		num++;
	}
}