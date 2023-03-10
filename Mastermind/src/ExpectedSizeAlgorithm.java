
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thanasis Manesiotis
 */
public class ExpectedSizeAlgorithm extends Algorithm {
	
    public ExpectedSizeAlgorithm(boolean duplicates, int pegs, int poolLength, Vector<String> code) {
	super(duplicates, pegs, poolLength, code);
    }

    
    @Override
    public void runGame() {
	if (this.guessList.isEmpty()) {
            if(this.duplicates)
                this.createSetOfSolutionsDuplicates();
            else
                this.createSetOfSolutionsNoDuplicates();
		
            this.sortSolutions();		
				
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
			
            int[] feedback = rule.check(this.code, guess);
            this.resultsList.add(feedback);
        }			
    }

    
    @Override
    public Vector<String> firstGuess() {
	Vector<String> fGuess = new Vector<>();
		
	if (this.duplicates) {
            if (this.sets.size() == 6) {
		fGuess.add("1");
		fGuess.add("1");
		fGuess.add("2");
		fGuess.add("3");
            }
            else {
                fGuess.add("1");
		fGuess.add("2");
		fGuess.add("3");
		fGuess.add("4");    
            }
        } 
	else {
            for (int i = 1; i <= this.pegs; i++)
		fGuess.add(String.valueOf(i));
	}
				
	return fGuess;
    }

    
    @Override
    public Vector<String> selectGuess() {
	Vector<String> nextGuess = new Vector<>();
	ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();
		
	int totalSize = sol.size();
	double score = Double.POSITIVE_INFINITY;
		
	for (Vector<String> tmpCode : this.sol) {
            int count = 0;
            for (int[] res : this.results) 
                for (Vector<String> tmpGuess : this.sol)
                    if (Arrays.equals(this.rule.check(tmpCode, tmpGuess), res))
                        count++;												
			
            double E = Math.pow(count, 2)/totalSize;
			
            if (E < score) {
                tmpSol.clear();
		score = E;
            }
			
            if (score == E) 
		tmpSol.add(tmpCode);
						
	}

	nextGuess = this.chooseOption(tmpSol);
		
	return nextGuess;
    }

}
