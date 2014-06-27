import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class MainMenuOptions {
	public static final int CREATE_DECK = 1;
	public static final int LOAD_DECK = 2;
	public static final int SHOW_DECK = 1;
	public static final int EDIT_DECK = 2;
}

class EditDeckOptionsEmpty {
	public static final int ADD_CARD = 1;
	public static final int SAVE_DECK = 2;
}

class EditDeckOptions{
	public static final int ADD_CARD = 1;
	public static final int DELETE_CARD = 2;
	public static final int MOVE_TO_NEXT = 3;
	public static final int MOVE_TO_PREVIOUS = 4;
	public static final int SAVE = 5;
}


public class Main {
	final static Charset ENCODING = StandardCharsets.UTF_8;
	final static String DECK_FOLDER_PATH = "C:\\Temp\\";
	
	static Deck currentDeck;
	public static void main(String args []) throws IOException{
		Scanner mainScanner = new Scanner(System.in);
		boolean run = true;
		
		while(run){
			printMainMenu();
			int mainMenuOption = mainScanner.nextInt();
			switch (mainMenuOption){
			
			// create a new deck
			case MainMenuOptions.CREATE_DECK:
				promptCreateDeck(mainScanner);
				break;
				
			// load a deck
			case MainMenuOptions.LOAD_DECK:
				File[] deckFiles = printLoadMenu(DECK_FOLDER_PATH);
				int loadMenuOption = mainScanner.nextInt();
				loadMenuOption-=1;
				if(loadMenuOption >=0 && loadMenuOption < deckFiles.length){
					loadFile(deckFiles[loadMenuOption].getAbsolutePath());
				}
				printMenu("Show deck","Edit deck");
				loadMenuOption = mainScanner.nextInt();
				if(loadMenuOption == MainMenuOptions.SHOW_DECK){
						runDeck(mainScanner);
				}
				else if(loadMenuOption == MainMenuOptions.EDIT_DECK){
						promptEditDeck(mainScanner);
				}
				break;
				
			// any other input exits the program
			default:
				run = false;
				break;
			}
		}
	}
	
	public static void printMainMenu(){
		
		System.out.println("Welcome to Flash Cards");
		printMenu("Create New Deck",
				"Load A Deck", 
				"Exit");
	}
	
	public static void promptCreateDeck(Scanner createDeckScanner) throws IOException{
		System.out.println("Enter a name for the deck: ");
		String deckName = createDeckScanner.next();
		Deck creationDeck = new Deck(deckName);
		currentDeck = creationDeck;
		promptEditDeck(createDeckScanner);
		
	}
	
	public static void promptEditDeck(Scanner editDeckScanner) throws IOException{
		boolean runEditDeckMenu = true;
		while(runEditDeckMenu){
			
			
			// different options for an empty deck
			if(currentDeck.isEmpty()){
				printEditDeckMenu();
				int editDeckMenuOption = editDeckScanner.nextInt();
				switch(editDeckMenuOption){
				
				case EditDeckOptionsEmpty.ADD_CARD:
					promptAddCard(editDeckScanner);
					break;
					
				case EditDeckOptionsEmpty.SAVE_DECK:
					saveCurrentDeck();
				
				default:
					runEditDeckMenu = false;
					
				}
				
			}
			
			// show a card if the deck is not empty
			else{
				System.out.println("Front:");
				System.out.println(currentDeck.getCurrentCard().getFront());
				System.out.println();
				System.out.println("Back:");
				System.out.println(currentDeck.getCurrentCard().getBack());
				System.out.println();
				System.out.println("Tags:");
				System.out.println(currentDeck.getCurrentCard().getTag());
				System.out.println();
				
				printEditDeckMenu();
				int editDeckMenuOption = editDeckScanner.nextInt();
				switch(editDeckMenuOption){
				
				case EditDeckOptions.ADD_CARD:
					promptAddCard(editDeckScanner);
					break;
					
				case EditDeckOptions.DELETE_CARD:
					currentDeck.deleteCard(currentDeck.getCurrentCard());
					break;
					
				case EditDeckOptions.MOVE_TO_NEXT:
					currentDeck.moveToNextCard();
					break;
					
				case EditDeckOptions.MOVE_TO_PREVIOUS:
					currentDeck.moveToPreviousCard();
					break;
					
				case EditDeckOptions.SAVE:
					saveCurrentDeck();
					break;
				
				default:
					runEditDeckMenu = false;
				
				}
				
				
			}
		
		}
	}
	
