
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thanasis Manesiotis
 */
public class SerialWorstCase {

    
    /************************************************************************
     *                      Variables Definitions                           *
     * ==================================================================== *
     * duplicates:	Indicate if duplicates pegs are allowed.            *
     * pegs: 		The length of the code.                             *
     * rule:		I need check function from class 'Rules'.           *
     * code:		The secret code.                                    *
     * sol: 		The models of every possible solution.              *
     * sets: 		The set of possible solutions for every peg.        *	 
     * results:		A set with all possible results(red & white).       *
     * results_4: 	Results for 4-peg code.                             *
     * history: 	Contains the number of red pegs(1st),               *
     *                  the number of white pegs(2nd)                       *
     *                  and the total(red+white)(3rds).                     *
     * possibleColors:  A list with all possible colors for each position.  *
     * guessList:	All used guesses.                                   *	
     * resultsList:	All feedbacks of all guesses.                       *
     ************************************************************************/
    private final boolean duplicates;
    private final int pegs;
    private final Rules rule;
    private final Vector<String> code;
    private ArrayList<Vector<String>> sol;
    private final ArrayList<String> sets;
    private final int[][] results;
    private final int[][] results_4 = {	{0,0},{0,1},{0,2},{0,3},{0,4},{1,0},{1,1},{1,2},{1,3},
					{2,0},{2,1},{2,2},{3,0},{4,0}	}; 
    private final int[][] history = {{0,0,0},{0,0,0}}; 
    private final ArrayList<Vector<String>> possibleColors;
    private final ArrayList<Vector<String>> guessList;
    private final ArrayList<int[]> resultsList;
    private int index;

	
    public SerialWorstCase(boolean duplicates, int pegs, int poolLength, Vector<String> code) {
	this.duplicates = duplicates;
	this.pegs = pegs;
	this.rule = new Rules(this.pegs, this.duplicates);
	this.code = code;
	this.sol = new ArrayList<>();
	this.sets = this.createSet(poolLength);
	this.possibleColors = this.initializeColorsPerPeg();
	this.guessList = new ArrayList<>();
	this.resultsList = new ArrayList<>();
	this.results = this.createResults();
    }
	
    
    /************************************************************
     * To-Do:   The basic function to run the algorithm. 	*
     * 		The whole algorithm runs through this function.	*
     ************************************************************/
    public void runGame() {		
	
	int[] feedback = new int[2];
        
        if (this.guessList.isEmpty()) {
            this.sol.add(this.initialModel());		
				
            Vector<String> guess = this.firstGuess();		
            this.guessList.add(guess);    
            
            feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
				
            this.setHistory(true, feedback);
            
            if(this.duplicates)
                this.index = 2;
            else 
                this.index = this.pegs;
            
            if (this.duplicates)
                this.updateSolutionsDuplicates(guess, feedback);
            else
                this.updateSolutionsNoDuplicates(guess, feedback);
            
            this.removeSolutions(guess);
            this.sol =  this.sortSolutions(this.sol);						
            this.removeColors(guess, feedback);
        }
        else {
            
            Vector<String> guess = new Vector<>();
            guess = this.selectGuess(this.resultsList.get(this.resultsList.size()-1), this.index);
            this.guessList.add(guess);  
            System.out.println(guess);
            
            this.setHistory(false, feedback);
            feedback = this.rule.check(this.code, guess);
            this.resultsList.add(feedback);
            this.setHistory(true, feedback);
            
            if(this.duplicates) {
            	if(this.sol.get(0).contains("x")) 
                    this.updateSolutionsDuplicates(guess, feedback);
            }
            else {
            	if(this.sol.get(0).contains("x")) 
                    this.updateSolutionsNoDuplicates(guess, feedback);
            }
            
            this.removeSolutions(guess);
            this.sol = this.sortSolutions(this.sol);
            
            this.removeColors(guess, feedback);
            
            if (this.duplicates) {
		if (this.index <= this.sets.size()-2) 
                    this.index += 2;				
            }
            else {
                Vector<String> tmpGuess = new Vector<>();
                for (String color : guess)
                    tmpGuess.add(color);
                
                Collections.sort(tmpGuess);
		String lastColor = tmpGuess.get(tmpGuess.size()-1);
				
		if (this.index < this.sets.size()-1 ) {
                    for (int i = 0; i < this.sets.size(); i++) {
			if (this.sets.get(i).equals(lastColor)) {
                            this.index = i;					
                            break;
			}
                    }
		}
				
		if (this.index == this.sets.size()-1) {
                    ArrayList<Vector<String>> tmpSol = new ArrayList<>();
					
                    for (Vector<String> model : this.sol)
			tmpSol.add(model);
					
                    for (Vector<String> model : tmpSol)
                        if(model.contains("x"))
                            this.sol.remove(model);
		}
            }
        }						
    }		
		   
	
    /************************************************
     * Input:   int poolLength                      *
     * Output:	ArrayList<String> res               *
     *                                              *
     * To-Do: 	Create possible choices of pegs,    *
     * 		based on the number of pegs.        *
     ************************************************/
    private ArrayList<String> createSet(int poolLength) {
	ArrayList<String> res = new ArrayList<>();

        for (int i = 1; i <= poolLength; i++) {
            if (i < 10) {
                res.add(String.valueOf(i));
            }
            else if (i == 10) {
                res.add("A");
            }
            else if (i == 11) {
                res.add("B");
            }            
            else if (i == 12) {
                res.add("C");
            } 
        }
        
        return res;
    }
	
	
    /********************************************************
     * Output:	ArrayList<Vector<String>> tmpColors         *
     *                                                      *
     * To-Do:	Initialize possible colors for each peg.    *
     ********************************************************/
    private ArrayList<Vector<String>> initializeColorsPerPeg() {
	ArrayList<Vector<String>> tmpColors = new ArrayList<>();
		
	for (int i = 0; i < this.pegs; i++) {
            Vector<String> a = new Vector<>();
            for (int j = 0; j < this.sets.size(); j++)
		a.add(this.sets.get(j));
			
            tmpColors.add(a);
        }
		
	return tmpColors;
    }
	
	
    /****************************************************
     * Output:	String[] res	 			*
     * 							*
     * To-Do: 	Create possible all possible results,   *
     * 		based on the number of pegs.		*
     ****************************************************/
    private int[][] createResults() {
	int[][] res;
	
        res = switch (this.pegs) {
            case 4 -> this.results_4;
            default -> null;
        };
		
	return res;
    }
		
	
    /************************************************************
     * Output:	Vector<String> guess				*
     * 								*
     * To-DO:	Returns the initial model. At the beginning, 	*
     * 		all solutions are possible, so the model is	*
     * 		of the form "x x x x". The symbol "x", means	* 
     * 		that every color is possible.			*
     ************************************************************/
    private Vector<String> initialModel(){
	Vector<String> guess = new Vector<>();
		
	for (int i = 0; i < this.pegs; i++)
            guess.add("x");
				
	return guess;
    }
	
	
    /****************************************************
     * Output:	Vector<String> fGuess			*
     * 							*
     * To-Do:	Create the first guess, depending on	* 
     *          whether duplicates are allowed. 	*
     ****************************************************/
    private Vector<String> firstGuess(){
	Vector<String> fGuess = new Vector<>();
		
	if (this.duplicates) {
            int a = this.pegs - (this.pegs/2);
            int b = this.pegs-a; 
		for (int i = 0; i < a; i++)
                    fGuess.add("1");
		for (int i = 0; i < b; i++)
                    fGuess.add("2");
	} 
	else
            for (int i = 1; i <= this.pegs; i++)
		fGuess.add(String.valueOf(i));
		
	return fGuess;			
    }
	
	
    /********************************************
     * Input:	boolean current, int[] feedback *
     * 						*
     * To-Do:	Update history array.		*
     ********************************************/
    private void setHistory(boolean current, int[] feedback) {
		
    	if (current) {
            for (int i = 0; i < feedback.length; i++) 
		this.history[1][i] = feedback[i];
			
            this.history[1][2] = feedback[0]+feedback[1];
	}
	else {
            for (int i = 0; i < history[1].length; i++) 
		this.history[0][i] = this.history[1][i];			
	}	
        
    }

	
    /****************************************************
     * Input:	Vector<String> tmpGuess, int[] feedback *
     *                                                  *
     * To-Do:	Find all possible models of solutions.  *
     ****************************************************/
    private void updateSolutionsDuplicates(Vector<String> tmpGuess, int[] feedback) {
	
        ArrayList<Vector<String>> tmpSol = new ArrayList<>();
		
	int red = feedback[0];
	int white = feedback[1];
		
	while (red > 0) {
            this.checkForEmpty(tmpSol);
				
            tmpSol.clear();				
            for (Vector<String> guess : this.sol)
		tmpSol.add(guess);
				
            this.sol.clear();
				
            for (Vector<String> guess : tmpSol) {
		int[] results = this.rule.check(guess, tmpGuess);
		int tmpRed = results[0];				
					
		if (tmpRed == feedback[0]) {
                    Vector<String> tmp = new Vector<>();
                    for(String a : guess)
                        tmp.add(a);
						
                    this.sol.add(tmp);
		}					
		else if (tmpRed < feedback[0]) {
                    for (String color : tmpGuess) {
                        for (int i = 0; i < guess.size(); i++) {
                            if(guess.get(i).equals("x")) {
                                guess.set(i, color);							
								
				Vector<String> tmp = new Vector<>();
				for(String a : guess)
                                    tmp.add(a);
									
				if (tmpGuess.get(i).equals(tmp.get(i))) 
                                    if (!this.sol.contains(tmp)) 
					this.sol.add(tmp);																			
									
				guess.set(i, "x");
                            }
			}
                    }
		}
            }
            red--;
	}		

	this.checkForEmpty(tmpSol);
			
	while (white > 0) {
            this.checkForEmpty(tmpSol);
				
            tmpSol.clear();				
            for (Vector<String> guess : this.sol)
                tmpSol.add(guess);
				
            this.sol.clear();
				
            for (Vector<String> guess : tmpSol) {
                int[] results = this.rule.check(guess, tmpGuess);
		int tmpWhite = results[1];			
					
		if (tmpWhite == feedback[1]) {
                    Vector<String> tmp = new Vector<>();
                    for(String a : guess)
			tmp.add(a);
						
                    this.sol.add(tmp);
		}					
		else if (tmpWhite < feedback[1]) {
                    for (String color : tmpGuess) {
			for (int i = 0; i < guess.size(); i++) {
                            if(guess.get(i).equals("x")) {
                                guess.set(i, color);							
								
				Vector<String> tmp = new Vector<>();
				for(String a : guess)
                                    tmp.add(a);
									
				if (!tmpGuess.get(i).equals(tmp.get(i))) 
                                    if (!this.sol.contains(tmp)) 
                                        this.sol.add(tmp);
																												
				guess.set(i, "x");
                            }
                        }
                    }
		}
            }
            white--;
	}
			
	this.checkForEmpty(tmpSol);	
    
    }
	
	
    /****************************************************
     * Input:	Vector<String> tmpGuess, int[] feedback *
     * 							*
     * To-Do:   Find all possible models of solutions   * 
     * 		for non duplicate game.			*
     ****************************************************/
    private void updateSolutionsNoDuplicates(Vector<String> tmpGuess, int[] feedback) {
	
        ArrayList<Vector<String>> tmpSol = new ArrayList<>();
	for(Vector<String> guess : this.sol)
            tmpSol.add(guess);
								
	int red = feedback[0];
	int white = feedback[1];
						
	while (red > 0) {
            this.checkForEmpty(tmpSol);
			
            tmpSol.clear();
            for(Vector<String> str : this.sol)
		tmpSol.add(str);
				
            this.sol.clear();
				
            for (Vector<String> guess : tmpSol) {
		int[] results = this.rule.check(guess, tmpGuess);
		int tmpRed = results[0];
				
		if (tmpRed == feedback[0]) {
                    Vector<String> tmp = new Vector<>();
                    for(String a : guess)
                        tmp.add(a);
					
                    this.sol.add(tmp);
		}
		else if (tmpRed < feedback[0]) {									
                    for (String color : tmpGuess) {
                        for (int i = 0; i < guess.size(); i++) {
                            if(!guess.contains(color)) {
                                if(guess.get(i).equals("x")) {
                                    guess.set(i, color);							
								
                                    Vector<String> tmp = new Vector<>();
                                    for(String a : guess)
                                        tmp.add(a);
									
                                    if (tmpGuess.get(i).equals(tmp.get(i))) 
                                        if (!this.sol.contains(tmp)) 
                                            this.sol.add(tmp);																			
									
                                    guess.set(i, "x");
				}	
                            }
			}
                    }
		}
            }
            red--;
	}
					
	this.checkForEmpty(tmpSol);
			
	while (white > 0) {
            tmpSol.clear();
            for(Vector<String> guess : this.sol)
		tmpSol.add(guess);
				
            this.sol.clear();
				
            for (Vector<String> guess : tmpSol) {
            	int[] results = this.rule.check(guess, tmpGuess);
		int tmpWhite = results[1];																			
					
		if (tmpWhite == feedback[1]) {
                    Vector<String> tmp = new Vector<>();
                    for(String a : guess)
			tmp.add(a);
						
                    this.sol.add(tmp);
		}
		else if (tmpWhite < feedback[1]) {
                    for (String color : tmpGuess) {
                        for (int i = 0; i < guess.size(); i++) {
                            if(!guess.contains(color)) {
                                if(guess.get(i).equals("x")) {
                                    guess.set(i, color);							
									
                                    Vector<String> tmp = new Vector<>();
                                    for(String a : guess)
                                        tmp.add(a);

                                    if (!tmpGuess.get(i).equals(tmp.get(i))) 
                                        if (!this.sol.contains(tmp)) 
                                            this.sol.add(tmp);

                                    guess.set(i, "x");
                                }
                            }
                        }
                    }
		}										
            }
            white--;
	}
	
        this.remSolutions();		
	this.checkForEmpty(tmpSol);	

    }
	
	
    /****************************************************
     * Input:	ArrayList<Vector<String>> tmpSol	*
     * 							*
     * To-Do:	Checks if the list with the possible	*
     * 		solutions is empty. If yes, then	*
     * 		fill this properly.			*
     ****************************************************/
    private void checkForEmpty(ArrayList<Vector<String>> tmpSol) {
    	if (this.sol.isEmpty()) 
            for(Vector<String> str : tmpSol)
		this.sol.add(str);
    }
	
	
    /********************************************
     * Input:   Vector<String> tmpGuess         *
     * 						*
     * To-Do:	We keep only the solutions with * 
     *          the same feedback, if the code 	*
     * 		was the current guess.		*
     ********************************************/
    private void removeSolutions(Vector<String> tmpGuess) {
		
	ArrayList<Vector<String>> tmpSol = new ArrayList<>();
	for(Vector<String> str : this.sol)
            tmpSol.add(str);
			
	for (Vector<String> str : tmpSol) { 
            int[] ch = this.rule.check(tmpGuess, str);
			
            if ((ch[0] != this.history[1][0]) || !(ch[1] == this.history[1][1]))
		this.sol.remove(str);					
	}
		
	this.sol.remove(tmpGuess);
    }
	
	
    /********************************************************
     * Input: 	ArrayList<Vector<String>> possibleSolutions *
     * Output:	ArrayList<Vector<String>> possibleSolutions *
     *                                                      *
     * To-Do:	Sort all possible models.                   *
     ********************************************************/
    private ArrayList<Vector<String>> sortSolutions(ArrayList<Vector<String>> possibleSolutions) {

	Vector<String> tmpSol = new Vector<>();
	for(Vector<String> str : possibleSolutions) {
            String tmpStr = "";
            for(String s : str) 
                tmpStr += s;
			
            tmpSol.add(tmpStr);
	}
		
	Collections.sort(tmpSol);		
	possibleSolutions.clear();
	
        for (String s : tmpSol) {
            Vector<String> tmp = new Vector<>();
            for (int i = 0; i < s.length(); i++) 
                tmp.add(String.valueOf((s.charAt(i))));
			
            possibleSolutions.add(tmp);
	}
		
	return possibleSolutions;
    
    }
	
				
    /****************************************************
     * Input:	Vector<String> guess, int[] feedback	*
     * 							*
     * To-Do:	Remove colors form the list of possible	* 
     * 		colors for each position.               *
     ****************************************************/
    private void removeColors(Vector<String> guess, int[] feedback) {

        int red = feedback[0];
	int white = feedback[1];
		
	if (red+white == 0) {
            for (String s : guess) { 
		for (Vector<String> str : this.possibleColors)
                    str.remove(s);
            }
	}
	else if (red == 0) {
            if (white > 0) 
		for (int i = 0; i < guess.size(); i++) 
                    this.possibleColors.get(i).remove(guess.get(i));							
	}
    
    }
	
	
    /****************************************************************************
     * Input:	int[] feedback, int count                                       *
     * 										*
     * To-Do:	It selects the next guess. If not all colors have been used,    * 
     * 		then it chooses a guess of the type "1122" and each time,	*
     * 		the numbers increase serially. Otherwise, it fills 		*
     * 		all possible models with data from possibleColors list and	*
     * 		applies minmax technique for the worst case. 			*
     ****************************************************************************/
    private Vector<String> selectGuess(int[] feedback, int index) {
        Vector<String> tmpGuess = new Vector<>();

	if ((index < this.sets.size() - 1) && (feedback[0]+feedback[1] < this.pegs)) {
            if (this.duplicates) {
                int a = this.pegs - (this.pegs/2);
		int b = this.pegs-a; 
				
		for (int i = 0; i < a; i++)
                    tmpGuess.add(this.sets.get(index));
				
		index++;
				
		if (index < this.sets.size()) 
                    for (int i = 0; i < b; i++)
			tmpGuess.add(this.sets.get(index));
            }
            else {
		tmpGuess = this.initialModel();
				
            	int j = 0;
		for (int i = index; i < this.sets.size(); i++)
                    tmpGuess.set(j++, this.sets.get(i));
								
                for (int i = 0; i < tmpGuess.size(); i++) {
                    j = 0;					
                    while (tmpGuess.get(i).equals("x")) {										
                        String s = this.possibleColors.get(i).get(j++);
                            if (!tmpGuess.contains(s))
                                tmpGuess.set(i, s);
                    }
		}
            }
	}
	else {			
            ArrayList<Vector<String>> tmpSol = new ArrayList<>();		
			
            boolean ok = false;
            while (!ok) {
				
                tmpSol.clear();
                for (Vector<String> solution : this.sol)
                    tmpSol.add(solution);

                this.sol.clear();

                for (Vector<String> model : tmpSol) {
                    if (model.contains("x")) {
                        int a = 0;
                        for (int i = 0; i < model.size(); i++) {
                            if (model.get(i).equals("x")) {
                                while ((this.possibleColors.get(i).size() > a)) {
                                    model.set(i, this.possibleColors.get(i).get(a++));

                                    if (this.guessList.contains(model)) {
                                        model.set(i, "x");
                                    }
                                    else {
                                        Vector<String> tmpModel = new Vector<>();
                                        for (String s : model)
                                            tmpModel.add(s);

                                        this.sol.add(tmpModel);

                                        model.set(i, "x");
                                    }
                                }					
                            }				
                        }
                    }					
                    else {						
                        Vector<String> tmpModel = new Vector<>();
                        for (String s : model)
                            tmpModel.add(s);

                        this.sol.add(tmpModel);
                    }
                }

                ok = true;
                for (Vector<String> str : this.sol) {
                    if (str.contains("x")) {
                        ok = false;
                        break;
                    }
                }
            }
			            
            ArrayList<Vector<String>> tmp = this.minimax();			
			
            tmp = this.sortSolutions(tmp);
            tmpGuess = tmp.get(0);

	}
		
	return tmpGuess;
    }

