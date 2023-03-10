
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
public class CalculateWorstCase extends Thread {
	
    private ArrayList<Vector<String>> group;
    private ArrayList<Vector<String>> allCodes;
    private int[][] results;
    private Rules r;
    private Vector<String> guess;
    private double score;
	  
    public CalculateWorstCase(ArrayList<Vector<String>> gr, ArrayList<Vector<String>> allCodes, int[][] res, int numOfPegs, boolean dup) { 
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
            this.setScore(Double.POSITIVE_INFINITY);
            this.setGuess(null);
            return;
	}
		
        ArrayList<Vector<String>> tmpSol = new ArrayList<Vector<String>>();
	double minimax = Double.POSITIVE_INFINITY;	
		
	boolean prun = false;
		
	for (Vector<String> tmpCode : this.group) {
            double maxScore = 0;			
			
            for (int i = 0; i < this.results.length; i++) {
                double currScore = 0;
		for (Vector<String> tmpGuess : this.allCodes) {
                    if (Arrays.equals(this.r.check(tmpCode, tmpGuess), this.results[i])) {
			currScore++;
						
                    if (prun)
                        if (currScore + (this.allCodes.size()-this.allCodes.indexOf(tmpGuess)+1) < maxScore)
                            break;
                    }
		}
									
		maxScore = Math.max(maxScore, currScore);
            }
			
            if (maxScore < minimax) {
                tmpSol.clear();
		minimax = maxScore;
            }
			
            if (maxScore == minimax) {
		tmpSol.add(tmpCode);
		this.setScore(maxScore);
            }
	}
		
	this.findGuess(tmpSol);
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


    public void setScore(double sc) { this.score = sc; }	

    public Vector<String> getGuess() { return this.guess; }
	
    public void setGuess(Vector<String> guess) { this.guess = guess; }

    public double getScore() { return this.score; }
		
}
