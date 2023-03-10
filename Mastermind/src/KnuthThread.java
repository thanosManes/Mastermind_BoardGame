
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
public class KnuthThread extends AlgorithmWithThread {
	
    public KnuthThread(boolean duplicates, int pegs, int poolLength, Vector<String> code) {
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
            
            this.groupUnusedCodes();

            int[] feedback = new int[2];

            Vector<String> guess = this.firstGuess();
            this.guessList.add(guess);

            feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
        }
        else {           
            this.removeSolutions(this.guessList.get(this.guessList.size()-1), this.resultsList.get(this.resultsList.size()-1));
            
            this.groupUnusedCodes();
            
            Vector<String> guess = this.selectGuess();
            this.guessList.add(guess);
			
            int[] feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
        }
    }         	
	    
    
    /********************************************
     * To-Do:   Create sets for multi-thread.   *
     ********************************************/
    private void groupUnusedCodes() {
		
	this.gr1.clear();
	this.gr2.clear();
	this.gr3.clear();
	this.gr4.clear();
	this.gr5.clear();
	this.gr6.clear();
	this.gr7.clear();
	this.gr8.clear();
	this.gr9.clear();
	this.gr10.clear();
	this.gr11.clear();
	this.gr12.clear();	
		
	int num = 12;
	double counter = 1;
	double total = this.unusedCodes.size();
		
	if (total <= num) {
            for (Vector<String> str : this.unusedCodes)				
                this.gr1.add(str);			
	}
        else {	
            for (Vector<String> str : this.unusedCodes) {
		if (counter <= Math.ceil(total/num)) {
                    this.gr1.add(str);
                }
		else if ((Math.ceil(total/num) < counter) && (counter <= Math.ceil(total/num)*2)) {
                    this.gr2.add(str);
		}
		else if ((Math.ceil(total/num)*2 < counter) && (counter <= Math.ceil(total/num)*3)) {
                    this.gr3.add(str);
		}
		else if ((Math.ceil(total/num)*3 < counter) && (counter <= Math.ceil(total/num)*4)) {
                    this.gr4.add(str);
		}
		else if ((Math.ceil(total/num)*4 < counter) && (counter <= Math.ceil(total/num)*5)) {
                    this.gr5.add(str);
		}
		else if ((Math.ceil(total/num)*5 < counter) && (counter <= Math.ceil(total/num)*6)) {
                    this.gr6.add(str);
		}
		else if ((Math.ceil(total/num)*6 < counter) && (counter <= Math.ceil(total/num)*7)) {
                    this.gr7.add(str);
		}
		else if ((Math.ceil(total/num)*7 < counter) && (counter <= Math.ceil(total/num)*8)) {
                    this.gr8.add(str);
		}
		else if ((Math.ceil(total/num)*8 < counter) && (counter <= Math.ceil(total/num)*9)) {
                    this.gr9.add(str);
		}
		else if ((Math.ceil(total/num)*9 < counter) && (counter <= Math.ceil(total/num)*10)) {
                    this.gr10.add(str);
		}
		else if ((Math.ceil(total/num)*10 < counter) && (counter <= Math.ceil(total/num)*11)) {
                    this.gr11.add(str);
		}
		else{
                    this.gr12.add(str);
		}					
				
                counter++;
            }
	}
	/*	
	int t = this.gr1.size();
	int len = 10;
	if (this.gr1.size() > len) {
            for (int i = len; i < t; i++) {
                this.gr1.remove(this.gr1.size()-1);
		this.gr2.remove(this.gr2.size()-1);
		this.gr3.remove(this.gr3.size()-1);
		this.gr4.remove(this.gr4.size()-1);
		this.gr5.remove(this.gr5.size()-1);
		this.gr6.remove(this.gr6.size()-1);
		this.gr7.remove(this.gr7.size()-1);
		this.gr8.remove(this.gr8.size()-1);
		this.gr9.remove(this.gr9.size()-1);
		this.gr10.remove(this.gr10.size()-1);
		this.gr11.remove(this.gr11.size()-1);
		this.gr12.remove(this.gr12.size()-1);
            }
	}
	*/	
    }
	
	    
    @Override
    public Vector<String> firstGuess(){
    	Vector<String> fGuess = new Vector<>();
		
        if (this.duplicates) {
            if (this.pegs == 4) {
                fGuess.add("1");
                fGuess.add("2");
                fGuess.add("3");
                fGuess.add("4");                   
            }
            else if (this.pegs == 5) {
            	if (this.sets.size() == 6) {
	            fGuess.add("1");
	            fGuess.add("1");
	            fGuess.add("2");
	            fGuess.add("3");	
	            fGuess.add("4");	
            	}
            	else {
	            fGuess.add("1");
	            fGuess.add("1");
	            fGuess.add("2");
	            fGuess.add("2");	
	            fGuess.add("3");	
            	}
            }
	} 
	else {
            for (int i = 1; i <= this.pegs; i++)
		fGuess.add(String.valueOf(i));
            }
		
        this.removeCode(fGuess);
			
	return fGuess;
    }


    /****************************************
     * Input:	Vector<String> guess        *
     *                                      *
     * To-Do:   Remove current guess from   *
     * 		the list with unused codes. *
     ****************************************/
    private void removeCode(Vector<String> guess) {
	this.unusedCodes.remove(guess);
        this.groupUnusedCodes();
    }
    	
	
    @Override
    public Vector<String> selectGuess(){
        
    	if (this.sol.size() == 1) {
            return this.sol.get(0);
    	}
    	
        CalculateWorstCase t1 = new CalculateWorstCase(this.gr1, this.sol, this.results, this.pegs, this.duplicates);
	t1.start();
	CalculateWorstCase t2 = new CalculateWorstCase(this.gr2, this.sol, this.results, this.pegs, this.duplicates);
	t2.start();
	CalculateWorstCase t3 = new CalculateWorstCase(this.gr3, this.sol, this.results, this.pegs, this.duplicates);
	t3.start();
	CalculateWorstCase t4 = new CalculateWorstCase(this.gr4, this.sol, this.results, this.pegs, this.duplicates);
	t4.start();
	CalculateWorstCase t5 = new CalculateWorstCase(this.gr5, this.sol, this.results, this.pegs, this.duplicates);
	t5.start();
	CalculateWorstCase t6 = new CalculateWorstCase(this.gr6, this.sol, this.results, this.pegs, this.duplicates);
	t6.start();
	CalculateWorstCase t7 = new CalculateWorstCase(this.gr7, this.sol, this.results, this.pegs, this.duplicates);
	t7.start();
	CalculateWorstCase t8 = new CalculateWorstCase(this.gr8, this.sol, this.results, this.pegs, this.duplicates);
	t8.start();
	CalculateWorstCase t9 = new CalculateWorstCase(this.gr9, this.sol, this.results, this.pegs, this.duplicates);
	t9.start();
	CalculateWorstCase t10 = new CalculateWorstCase(this.gr10, this.sol, this.results, this.pegs, this.duplicates);
	t10.start();
	CalculateWorstCase t11 = new CalculateWorstCase(this.gr11, this.sol, this.results, this.pegs, this.duplicates);
	t11.start();
	CalculateWorstCase t12 = new CalculateWorstCase(this.gr12, this.sol, this.results, this.pegs, this.duplicates);
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
        				
	double max = Double.POSITIVE_INFINITY;
	Vector<String> g = new Vector<>();
	for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i) < max) {
		max = scores.get(i);
		g.clear();
		for (String s : guesses.get(i))
                    g.add(s);
            }
	}
		
	this.removeCode(g);
		
	return g;
    }   
}

