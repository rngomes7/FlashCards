
public class Card {
	private long correctCount = 0;
	private long incorrectCount = 0;
	private String back = "";
	private String front = "";
	private String tag = "";
	private int id = -1;
	
	public Card(String back, String front, String tag){
		this.back = back;
		this.front = front;
		this.tag = tag;
	}
	
	public Card(String back, String front){
		this.back  = back;
		this.front = front;
		this.tag = "";
	}
	
	public void editBack(String backContent){
		this.back = backContent;
	}
	
	public void editFront(String frontContent){
		this.front = frontContent;
	}
	
	public void editTag(String tag){
		this.tag = tag;
	}
	
	public String[] getContent(){
		return new String[] {this.back , this.front,this.tag};
	}
	
	public String getBack(){
		return this.back;
	}
	
	public String getFront(){
		return this.front;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public int getID(){
		return this.id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	/*
	public void correctInc(){
		this.correctCount++;
	}
	
	public void incorrectInc(){
		this.incorrectCount++;
	}
	*/
}