    /************************************************
     * To-Do:   Remove potential codes, when        *
     *          the results are not satisfactory    *
     ************************************************/
    private void remSolutions() {
		
        ArrayList<Vector<String>> tmpSol = new ArrayList<>();
        for(Vector<String> str : this.sol)
            tmpSol.add(str);
			
        for (Vector<String> str : tmpSol) {
            for (int i = 0; i < this.guessList.size(); i++) {
                int[] ch = this.rule.check(this.guessList.get(i), str);
                
                if (!Arrays.equals(ch, this.resultsList.get(i)))
                    this.sol.remove(str);
            }				
        }

    }
          
	
    /************************************************
     * Output:	ArrayList<Vector<String>> tmpSol    *
     *                                              *
     * To-Do:	Apply minimax technique.            * 
     ************************************************/
    private ArrayList<Vector<String>> minimax() {
		
        ArrayList<Vector<String>> tmpSol = new ArrayList<>();
	double minimax = Double.POSITIVE_INFINITY;	
		
	for (Vector<String> tmpCode : this.sol) {
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
		
	
    /************************************************
     * To-Do:	Only to check if exist any problem. *
     ************************************************/
    public boolean finish() {
	return this.history[1][0] == this.pegs;
    }    
    
    
    public ArrayList<Vector<String>> getGuessList(){ return this.guessList; }

    
    public ArrayList<int[]> getResultsList(){ return this.resultsList; }
}
