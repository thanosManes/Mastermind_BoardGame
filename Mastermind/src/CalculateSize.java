
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
public class CalculateSize extends Thread {
	
    private ArrayList<Vector<String>> group;
    private ArrayList<Vector<String>> allCodes;
    private int[][] results;
    private Rules r;
    private Vector<String> guess;
    private double score;
	
    public CalculateSize(ArrayList<Vector<String>> gr, ArrayList<Vector<String>> allCodes, int[][] res, int numOfPegs, boolean dup) { 
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
		
	int totalSize = this.allCodes.size();
	double score = Double.POSITIVE_INFINITY;
		
	for (Vector<String> tmpCode : this.group) {
            int count = 0;
            for (int[] res : this.results) 
		for (Vector<String> tmpGuess : this.allCodes)
                    if (Arrays.equals(this.r.check(tmpCode, tmpGuess), res))
                        count++;												
			
            double E = Math.pow(count, 2)/totalSize;
			
            if (E < score) {
		tmpSol.clear();
		score = E;
            }
			
            if (score == E) {
		tmpSol.add(tmpCode);
		this.setScore(score);
                this.setGuess(tmpCode);
            }
						
	}		
    }
		
	
    public void setScore(double sc) { this.score = sc; }
		
    public Vector<String> getGuess() { return this.guess; }
	
    public void setGuess(Vector<String> guess) { this.guess = guess; }

    public double getScore() { return this.score; }
}

