import java.util.LinkedList;
import java.io.*;

import com.thoughtworks.xstream.XStream;

public class Deck {
	
	String name;
	///// Member Variables /////
	private LinkedList<Card> cards;
	private Card currentCard;
	private int runningCardID;

	public Deck(String name){
		this.name = name;
		this.cards = new LinkedList<Card>();
		this.currentCard = null;
		this.runningCardID = 0;
	}
	
	public void init(){
		this.getCurrentCard();
	}
	
	public void addCard(Card c){
		c.setID(runningCardID);
		runningCardID += 1;
		cards.addFirst(c);
	}
	
	public void deleteCard(Card c){
		cards.remove(c);
	}
	
	public void deleteCard(int cardIndexNum){
		if(cards.get(cardIndexNum).getID() != cardIndexNum){
			for (int i =0; i <cards.size()-1;i++){
				Card candidate = cards.get(i);
				if (candidate.getID() == cardIndexNum){
					cards.remove(candidate);
				}
			}
			return;
		}
		else{
		cards.remove(cardIndexNum);
		}
	}
	
	public Card getCurrentCard(){
		currentCard = cards.peek();
		return currentCard;
	}
	
	public int getNumCards(){
		return cards.size();
	}
	
	public boolean isEmpty(){
		return this.getNumCards()==0;
	}
	
	///// Navigation Methods /////
	
	public Card moveToNextCard(){
		Card c = cards.pop();
		cards.addLast(c);
		currentCard = cards.peek();
		return currentCard;
	}
	
	public Card moveToPreviousCard(){
		currentCard = cards.pollLast();
		cards.addFirst(currentCard);
		Card p = cards.pollLast();
		cards.addLast(p);
		return currentCard;
	}
	
	///// Read and writing to XML /////
	
	public String writeToXml(){
		XStream xs = new XStream();
		return xs.toXML(this);
	}
	
	@SuppressWarnings("unchecked")
	public static Deck readFromXml(Reader xml){
		XStream xs = new XStream();
		return (Deck)xs.fromXML(xml);
	}
	

	
}
