
import java.util.ArrayList;
import java.util.Vector;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thanasis Manesiotis
 */
public class EntropyAlgorithm extends Algorithm {

    public EntropyAlgorithm(boolean duplicates, int pegs, int poolLength, Vector<String> code) {
	super(duplicates, pegs, poolLength, code);
    }

    @Override
    public void runGame() {
	
        if (this.guessList.isEmpty()) {
            if(this.duplicates)
                this.createSetOfSolutionsDuplicates();
            else
                this.createSetOfSolutionsNoDuplicates();
            
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
    public Vector<String> firstGuess() {
	Vector<String> fGuess = new Vector<>();
	
	fGuess.add("1");
	fGuess.add("2");
	fGuess.add("3");
	fGuess.add("4");

	return fGuess;
    }

	
    @Override
    public Vector<String> selectGuess() {
	ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();
		
	int initialPool = this.sol.size();
	double initEntropy = Double.NEGATIVE_INFINITY;	

	for (Vector<String> tmpCode : this.sol) {
            double entropy = 0;
			
            for (int[]response : this.results) {
		double I;
								
                double tmpPool = this.removeGuess(tmpCode, response);
				
		if (tmpPool == 0)
                    I = 1;
		else
                    I = (Math.log((initialPool/tmpPool)) / Math.log(2));				
				
		double prob = tmpPool/initialPool;
				
		entropy += prob*I;
            }
			
            if (entropy > initEntropy) {
                tmpSol.clear();
		initEntropy = entropy;
            }
			
            if (entropy == initEntropy)
		tmpSol.add(tmpCode);
			
	}
	
        return this.chooseOption(tmpSol);
    }

	
    /********************************************************
     * Input:	Vector<String> guess, int[]feedback         *
     * Output:	(int) tmpSol.size()                         *
     *                                                      *
     * To-Do:	Calculate the number of possible solutions, *
     *		if the guess is the code.                   *
     ********************************************************/
    private int removeGuess(Vector<String> guess, int[]feedback) {
	ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();
		
	int red = feedback[0];
	int white = feedback[1];
		
	for (Vector<String> str : this.sol) {
            int[] response = this.rule.check(guess, str);
			
            if (red == response[0] && white == response[1])
                tmpSol.add(str);			
	}

	return tmpSol.size();
    }
}
