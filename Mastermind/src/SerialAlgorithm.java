import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thanasis
 */
public class SerialAlgorithm {
    
    /****************************************************************
     *                      Variables Definitions                   *
     * ============================================================ *
     * duplicates:  Indicate if duplicates pegs are allowed.        *
     * pegs:        The length of the code.                         *
     * code:        The code from codemaker.                        *
     * sol:         The models of every possible solution.          *
     * guessList:   Save all guesses.                               *
     * resultsList: Save all feedbacks.                             *
     * sets:        The set of possible solutions for every peg.    * 
     * history:     Contains the number of red pegs(1st),           *
     *              the number of white pegs(2nd)                   *
     *              and the total(red+white)(3rds).                 *
     ****************************************************************/
    private final boolean duplicates;
    private final int pegs;     
    private final Vector<String> code;
    private final ArrayList<Vector<String>> sol;
    private final ArrayList<Vector<String>> guessList;
    private final ArrayList<int[]> resultsList;
    private final ArrayList<String> sets;
    private final int[][] history = {{0,0,0},{0,0,0}};     
    private final Rules rule;
    private int index;
    private int count;
	
    
    public SerialAlgorithm(boolean duplicates, int pegs, int poolLength, Vector<String> code) {
        this.duplicates = duplicates;
        this.pegs = pegs;
        this.code = code;
        this.sol = new ArrayList<Vector<String>>();
        this.guessList = new ArrayList<Vector<String>>();
        this.resultsList = new ArrayList<int[]>();
        this.sets = this.createSet(poolLength);
        this.rule = new Rules(this.pegs, this.duplicates);
    }
    
    
    /************************************************************
     * To-Do:   The basic function to run the algorithm.        *
     *          The whole algorithm runs through this function. *
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
                this.index = 1;
            else
                this.index = this.pegs;

            if(!this.duplicates)
                this.createSolutionsNoDuplicates(guess, feedback, this.rule);                            
            else
                this.createSolutionsDuplicates(guess, guess.get(0), this.rule);
                            
            this.sortSolutions();
            
        } 
        else {
            this.count = 0;
			
            if (!this.duplicates) {
                this.count = 0;
                for (String s : this.sol.get(0))
                    if (s.equals("_"))
                        this.count++;							
            }
			
            Vector<String> guess = new Vector<>();
            guess = this.selectGuess(this.guessList.get(this.guessList.size()-1), this.resultsList.get(this.resultsList.size()-1));
            this.guessList.add(guess);	                

            this.setHistory(false, feedback);
            feedback = rule.check(this.code, guess);
            this.resultsList.add(feedback);
            this.setHistory(true, feedback);
						
            if(!this.duplicates)           
                this.createSolutionsNoDuplicates(guess, feedback, this.rule);
            else
                this.createSolutionsDuplicates(guess, this.sets.get(this.index), this.rule);
            
            this.sortSolutions();
            this.remSolutions();
            			
            if (this.duplicates) {
                if (this.index < this.sets.size()-1)
                    this.index++;
            }
            else {
                int tmp = this.index + this.count;
                if (tmp < this.sets.size())
                    this.index = tmp;
            }			
        }
    }
	
    
    /************************************************
     * Input:   int pooLength                       *
     * Output:  String[] results                    *
     *                                              *
     * To-Do:   Create possible choices of pegs,    *
     *          based on the number of pegs.        *
     ************************************************/
    private ArrayList<String> createSet(int poolLength) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 1; i <= poolLength; i++) {
            if (i < 10) {
                results.add(String.valueOf(i));
            }
            else if (i == 10) {
                results.add("A");
            }
            else if (i == 11) {
                results.add("B");
            }            
            else if (i == 12) {
                results.add("C");
            } 
        }         
		
        return results;
    }

    
    /************************************************************
     * Output:  Vector<String> guess                            *
     *                                                          *
     * To-DO:   Returns the initial model. At the beginning,    *
     *          all solutions are possible, so the model is     *
     *          of the form "_ _ _ _". The symbol "_", means    * 
     *          that every color is possible.                   *
     ************************************************************/
    private Vector<String> initialModel(){
        Vector<String> guess = new Vector<>();
		
        for (int i = 0; i < this.pegs; i++)
            guess.add("_");
				
        return guess;
    }
	
    
    /****************************************************
     * Output:	Vector<String> fGuess                   *
     *                                                  *
     * To-Do:   Create the first guess, depending on    * 
     *          whether duplicates are allowed.         *
     ****************************************************/
    private Vector<String> firstGuess(){
        Vector<String> fGuess = new Vector<>();
		
        if (this.duplicates)
            for (int i = 0; i < this.pegs; i++)
                fGuess.add("1");
        else
            for (int i = 1; i <= this.pegs; i++)
                fGuess.add(String.valueOf(i));
		
        return fGuess;			
    }
	
    
    /********************************************************************
     * Input:   Vector<String> guess, int[] feedback, String s, Rules r *
     *                                                                  *
     * To-Do:   Find all possible models of solutions for               *
     *          non duplicate games.                                    *
     ********************************************************************/
    private void createSolutionsDuplicates(Vector<String> guess, String s, Rules r) {
		
        ArrayList<Vector<String>> tmpSol = new ArrayList<>();
        for(Vector<String> str : this.sol)
            tmpSol.add(str);
		
        Vector<String> tmpGuess = new Vector<>();
        for(String str : guess)
            tmpGuess.add(str);
						
        int red = this.history[1][0] - this.history[0][0];
        int white = this.history[1][1] - this.history[0][1];
        int total = red+white;	
		
        // 4 pegs, I have find all colors
        if (this.history[1][2] == this.pegs) {
				
            boolean onlyNumbers = true;
            for (Vector<String> str : this.sol) {
                for (String a : str) {
                    if (a.equals("_")) {
                        onlyNumbers = false;
                        break;
                    }
                }						
            }
				
            if (onlyNumbers) {
                this.removeSolutions(tmpGuess, r);	
            }				
            else if (this.history[1][0] != this.pegs) {								
                int counter = 0;
                for (int i = 0; i < this.sol.get(0).size(); i++) 
                    if (this.sol.get(0).get(i).equals("_")) 
                        counter++;											
					
		this.setSolutions(counter, s, tmpSol);
					
		this.removeSolutions(guess, r);
            }
        }
        //Red == 
        else if (this.history[0][0] == this.history[1][0]) {
            if (this.history[0][1] < this.history[1][1]) {
                this.findModel(tmpGuess, s);

                this.sol.remove(tmpGuess);
					
                this.setSolutions(white, s, tmpSol);

                this.checkForEmpty(tmpSol);
                    
                this.removeSolutions(guess, r);
            }
            else if (this.history[0][1] > this.history[1][1]) {
                this.findModel(tmpGuess, s);

                this.sol.remove(tmpGuess);

                this.removeSolutions(guess, r);
            }				
        }
        // RED >
        else if (this.history[0][0] < this.history[1][0]) {
            if (this.history[0][2] == this.history[1][2]) {
                this.findModel(tmpGuess, s);

                red = this.history[1][0];
					
                for (Vector<String> str :  tmpSol) {
                    int count = 0;
                    for (int i = 0; i < tmpGuess.size(); i++) 
                        if (tmpGuess.get(i).equals(str.get(i)) && !tmpGuess.get(i).equals("_"))
                            count++;
													
                    if (count < red) 
                        this.sol.remove(str);						
                }
                    
                this.removeSolutions(guess, r);
            }
            else if (this.history[0][1] == this.history[1][1]) {	
                this.setSolutions(red, s, tmpSol);

                this.checkForEmpty(tmpSol);
                    
                this.removeSolutions(guess, r);
            }
            else if (this.history[0][1] > this.history[1][1]) {
                total = this.history[1][2] - this.history[0][2]; 
					
                this.setSolutions(total, s, tmpSol);
					
                this.checkForEmpty(tmpSol);
                    
                this.removeSolutions(guess, r);
            }
            else if (this.history[0][1] < this.history[1][1]) {					
                this.setSolutions(red, s, tmpSol);
					
                this.findModel(tmpGuess, s);

                this.checkForEmpty(tmpSol);

                this.sol.remove(tmpGuess);

                this.setSolutions(white, s, tmpSol);
					
                this.checkForEmpty(tmpSol);
                    
                this.removeSolutions(guess, r);
            }
        }
        //RED <
        else {
            if (this.history[0][2] >= this.history[1][2]) {
                this.findModel(tmpGuess, s);
					
                this.sol.remove(tmpGuess);
                    
                this.removeSolutions(guess, r);
            }
            else if (this.history[0][2] < this.history[1][2]) {
                this.findModel(tmpGuess, s);
					
                this.sol.remove(tmpGuess);

                this.setSolutions(total, s, tmpSol);
					
                this.checkForEmpty(tmpSol);
                    
                this.removeSolutions(guess, r);
            }			
        }				
    }
	

    /********************************************************
     * Input:   Vector<String> guess, Rules r, int index    *
     *                                                      *
     * To-Do:   Find all possible models of solutions       * 
     *          for non duplicate game.                     *
     ********************************************************/
    private void createSolutionsNoDuplicates(Vector<String> tmpGuess, int[] feedback, Rules r) {
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
                int[] results = r.check(guess, tmpGuess);
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
                                if(guess.get(i).equals("_")) {
                                    guess.set(i, color);							
								
                                    Vector<String> tmp = new Vector<>();
                                    for(String a : guess)
                                        tmp.add(a);

                                    if (tmpGuess.get(i).equals(tmp.get(i))) 
                                        if (!this.sol.contains(tmp)) 
                                            this.sol.add(tmp);																			

                                    guess.set(i, "_");
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
                int[] results = r.check(guess, tmpGuess);
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
                                if(guess.get(i).equals("_")) {
                                    guess.set(i, color);							
									
                                    Vector<String> tmp = new Vector<>();
                                    for(String a : guess)
                                        tmp.add(a);
										
                                    if (!tmpGuess.get(i).equals(tmp.get(i))) 
                                        if (!this.sol.contains(tmp)) 
                                            this.sol.add(tmp);
																													
                                    guess.set(i, "_");
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
	
    
    /****************************************************************
     * Input:   Vector<String> guess, int[] feedback, int count     *
     *                                                              *
     * To-Do:   It chooses the next guess, choosing the solution    * 
     *          that is numerically smaller.                        *
     ****************************************************************/
    private Vector<String> selectGuess(Vector<String> guess, int[] feedback) {
	Vector<String> tmpGuess = new Vector<>();
		
	int red = feedback[0];
	int white = feedback[1];
	int total = red+white;

	if(this.duplicates) {
            if (total == 0) {
                String num = String.valueOf(Integer.parseInt(guess.get(0))+1);
		for (int i = 0; i < this.pegs; i++)
                    tmpGuess.add(num);				
            } 
            else {
                Vector<String> tmpStr = new Vector<>();
				
		for (String s : this.sol.get(0))
                    tmpStr.add(s);
				
		for(int i = 0; i < tmpStr.size(); i++) 
                    if(tmpStr.get(i).equals("_"))
                        tmpStr.set(i, this.sets.get(this.index));
				
                    tmpGuess = tmpStr;
            }						
	}
	else {
            Vector<String> tmpStr = new Vector<>();
			
            if (this.index == this.sets.size()) {
                for(String color : this.sol.get(0))
                    tmpGuess.add(color);
            }
            else {			
		for (Vector<String> str : this.sol) {
                    int a = 0;
                    for (String s : str) 
			if (s.equals("_"))
                            a++;									
					
                    if (this.sets.size()-this.index >= a) {
                        for (String s : str) 
                            tmpStr.add(s);
						
			break;
                    }					
	 	}
				
		for(int i = 0; i < tmpStr.size(); i++) {
                    if(tmpStr.get(i).equals("_")) {
                        if (!tmpStr.contains(this.sets.get(this.index)))
                            tmpStr.set(i, this.sets.get(this.index));
											
			if (this.index < this.sets.size())
                            this.index++;
                    }
		}				
		tmpGuess = tmpStr;
            }
	}		
	return tmpGuess;
    }
	
        
    /************************************************************************
     * Input:   int counter, String s, ArrayList<Vector<String>> tmpSol     *
     *                                                                      *
     * To-Do:   Used for games where duplicates are allowed. Depending on   * 
     *          the results, he formulates the solutions appropriately.     *
     ************************************************************************/
    private void setSolutions(int c, String s, ArrayList<Vector<String>> tmpSol) {
        while (c > 0) {
            tmpSol.clear();
            for(Vector<String> str : this.sol)
                tmpSol.add(str);
			
            this.sol.clear();
			
            for(Vector<String> str : tmpSol){ 
                for(int i=0; i<str.size(); i++) {
                    if(str.get(i).equals("_")) {
                        str.set(i, s);
						
                        Vector<String> tmpStr = new Vector<>();
                        for(String a : str)
                            tmpStr.add(a);
						
                        if (!this.sol.contains(tmpStr))
                            this.sol.add(tmpStr);
						
                        str.set(i, "_");
                    }
                }
            }										
        c--;
        }
    }
	
        
    /****************************************************
     * Input:   ArrayList<Vector<String>> tmpSol        *
     *                                                  *
     * To-Do:   Checks if the list with the possible    *
     *          solutions is empty. If yes, then        *
     *          fill this properly.                     *
     ****************************************************/
    private void checkForEmpty(ArrayList<Vector<String>> tmpSol) {
        if (this.sol.isEmpty()) 
            for(Vector<String> str : tmpSol)
                this.sol.add(str);
    }

    
    /********************************************
     * Input:   boolean current, int[] feedback *
     *                                          *
     * To-Do:   Update history array.           *
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
	
        
    /************************************************
     * Input:   Vector<String> tmpGuess, String s   *
     *                                              *
     * To-DO:   Identifies the model from which the * 
     *          solution is derived.                *
     ************************************************/
    private void findModel(Vector<String> tmpGuess, String s) {
        for(int i=0; i<tmpGuess.size(); i++) 
            if(tmpGuess.get(i).equals(s)) 
                tmpGuess.set(i, "_");									
    }

        
    /********************************************
     * Output:  True or False                   *
     *                                          *
     * To-Do:   Checks if the game has ended.   *	
     ********************************************/
    public boolean finish() {
        if (this.history[1][0] == this.pegs)
            return true;
		
        return false;
    }

        
    /****************************************
     * To-Do:   Sort all possible models.   *
     ****************************************/
    private void sortSolutions() {

        Vector<String> tmpSol = new Vector<>();
        for(Vector<String> str : this.sol) {
            String tmpStr = "";
            for(String s : str) 
                tmpStr += s;
			
            tmpSol.add(tmpStr);
        }
		
        Collections.sort(tmpSol);
		
        this.sol.clear();
        for (String s : tmpSol) {
            Vector<String> tmp = new Vector<>();
            for (int i = 0; i < s.length(); i++) 
                tmp.add(String.valueOf((s.charAt(i))));
			
            this.sol.add(tmp);
        }	
    }
	
        
    /********************************************************
     * Input:   Vector<String> tmpGuess, Rules r            *
     *                                                      *
     * To-Do:   If we have find all numbers, then we keep   *
     *          only the solutions with the same feedback,  * 
     *          if the code was the current guess.          *
     ********************************************************/
    private void removeSolutions(Vector<String> tmpGuess, Rules r) {
		
        ArrayList<Vector<String>> tmpSol = new ArrayList<>();
        for(Vector<String> str : this.sol)
            tmpSol.add(str);
			
        for (Vector<String> str : tmpSol) { 
            int[] ch = r.check(tmpGuess, str);
			
            if ((ch[0] != this.history[1][0]) || !(ch[1] == this.history[1][1]))
                this.sol.remove(str);					
        }
		
        this.sol.remove(tmpGuess);
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
        
    
    public ArrayList<Vector<String>> getGuessList(){ return this.guessList; }

    
    public ArrayList<int[]> getResultsList(){ return this.resultsList; }
}
