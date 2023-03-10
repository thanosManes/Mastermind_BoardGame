
import java.util.Vector;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thanasis
 */
public class Rules {
    /*********************************************************
     *               Variables Definitions                   *
     * ====================================================  *
     * numOfPegs: The length of the code                     *
     * duplicates: Indicate if duplicates pegs are allowed   *
     *********************************************************/
    private int numOfPegs;
    private boolean duplicates;
	
    public Rules(int numOfPegs, boolean duplicates) {
	this.numOfPegs = numOfPegs;
	this.duplicates = duplicates;
    }

    /****************************************************************************
     * Input:   Vector<String> code, Vector<String> guess, boolean duplicates   *
     * Output:  int[] feedback                                                  *
     *                                                                          *
     * To-Do:   Get feedback. For peg with same color and                       *
     *          same position is red feedback. For peg with                     *
     *          same color and different position is white                      *
     * 		feedback. First place for reds and second                       *
     * 		for whites                                                      *
     ****************************************************************************/
    public int[] check(Vector<String> code, Vector<String> guess){
        
        int red = 0;
        int white = 0;
        
        if (!this.duplicates) {
            for (int i=0; i<guess.size(); i++){
                if (code.contains(guess.get(i)) && !code.get(i).equals(guess.get(i))){
                    white += 1;
                }
                else if(code.get(i).equals(guess.get(i))){
                    red += 1;
                }
            }
        }
        else if (this.duplicates) {
            Vector<String> tmpGuess = new Vector<>();
            Vector<String> tmpCode = new Vector<>();
            
            for (String peg : guess)
            	tmpGuess.add(peg);
            
            for (String peg : code)
            	tmpCode.add(peg);

            for (int i=0; i<tmpGuess.size(); i++) {
                if(tmpCode.get(i).equals(tmpGuess.get(i))){
                    red += 1;
                    tmpGuess.set(i, "0");
                    tmpCode.set(i, "-1");
                }
            }
            
            for (int i=0; i<tmpGuess.size(); i++) {
                if(tmpCode.contains(tmpGuess.get(i))){
                    white += 1;
                    tmpCode.set(tmpCode.indexOf(tmpGuess.get(i)), "-1");
                    tmpGuess.set(i, "0");
                }
            }      
            
        }
        
        int[] feedback = {red,white};
        return feedback;
    }   
    
    
    /************************************************************
     * Input:   int regPegs 					*
     * 								*
     * To-Do:   Checks if game has ended. If red signs are 	*
     * 		equal with the number of pegs, then the game    *
     * 		has ended. 					*
     ************************************************************/
    public boolean endGame(int redPegs) {
    	return redPegs == this.numOfPegs;
    }
}