	public static void promptAddCard(Scanner addCardScanner){
		String backString,frontString,tag;
		addCardScanner.nextLine();
		System.out.println("Type the back of the card: ");
		backString = addCardScanner.nextLine();
		System.out.println("Type the front of the card: ");
		frontString = addCardScanner.nextLine();
		System.out.println("Enter a tag (press enter for no tags): ");
		tag = addCardScanner.nextLine();
		if(tag.trim().length()==0){
			currentDeck.addCard(new Card(backString,frontString));
		}
		else{
			currentDeck.addCard(new Card(backString,frontString,tag));
		}
	}
	
	public static void saveCurrentDeck()throws IOException{
		try{
			saveFile(makeFilePath(currentDeck.name),currentDeck);
		}
		catch (IOException ioe ){
			System.out.println("Error saving file: "+ioe.toString() );
		}
	}
	
	public static void printEditDeckMenu(){
		if(currentDeck !=null && !currentDeck.isEmpty()){
			printMenu("Add A Card",
					"Delete This Card",
					"Move To Next Card",
					"Move To Previous Card",
					"Save Deck",
					"Exit");
			
		}
		else{
			printMenu("Add A Card",
					"Save Deck",
					"Exit");
			
		}
	}
	
	/**
	 * Prints a list of files in the specified folder
	 * @param 
	 * path - An absolute URL to the folder
	 * @return 
	 * An array of all the files in the folder
	 */
	public static File[] printLoadMenu(String path){
		
		System.out.println("Decks available to load: ");
		File folder = new File(DECK_FOLDER_PATH);
		File[] listOfFiles = folder.listFiles();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println(i+1+". "+ listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }

		return listOfFiles;
	}
	
	public static void runDeck(Scanner deckScan){
		
		if(currentDeck == null)
			return;
		printMenu("Show front first","Show back first");
		boolean frontFirst = true;
		int sideChoice = deckScan.nextInt();
		if(sideChoice == 2){
			frontFirst = false;
		}
		
		boolean showFront = frontFirst;
		
		
		boolean run = true;
		if(showFront){
			System.out.println(currentDeck.getCurrentCard().getFront());
		}
		else{
			System.out.println(currentDeck.getCurrentCard().getBack());
		}
		while(run){
			printDeckMenu();
			int deckOption = deckScan.nextInt();
			switch(deckOption){
			case 1:
				if(showFront){
					System.out.println(currentDeck.getCurrentCard().getBack());
					showFront = false;
				}
				else{
					System.out.println(currentDeck.getCurrentCard().getFront());
					showFront = true;
				}
				break;
			case 2: 
				currentDeck.moveToNextCard();
				showFront = frontFirst;
				if(frontFirst){
					System.out.println(currentDeck.getCurrentCard().getFront());
				}
				else{
					System.out.println(currentDeck.getCurrentCard().getBack());
				}
				break;
			case 3: 
				currentDeck.moveToPreviousCard();
				showFront = frontFirst;
				if(frontFirst){
				System.out.println(currentDeck.getCurrentCard().getFront());
				}
				else{
					System.out.println(currentDeck.getCurrentCard().getBack());
				}
				break;
			default:
				run = false;
				break;
			}
		}
		
	}
	
	public static void printMenu(String ...menuOptions){
		if(menuOptions != null){
			for(int i =0; i <menuOptions.length;i++){
				System.out.println(i+1+". "+menuOptions[i]);
			}
		}
	}
	
	public static void printDeckMenu(){
		printMenu("Flip Card", 
				"Next Card", 
				"Previous Card", 
				"Exit");
	}
	
	
	public static void loadFile(String filepath)throws IOException{
		Path path = Paths.get(filepath);
		try(BufferedReader reader = Files.newBufferedReader(path, ENCODING)){
			currentDeck = Deck.readFromXml(reader);
		}
	}
	
	public static void saveFile(String filepath,Deck deck)throws IOException{
		Path path = Paths.get(filepath);
		try(BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
			writer.write(deck.writeToXml());
		}
	}
	
	public static String makeFilePath(String filename){
		return DECK_FOLDER_PATH + filename + ".xml";
	}

}
