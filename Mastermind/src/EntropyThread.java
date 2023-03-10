
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
public class EntropyThread extends AlgorithmWithThread {


    public EntropyThread(boolean duplicates, int pegs, int poolLength, Vector<String> code) {
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
            
            this.group();
            									           
            int[] feedback = new int[2];
		
            Vector<String> guess = this.firstGuess();
            this.guessList.add(guess);
		
            feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
        }
        else {           
            this.removeSolutions(this.guessList.get(this.guessList.size()-1), this.resultsList.get(this.resultsList.size()-1));

            this.group();
            
            Vector<String> guess = this.selectGuess();
            this.guessList.add(guess);
			
            int[] feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
        }	
    }
   
    
    @Override
    public Vector<String> firstGuess() {
	// TODO Auto-generated method stub
	Vector<String> fGuess = new Vector<>();

        if (this.duplicates) {
            if (this.sets.size() == 6) {
                fGuess.add("1");
                fGuess.add("1");
                fGuess.add("2");
                fGuess.add("2");
                fGuess.add("3");
            }
            else if (this.sets.size() == 7) {
                fGuess.add("1");
                fGuess.add("1");
                fGuess.add("2");
                fGuess.add("3");
                fGuess.add("4");
            }
        }
        else {
            fGuess.add("1");
            fGuess.add("2");
            fGuess.add("3");
            fGuess.add("4");
            fGuess.add("5");
        }    

	return fGuess;
    }

    
    @Override
    public Vector<String> selectGuess() {
	// TODO Auto-generated method stub
        if (this.sol.size() == 1) {
            return this.sol.get(0);
        }
    	
        CalculateEntropy t1 = new CalculateEntropy(this.gr1, this.sol, this.results, this.pegs, this.duplicates);
        t1.start();
        CalculateEntropy t2 = new CalculateEntropy(this.gr2, this.sol, this.results, this.pegs, this.duplicates);
        t2.start();
        CalculateEntropy t3 = new CalculateEntropy(this.gr3, this.sol, this.results, this.pegs, this.duplicates);
        t3.start();
        CalculateEntropy t4 = new CalculateEntropy(this.gr4, this.sol, this.results, this.pegs, this.duplicates);
        t4.start();
        CalculateEntropy t5 = new CalculateEntropy(this.gr5, this.sol, this.results, this.pegs, this.duplicates);
        t5.start();
        CalculateEntropy t6 = new CalculateEntropy(this.gr6, this.sol, this.results, this.pegs, this.duplicates);
        t6.start();
        CalculateEntropy t7 = new CalculateEntropy(this.gr7, this.sol, this.results, this.pegs, this.duplicates);
        t7.start();
        CalculateEntropy t8 = new CalculateEntropy(this.gr8, this.sol, this.results, this.pegs, this.duplicates);
        t8.start();
        CalculateEntropy t9 = new CalculateEntropy(this.gr9, this.sol, this.results, this.pegs, this.duplicates);
        t9.start();
        CalculateEntropy t10 = new CalculateEntropy(this.gr10, this.sol, this.results, this.pegs, this.duplicates);
        t10.start();
        CalculateEntropy t11 = new CalculateEntropy(this.gr11, this.sol, this.results, this.pegs, this.duplicates);
        t11.start();
        CalculateEntropy t12 = new CalculateEntropy(this.gr12, this.sol, this.results, this.pegs, this.duplicates);
        t12.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
            t9.join();
            t10.join();
            t11.join();
            t12.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        ArrayList<Double> scores = new ArrayList<>();
        ArrayList<Vector<String>> guesses = new ArrayList<>();

        scores.add(t1.getScore());
        guesses.add(t1.getGuess());
        scores.add(t2.getScore());
        guesses.add(t2.getGuess());
        scores.add(t3.getScore());
        guesses.add(t4.getGuess());
        scores.add(t4.getScore());
        guesses.add(t4.getGuess());		
        scores.add(t5.getScore());
        guesses.add(t5.getGuess());		
        scores.add(t6.getScore());
        guesses.add(t6.getGuess());
        scores.add(t7.getScore());
        guesses.add(t7.getGuess());
        scores.add(t8.getScore());
        guesses.add(t8.getGuess());
        scores.add(t9.getScore());
        guesses.add(t9.getGuess());
        scores.add(t10.getScore());
        guesses.add(t10.getGuess());		
        scores.add(t11.getScore());
        guesses.add(t11.getGuess());		
        scores.add(t12.getScore());
        guesses.add(t12.getGuess());


        double max = Double.NEGATIVE_INFINITY;
        Vector<String> g = new Vector<>();
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i) > max) {
                max = scores.get(i);
                g.clear();
                for (String s : guesses.get(i)) {
                    g.add(s);
                }
            }
        }

        return g;
    }
    
}

