
import java.util.ArrayList;
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
public abstract class Algorithm {
	
    protected boolean duplicates;
    protected int pegs;
    protected Vector<String> code;
    protected ArrayList<String> sets;
    protected ArrayList<Vector<String>> sol;
    protected ArrayList<Vector<String>> unusedCodes;
    protected Rules rule;
    protected int[][] results;
    protected ArrayList<Vector<String>> guessList;
    protected ArrayList<int[]> resultsList;
    private int[][] results_4 = {   {0,0},{0,1},{0,2},{0,3},{0,4},
                                    {1,0},{1,1},{1,2},{1,3},
                                    {2,0},{2,1},{2,2},
                                    {3,0},{4,0} }; 
    private int[][] results_5 = {   {0,0},{0,1},{0,2},{0,3},{0,4},{0,5},
                                    {1,0},{1,1},{1,2},{1,3},{1,4},
                                    {2,0},{2,1},{2,2},{2,3},
                                    {3,0},{3,1},{3,2},
                                    {4,0},{5,0} }; 


    public Algorithm (boolean duplicates, int pegs, int poolLength, Vector<String> code) {
	this.duplicates = duplicates;
	this.pegs = pegs;
	this.code = code;
	this.sets = this.createSet(poolLength);
        this.sol = new ArrayList<>();
        this.unusedCodes = new ArrayList<>();
	this.rule = new Rules(this.pegs, this.duplicates);
        this.guessList = new ArrayList<>();
        this.resultsList = new ArrayList<>();
	this.results = this.createResults();
    }

    
    /************************************************
     * Output:	ArrayList<String> res               *
     *                                              *
     * To-Do: 	Create possible choices of pegs,    *
     * 		based on the number of pegs.        *
     ************************************************/
    public ArrayList<String> createSet(int poolLength) {
    	ArrayList<String> res = new ArrayList<>();
        
        for (int i = 1; i <= poolLength; i++)
            res.add(Integer.toString(i));
				
        return res;
    }
    
    
    /****************************************************
     * Output:	int[] res	 			*
     * 							*
     * To-Do: 	Create possible all possible results,   *
     * 		based on the number of pegs.		*
     ****************************************************/
    public int[][] createResults() {
	int[][] res;
		
        res = switch (this.pegs) {
	    case 4 -> this.results_4;
	    case 5 -> this.results_5;
	    default -> null;
        };
		
	return res;
    }
	
	
    /********************************************
     * To-Do:   Create all possible codes, when *
     *          duplicates allowed.             *
     ********************************************/	
    public void createSetOfSolutionsDuplicates(){		
	ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();

	int total = 1;
		
	total = (int) Math.pow(this.sets.size(), this.pegs);						
		
		
	int div  = this.sets.size();		
	int sets = total;
	int counter = 0;
	int index = 0;
		
	while (counter < this.pegs) {
            int even = this.pegs % 2;
            if (even != 0)
		this.sortSolutions();
						
            tmpSol.clear();
            for(Vector<String> str : this.sol)
                tmpSol.add(str);
			
            sets /= div;
			
            this.sol.clear();
            while (index < total-1) {
		for (int i = 0; i < this.sets.size(); i++) {
                    for (int j = 0; j < sets; j++) {
                        if(index == total) 
                            break;						
						
			if (counter == 0) {
                            Vector<String> str = new Vector<String>();
                            str.add(this.sets.get(i));
                            this.sol.add(str);
                            index++;
			}
			else {
                            Vector<String> str = new Vector<String>();
                            for (String s : tmpSol.get(index))
                                str.add(s);							
							
                            str.add(this.sets.get(i));
                            this.sol.add(str);
                            index++;							
			}
                    }						
		}					
            }
	
            index = 0;
            counter++;
	}				
    }
	
	   
    /********************************************
     * To-Do:   Create all possible codes, when *
     *          duplicates not allowed.         *
     ********************************************/	
    public void createSetOfSolutionsNoDuplicates() {
	helper(this.sets, 0);
    }

    /***************************************
     * To-Do:   Create all permutations.   *
     ***************************************/  
    private void helper(ArrayList<String> array, int pos){  
        if(pos >= array.size() - 1){   
            Vector<String> possibleCode = new Vector<>();
            for(int i = 0; i < this.pegs-1; i++){  
            	possibleCode.add(array.get(i));
            }  
            if(array.size() > 0)   
            	possibleCode.add(array.get(array.size() - 1));

            this.sol.add(possibleCode);
            return;  
        }  
  
        for(int i = pos; i < array.size(); i++){   
          
            String t = array.get(pos); 
            array.set(pos, array.get(i));
            array.set(i, t);
  
            helper(array, pos+1);  
  
            t = array.get(pos);  
            array.set(pos, array.get(i));
            array.set(i, t);
        }  
    }  

		
    /****************************************
     * To-Do: Sort all possible solutions.  *
     ****************************************/
    public void sortSolutions() {

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

    	
    /****************************************************
     * Input:	Vector<String> guess, int[]feedback	*
     * 							*
     * To-Do:   Remove from possible solutions any code	* 
     * 		that would not give the same response 	*
     * 		if the current guess were the code. 	*
     ****************************************************/
    public void removeSolutions(Vector<String> guess, int[]feedback) {
	ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();
		
	int red = feedback[0];
	int white = feedback[1];
		
	for (Vector<String> str : this.sol) {
            int[] response = this.rule.check(guess, str);
			
            if (red == response[0] && white == response[1])
                tmpSol.add(str);			
        }
		
	this.sol.clear();
	for (Vector<String> str : tmpSol)
            this.sol.add(str);
    }
	
		
    /********************************************************************
     * Input:	ArrayList<Vector<String>> tmpSol			*
     * Output:	Vector<String> lastGuess 				*
     * 									*
     * To-Do:	From the set of guesses with the minimum (max) score,	* 
     *		select one as the next guess, choosing a member of 	*
     * 		possible solutions whenever possible. We follow 	*
     * 		the convention of choosing the guess with 		*
     * 		the least numeric value.				*
     ********************************************************************/
    public Vector<String> chooseOption(ArrayList<Vector<String>> tmpSol){
		
	Vector<String> tmp = new Vector<>();
	for(Vector<String> str : tmpSol) {
            String tmpStr = "";
            for(String s : str) 
                tmpStr += s;
			
            tmp.add(tmpStr);
	}
		
	Collections.sort(tmp);
		
	tmpSol.clear();
	for (String s : tmp) {
            Vector<String> g = new Vector<>();
            for (int i = 0; i < s.length(); i++) 
                g.add(String.valueOf((s.charAt(i))));
			
            tmpSol.add(g);
	}
		
	Vector<String> lastGuess = new Vector<>();
		
        boolean found = false;

        for (Vector<String> str : tmpSol) {
            if (found)
        	break;
        	
            if (this.sol.contains(str)) {
        	found = true;
            	lastGuess = str;
            }
        }

        if (!found) 
            lastGuess = tmpSol.get(0);
                        
        return lastGuess;
    }
    
    
    public ArrayList<Vector<String>> getGuessList(){
        return this.guessList;
    }
    
    
    public ArrayList<int[]> getResultsList(){
        return this.resultsList;
    }	
    
    
    public abstract void runGame();
    public abstract Vector<String> firstGuess();
    public abstract Vector<String> selectGuess();
	
}

