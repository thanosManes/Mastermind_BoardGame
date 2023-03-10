
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import javafx.scene.paint.Color;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thanasis
 */
public class Game {
       
    
    /********************************************************
     *                  Variables Definitions               *
     * ==================================================== *
     * numOfPegs:   The length of the code                  *
     * duplicates:  Indicate if duplicates pegs are allowed *
     * set:         Array of possible choices               *
     * code:        The final code                          *
     ********************************************************/   
    private int numOfPegs;
    private boolean duplicates;
    private ArrayList<String> set = new ArrayList<String>();
    private Vector<String> code;
    private Hashtable<String, Color> colors;
    
    public Game(){
        this.numOfPegs = 4;
        this.duplicates = false;
        this.colors = new Hashtable<String, Color>();
        this.initializeColors();
	this.setOfNumbers(6);        
        this.code = new Vector<String>();
        this.generateCode();       
    }

    public Game(int codeLength, int poolLength, boolean dup){
	this.numOfPegs = codeLength;
	this.duplicates = dup;        
        this.colors = new Hashtable<String, Color>();
        this.initializeColors();
	this.setOfNumbers(poolLength);
        this.code = new Vector<String>();
	this.generateCode();        
    }
    
    /****************************************
     * Output:  String s                    *
     *                                      *
     * To-Do:   Create the set of string.   * 
     ****************************************/
    private void setOfNumbers(int poolLength) {
        
        for (int i = 1; i <= poolLength; i++)
            this.set.add(Integer.toString(i));
 
    }
	    
	 
    /***********************************
      * To-Do: Generate the secret code *
      ***********************************/
    private void generateCode() {
        
        Random rand = new Random();
	int counter = 0;	        
	        
	int a;       
        
	if (this.duplicates) { 
            for(int i=0; i<this.numOfPegs; i++) {
                a = rand.nextInt(this.set.size());
	    	this.code.add(this.set.get(a));
	    } 
	} 
	else if (!this.duplicates) {
	    while (counter < this.numOfPegs){
	        a = rand.nextInt(this.set.size());

	        if (!this.code.contains(this.set.get(a))) {
	            this.code.add(this.set.get(a));
	            counter += 1;
	        }
	    }              
	} 
    }
       
    /*********************************************************
     * To-Do:   Initialize hashmap with all possibly colors. *
     *********************************************************/
    private void initializeColors() {
        colors.put("1", Color.rgb(255, 0, 0)); //Red=1
        colors.put("2", Color.rgb(255, 255, 0)); //Yellow=2
        colors.put("3", Color.rgb(0, 255, 0)); //Green=3
        colors.put("4", Color.rgb(0, 0, 255)); //Blue=4
        colors.put("5", Color.rgb(255, 0, 255)); //Purple=5
        colors.put("6", Color.rgb(0, 255, 255)); //Cyan=6
        colors.put("7", Color.rgb(255, 165, 0)); //Orange=7
        colors.put("8", Color.rgb(0, 0, 0)); //Black=8        
        /*colors.put("9", Color.rgb(242,173,133));
        colors.put("10", Color.rgb(114,18,32));
        colors.put("11", Color.rgb(125,147,55));
        colors.put("12", Color.rgb(146,178,230));
        colors.put("13", Color.rgb(168,111,168));
        colors.put("14", Color.rgb(133,242,173));
        colors.put("15", Color.rgb(0,186,186));
        colors.put("16", Color.rgb(49,114,86));  */            
    }
    
    /********************************
     * To-Do:   Return the code.    *
     ********************************/
    public Vector<String> getCode(){ return this.code; }
    
    
    /****************************************
     * To-Do:   Return all possible colors. *
     ****************************************/
    public Hashtable<String, Color> getColors() { return this.colors; }       
}
