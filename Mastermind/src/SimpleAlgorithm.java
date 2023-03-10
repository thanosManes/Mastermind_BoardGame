
import java.util.Random;
import java.util.Vector;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thanasis Manesiotis
 */
public class SimpleAlgorithm extends Algorithm {

    public SimpleAlgorithm(boolean duplicates, int pegs, int poolLength, Vector<String> code) {
	super(duplicates, pegs, poolLength, code);
    }

    @Override
    public void runGame() {

        int[] feedback = new int[2];
        if (this.guessList.isEmpty()) {
            if(this.duplicates)
                this.createSetOfSolutionsDuplicates();
            else
                this.createSetOfSolutionsNoDuplicates();
        
            this.sortSolutions();		
            
            Vector<String> guess = this.firstGuess();
            this.guessList.add(guess);
		
            feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
        }
        else {
            this.removeSolutions(this.guessList.get(this.guessList.size()-1), this.resultsList.get(this.resultsList.size()-1));
			
            this.sortSolutions();
            Random rand = new Random();
            Vector<String> guess = this.sol.get(rand.nextInt(this.sol.size()));
            this.guessList.add(guess);
					
            feedback = rule.check(code, guess);
            this.resultsList.add(feedback);
        }
    }

    @Override
    public Vector<String> firstGuess() {
        Random random = new Random();
        return this.sol.get(random.nextInt(this.sol.size()));
    }

    @Override
    public Vector<String> selectGuess() {
        Random random = new Random();
        int a = random.nextInt(this.sol.size());
        return this.sol.get(a);
    }
}
