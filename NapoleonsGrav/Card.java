

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Card {
	
    public final static String[] Suit = {"h", "d", "c", "s"}; // Array för färger
    public final static int[] Rank = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}; // Array för nummer, och värden på kortet. 11, 12, 13 är J, Q, K.
	
    private boolean faceUp; // Kontroll om kortets framsida visas eller ej
    private Image frontImage; // Kortets framsida
    private Image backImage; // Kortets baksida
	
	private int x; // Kortets position längs x-axeln
	private int y; // Kortets position längs y-axeln
	
	// Bredden och höjden på kortet
	public static int width = 71;
	public static int height = 96;
	
	// Bredden och höjden på "fönstret" där korten kommer att läggas
	public final static int panelH = 75;
	public final static int panelW = 100;
	
	private final int rank; // Rank/värde på kortet
	private final String suit; // Kortets färg

    
	// Tilldelar variabler till själva kortet
    public Card(int rank, String suit, int x, int y) {
        this.rank = rank;
        this.suit = suit;
		this.x = x;
		this.y = y;
        this.frontImage = frontImage;
        this.backImage = backImage;
		
        this.faceUp = false; // Korten börjar med att baksidan visas från början
		

    }
	
	
	public int getX(){ 
		return x; // Returnerar x-koordinater för kortets position
	} 	
	
	
	public int getY(){ 
		return y; // Returnerar x-koordinater för kortets position
	} 
	
	
    public int getRank() {
        return rank; // Returnerar ranken/värdet på kortet
    }
    
	
	public boolean isFaceUp(){
		return faceUp; // Uppger om kortets framsida visas eller ej
	}
    

    
    public void flip() {
        faceUp = !faceUp; // Vänder på kortet
    }
	
	
	// Metod för att bestämma kortets position
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}


	// Filerna på kortets bilder läses in 
	static Image readImage(String imgFile){
		Image image;
		
		try{
			image = ImageIO.read(new File(imgFile));
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
		return image;
	}
	
	
	
	// Hämtar kortets bild på bak- eller framsida beroende på conditions
	public Image getImage() {

		if(isFaceUp()){
			return frontImage;
		} else{
			return backImage;
		}
    }
	
	
	// Set-metod för att tilldela bilderna till själva kortet
	public void setCardImg(String imgFile){
		frontImage = readImage("C:/CHANGE_ME/CardImages/cards/" + imgFile); // Ändra path till kortbilder vid behov
		backImage = readImage("C:/CHANGE_ME/CardImages/cards/b1fv.gif"); // Ändra path till kortbilden vid behov
	}
	
	
	// Metod som ritar kortet på bestämda koordinater
	public void draw(Graphics g, int pileX, int pileY){
		int xPos = pileX + (Pile.panelW - width) - 27; // Gör så att kortet sitter i linje med panelen som kortet befinner sig i
		g.drawImage(this.getImage(), xPos, pileY, null);
	}
	
}