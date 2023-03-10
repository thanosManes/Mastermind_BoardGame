
import java.util.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thanasis Manesiotis
 */
public class KnuthAlgorithm extends Algorithm {
    
    public KnuthAlgorithm(boolean duplicates, int pegs, int poolLength, Vector<String> code) {
        super(duplicates, pegs, poolLength, code);
    }
	
    
    @Override
    public void runGame() {
        if (this.guessList.isEmpty()) {
        
            if (this.duplicates)
                this.createSetOfSolutionsDuplicates();
            else
                this.createSetOfSolutionsNoDuplicates();

            this.sortSolutions();

            for (Vector<String> str : this.sol)
                this.unusedCodes.add(str);
            
            int[] feedback = new int[2];

            Vector<String> guess = this.firstGuess();
            this.guessList.add(guess);

            feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
        }
        else {           
            this.removeSolutions(this.guessList.get(this.guessList.size()-1), this.resultsList.get(this.resultsList.size()-1));
                        
            Vector<String> guess = this.selectGuess();
            this.guessList.add(guess);
			
            int[] feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
        }
    }           

    
    @Override
    public Vector<String> firstGuess(){
        Vector<String> fGuess = new Vector<>();
		
        if (this.duplicates) {
            fGuess.add("1");
            fGuess.add("1");
            fGuess.add("2");
            fGuess.add("2");
        }
        else {
            for (int i = 1; i <= this.pegs; i++)
                fGuess.add(String.valueOf(i));
        }
		
        this.removeCode(fGuess);
		
        return fGuess;
    }

    
    /****************************************
     * Input:   Vector<String> guess        *
     *                                      *
     * To-Do:   Remove current guess from   *
     *          the list with unused codes. *
     ****************************************/
    private void removeCode(Vector<String> guess) {
        this.unusedCodes.remove(guess);
    }

	
    @Override
    public Vector<String> selectGuess(){
	
        ArrayList<Vector<String>> tmpSol = this.minimax();
		
        Vector<String> nextGuess = this.chooseOption(tmpSol);
		
        this.removeCode(nextGuess);
		
        return nextGuess;
    }
	
    
    /************************************************
     * Output:  ArrayList<Vector<String>> tmpSol    *
     *                                              *
     * To-Do:   Apply minimax technique.            * 
     ************************************************/
    private ArrayList<Vector<String>> minimax() {
		
        ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();
        double minimax = Double.POSITIVE_INFINITY;	
		
        for (Vector<String> tmpCode : this.unusedCodes) {
            double maxScore = 0;			
			
            for (int i = 0; i < this.results.length; i++) {
                double currScore = 0;
                for (Vector<String> tmpGuess : this.sol) 
                    if (Arrays.equals(this.rule.check(tmpCode, tmpGuess), this.results[i])) 
                        currScore++;
									
                maxScore = Math.max(maxScore, currScore);
            }
			
            if (maxScore < minimax) {
                tmpSol.clear();
                minimax = maxScore;
            }
			
            if (maxScore == minimax)
                tmpSol.add(tmpCode);			
        }

        return tmpSol;
    }
	
}
