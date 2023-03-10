
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
public class CalculateEntropy extends Thread {
	
    private ArrayList<Vector<String>> group;
    private ArrayList<Vector<String>> allCodes;
    private int[][] results;
    private Rules r;
    private Vector<String> guess;
    private double score;
	
    public CalculateEntropy(ArrayList<Vector<String>> gr, ArrayList<Vector<String>> allCodes, int[][] res, int numOfPegs, boolean dup) { 
	this.group = gr; 
	this.allCodes = allCodes;
	this.results = res;
	this.r = new Rules(numOfPegs,dup);
	this.guess = new Vector<>();
    }
	
    @Override
    public void run() {
    	// TODO Auto-generated method stub		
	if (this.group.isEmpty()) {
            this.setScore(Double.NEGATIVE_INFINITY);
            this.setGuess(null);
            return;
	}		
		
	ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();
		
	int initialPool = this.allCodes.size();
	double initEntropy = Double.NEGATIVE_INFINITY;	

	for (Vector<String> tmpCode : this.group) {
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
			
            if (entropy == initEntropy) {
                tmpSol.add(tmpCode);
		this.setScore(entropy);
		this.setGuess(tmpCode);
            }
	}						
    }
	
	
    public void findGuess(ArrayList<Vector<String>> tmpSol) {
		
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
        	
            if (this.group.contains(str)) {
        	found = true;
        	lastGuess = str;
            }
        }

        if (!found) 
            lastGuess = tmpSol.get(0);
                        	
        this.setGuess(lastGuess);
    }
	
	
    /********************************************************
     * Input:	Vector<String> guess, int[]feedback         *
     * Output:	(int) tmpSol.size()                         *
     *                                                      *
     * To-Do:	Calculate the number of possible solutions, *
     * 		if the guess is the code.                   *
     ********************************************************/
    private int removeGuess(Vector<String> guess, int[]feedback) {
	ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();
		
	int red = feedback[0];
	int white = feedback[1];
		
	for (Vector<String> str : this.allCodes) {
            int[] response = this.r.check(guess, str);
			
            if (red == response[0] && white == response[1])
		tmpSol.add(str);			
        }

	return tmpSol.size();
    }
	
	
	public void setScore(double sc) { this.score = sc; }
		
	public Vector<String> getGuess() { return this.guess; }
	
	public void setGuess(Vector<String> guess) { this.guess = guess; }

	public double getScore() { return this.score; }
}

