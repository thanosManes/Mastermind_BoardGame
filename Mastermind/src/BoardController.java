/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import javafx.scene.paint.Color;
import java.net.URL;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Thanasis
 */
public class BoardController implements Initializable {
    
    private Color tmp_color;
    private Vector<String> guess = new Vector<>();
    private int row;
    
    private Rectangle[] guessArray = new Rectangle[5];
    private Rectangle[][] recArray = new Rectangle[15][5];
    private Circle[][] feedback = new Circle[15][5];
    private Button[] butArray = new Button[8];
    private Rectangle[] codeArray = new Rectangle[5];
    private Text[][] textArray = new Text[15][2];
    private SerialAlgorithm algSerial;
    private SimpleAlgorithm algSimple;
    private KnuthAlgorithm algKnuth;
    private EntropyAlgorithm algEntropy;
    private ExpectedSizeAlgorithm algExpSize;
    private SerialWorstCase algSerialWorstCase;
    
    private EntropyThread algEntropyThread;
    private ExpectedSizeThread algSizeThread;
    private KnuthThread algKnuthThread;

    //Game game;
    //Hashtable<Color,Integer> color;
    Game game = new Game();            
    Hashtable<String, Color> color = game.getColors();
    Rules rule = new Rules(4, false);
    
    /* 
    /   Create the board game(All @FXML variables)
    */
    
    /*These rectangle are for selecting guess */
    @FXML
    private Rectangle algRec_1, algRec_2, algRec_3, algRec_4,algRec_5;

    
    /*These rectangles are for guessing*/
    @FXML
    private Rectangle guess_1, guess_2, guess_3, guess_4,guess_5;

    
    /* These buttons are for choosing colors */
    @FXML
    private Button p_11,p_12,p_13,p_14,p_21,p_22,p_23,p_24;

    
    /*These rectangles are for seeing guesses of each round*/
    @FXML
    private Rectangle rec_11,rec_12,rec_13,rec_14,rec_15;
    @FXML
    private Rectangle rec_21,rec_22,rec_23,rec_24,rec_25;
    @FXML
    private Rectangle rec_31,rec_32,rec_33,rec_34,rec_35;
    @FXML
    private Rectangle rec_41,rec_42,rec_43,rec_44,rec_45;
    @FXML
    private Rectangle rec_51,rec_52,rec_53,rec_54,rec_55;
    @FXML
    private Rectangle rec_61,rec_62,rec_63,rec_64,rec_65;
    @FXML
    private Rectangle rec_71,rec_72,rec_73,rec_74,rec_75;
    @FXML
    private Rectangle rec_81,rec_82,rec_83,rec_84,rec_85;
    @FXML
    private Rectangle rec_91,rec_92,rec_93,rec_94,rec_95;
    @FXML
    private Rectangle rec_101,rec_102,rec_103,rec_104,rec_105;
    @FXML
    private Rectangle rec_111,rec_112,rec_113,rec_114,rec_115;
    @FXML
    private Rectangle rec_121,rec_122,rec_123,rec_124,rec_125;
    @FXML
    private Rectangle rec_131,rec_132,rec_133,rec_134,rec_135;
    @FXML
    private Rectangle rec_141,rec_142,rec_143,rec_144,rec_145;
    @FXML
    private Rectangle rec_151,rec_152,rec_153,rec_154,rec_155;


    /*These circles are for feedback*/
    @FXML
    private Circle c_11,c_12,c_13,c_14,c_15;
    @FXML
    private Circle c_21,c_22,c_23,c_24,c_25;
    @FXML
    private Circle c_31,c_32,c_33,c_34,c_35;
    @FXML
    private Circle c_41,c_42,c_43,c_44,c_45;
    @FXML
    private Circle c_51,c_52,c_53,c_54,c_55;
    @FXML
    private Circle c_61,c_62,c_63,c_64,c_65;
    @FXML
    private Circle c_71,c_72,c_73,c_74,c_75;
    @FXML
    private Circle c_81,c_82,c_83,c_84,c_85;
    @FXML
    private Circle c_91,c_92,c_93,c_94,c_95;  
    @FXML
    private Circle c_101,c_102,c_103,c_104,c_105;
    @FXML
    private Circle c_111,c_112,c_113,c_114,c_115;
    @FXML
    private Circle c_121,c_122,c_123,c_124,c_125;
    @FXML
    private Circle c_131,c_132,c_133,c_134,c_135;
    @FXML
    private Circle c_141,c_142,c_143,c_144,c_145;
    @FXML
    private Circle c_151,c_152,c_153,c_154,c_155;

    
    /*These boxes are for game settings*/
    @FXML
    private RadioButton code4,code5;
    @FXML
    private CheckBox duplicate;
    @FXML
    private RadioButton pool1, pool2;
           
    
    /*End Game text*/
    @FXML
    private Label txtLabel;
    
    
    /*Check Button*/
    @FXML
    private Button checkButton;
    @FXML
    private Button newGameBut;       
    @FXML
    private Button startButton;
    @FXML
    private Button continueButton;
    
    
    /*Numbers for attemts and results*/
    @FXML
    private Text try_1_1, try_1_2, try_2_1, try_2_2, try_3_1, try_3_2, try_4_1, try_4_2, try_5_1, try_5_2;
    @FXML
    private Text try_6_1, try_6_2, try_7_1, try_7_2, try_8_1, try_8_2, try_9_1, try_9_2, try_10_1, try_10_2;
    @FXML
    private Text try_11_1, try_11_2, try_12_1, try_12_2, try_13_1, try_13_2, try_14_1, try_14_2, try_15_1, try_15_2;
         
    
    @FXML
    private RadioButton entropyAlg, expSizeAlg, knuthAlg, serialv2Alg, serialAlg, simpleAlg;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize Parameters
        initializeButtonArray();
        initializeGuessArray();
        initializeRecArray();
        initializeFeedback();
        initializeCodeArray();
        initializeTextArray();
    } 
    
    
    /**************************** 
     * Paint guesses rectangles * 
     ****************************/
    @FXML
    void paint_1(MouseEvent event) {
        guessArray[0].setFill(tmp_color);
        System.out.println("Code: "+game.getCode());
    }
    @FXML
    void paint_2(MouseEvent event) { guessArray[1].setFill(tmp_color); }
    @FXML
    void paint_3(MouseEvent event) { guessArray[2].setFill(tmp_color); }
    @FXML
    void paint_4(MouseEvent event) { guessArray[3].setFill(tmp_color); }
    @FXML
    void paint_5(MouseEvent event) { guessArray[4].setFill(tmp_color); }

    
    /* Paint code rectangles */
    @FXML
    void paintAlg_1(MouseEvent event) { codeArray[0].setFill(tmp_color); }
    @FXML
    void paintAlg_2(MouseEvent event) { codeArray[1].setFill(tmp_color); }
    @FXML
    void paintAlg_3(MouseEvent event) { codeArray[2].setFill(tmp_color); }
    @FXML
    void paintAlg_4(MouseEvent event) { codeArray[3].setFill(tmp_color); }
    @FXML
    void paintAlg_5(MouseEvent event) { codeArray[4].setFill(tmp_color); }

    
    /************************************
     * Color for each peg.              *
     * First number is for the row      *
     * and the second for the column.   *
     ************************************/ 
    @FXML
    void peg_11(ActionEvent event) { tmp_color = Color.rgb(255, 0, 0); }
    @FXML
    void peg_12(ActionEvent event) { tmp_color = Color.rgb(255, 255, 0); }
    @FXML
    void peg_13(ActionEvent event) { tmp_color = Color.rgb(0, 255, 0); }  
    @FXML
    void peg_14(ActionEvent event) { tmp_color = Color.rgb(0, 0, 255); }
    @FXML
    void peg_21(ActionEvent event) { tmp_color = Color.rgb(255, 0, 255); }
    @FXML
    void peg_22(ActionEvent event) { tmp_color = Color.rgb(0, 255, 255); }  
    @FXML
    void peg_23(ActionEvent event) { tmp_color = Color.rgb(255, 165, 0); } 
    @FXML
    void peg_24(ActionEvent event) { tmp_color = Color.rgb(0, 0, 0); }  
       
    
    /************************************
     *  Fix the buttons of code length  *
     ************************************/
    @FXML   // Code length = 4
    void code4Checked(ActionEvent event) {
        pool1.setText("6");
        pool2.setText("8");
        
        code5.setSelected(false);
    }       
    @FXML   // Code length = 5
    void code5Checked(ActionEvent event) {
        pool2.setText("7");
        
        code4.setSelected(false);                
    }           
    
    
    /************************************
     *  Fix the buttons of pool length  *
     ************************************/
    @FXML   // 1st pool length button
    void pool1Checked(ActionEvent event) { pool2.setSelected(false); }
    @FXML   // 2nd pool length button
    void pool2Checked(ActionEvent event) { pool1.setSelected(false); }
    
    
    /****************************************
     *  Fix the buttons for AI algorithms   *
     ****************************************/
    @FXML   // Serial Algorithm
    void SerialChecked(ActionEvent event) {
        simpleAlg.setSelected(false);
        knuthAlg.setSelected(false);
        entropyAlg.setSelected(false);
        expSizeAlg.setSelected(false);
        serialv2Alg.setSelected(false);
    }
    @FXML   // Simple Algorithm
    void SimpleChecked(ActionEvent event) {
        serialAlg.setSelected(false);
        knuthAlg.setSelected(false);
        entropyAlg.setSelected(false);
        expSizeAlg.setSelected(false);
        serialv2Alg.setSelected(false);
    }       
    @FXML    // Knuth Algorithm
    void KnuthChecked(ActionEvent event) {
        serialAlg.setSelected(false);
        simpleAlg.setSelected(false);
        entropyAlg.setSelected(false);
        expSizeAlg.setSelected(false);
        serialv2Alg.setSelected(false);
    }
    @FXML    // Max Entropy Algorithm
    void EntropyChecked(ActionEvent event) {
        serialAlg.setSelected(false);
        simpleAlg.setSelected(false);
        knuthAlg.setSelected(false);
        expSizeAlg.setSelected(false);
        serialv2Alg.setSelected(false);
    }
    @FXML    // Expected Size Algorithm
    void ExpSizeChecked(ActionEvent event) {
        serialAlg.setSelected(false);
        simpleAlg.setSelected(false);
        knuthAlg.setSelected(false);
        entropyAlg.setSelected(false);
        serialv2Alg.setSelected(false);
    }    
    @FXML    // Serial Algorithm With Worst Case
    void Serialv2Checked(ActionEvent event) {
        serialAlg.setSelected(false);
        simpleAlg.setSelected(false);
        knuthAlg.setSelected(false);
        entropyAlg.setSelected(false);
        expSizeAlg.setSelected(false);
    }
    
    
    @FXML
    void continueButClicked(ActionEvent event) throws InterruptedException {
        ArrayList<Vector<String>> sol = new ArrayList<>();
        ArrayList<int[]> res = new ArrayList<>();
        
        if (serialAlg.isSelected()) {
            algSerial.runGame();            
            sol = algSerial.getGuessList();
            res = algSerial.getResultsList();
        }
        else if (simpleAlg.isSelected()) {
            algSimple.runGame();            
            sol = algSimple.getGuessList();
            res = algSimple.getResultsList();
        }
        else if (knuthAlg.isSelected()) {
            if ((findCodeLength() == 4) && (findPoolLength() == 6)) {
                algKnuth.runGame();
                sol = algKnuth.getGuessList();
                res = algKnuth.getResultsList();
            }
            else {
                algKnuthThread.runGame();
                sol = algKnuthThread.getGuessList();
                res = algKnuthThread.getResultsList();
            }
        }
        else if (entropyAlg.isSelected()) {
            if (findCodeLength() == 5) {
                algEntropyThread.runGame();
                sol = algEntropyThread.getGuessList();
                res = algEntropyThread.getResultsList();
            }            
            else {        
                algEntropy.runGame();
                sol = algEntropy.getGuessList();
                res = algEntropy.getResultsList();
            }
        }
        else if (expSizeAlg.isSelected()) {
            if (findCodeLength() == 5) {
                algSizeThread.runGame();
                sol = algSizeThread.getGuessList();
                res = algSizeThread.getResultsList();
            }
            else {
                algExpSize.runGame();
                sol = algExpSize.getGuessList();
                res = algExpSize.getResultsList();
            }
        }
        else if (serialv2Alg.isSelected()) {
            algSerialWorstCase.runGame();            
            sol = algSerialWorstCase.getGuessList();
            res = algSerialWorstCase.getResultsList();
        }
        
        fillRec(sol.get(sol.size()-1));

        int red = res.get(res.size()-1)[0];
        int white = res.get(res.size()-1)[1];

        fillCircles(red,white);

        showRec();
        showCircles(red+white);
        checkEnd(red);
    }

 
    /********************************************
     * To-Do:   Check if the code is correct    *
     *          and give feedback.              *
     ********************************************/
    @FXML
    void checkBut(ActionEvent event) throws InterruptedException {
        
        int pegs = this.findCodeLength();
        
        guess.clear();
        for (int i=0; i<pegs; i++) {
            for(Entry<String, Color> entry : color.entrySet()) {
              if(entry.getValue().equals(guessArray[i].getFill())) {
                guess.add(entry.getKey());
                break;
              }
            }    
        }
        
        fillRec(guess);
        
        int[] result = rule.check(game.getCode(), guess);        
        int red = result[0];
        int white = result[1];
        
        fillCircles(red,white);
        
        showRec();
        showCircles(red+white);
        
        checkEnd(red);
    }
      
    
    /************************************************ 
     * To-Do:   Start new game and fix the board.   *
     ************************************************/
    @FXML
    void newGameClicked(ActionEvent event) throws InterruptedException {
        int codeLength = findCodeLength();
        int poolLength = findPoolLength();
        
        /* Hide all feedback circles */
        for (Circle[] feedbackRow : feedback)
            for (Circle fb : feedbackRow)
                fb.setVisible(false);
        
        /* Hide all numeric text*/
        for (Text[] t1 : textArray)
            for (Text t2 : t1)
                t2.setVisible(false);
        
        /* Paint all guesses rectangles grey 
        /  and hide them
        */
        for (Rectangle g : guessArray) {
            g.setFill(Color.GREY);
            g.setVisible(false);
        }
        
        /* Show rectangles. 
        /  #rectangles = codeLenth
        */
        for (int i=0; i<codeLength; i++)
            guessArray[i].setVisible(true);
        
        
        /* Hide all rectangles*/
        for (Rectangle[] recRow : recArray)
            for (Rectangle r : recRow)
                r.setVisible(false);
     
        for (int i = 0; i < butArray.length; i++) {
            if (i < poolLength)
                butArray[i].setVisible(true);
            else
                butArray[i].setVisible(false);
        }
        
        for (Rectangle r : codeArray)
            r.setVisible(false);
        
        startButton.setVisible(false);
        continueButton.setVisible(false);
        
        switch (codeLength) {
            case 4:
                break;
            case 5:
                for (int i=0; i<4; i++) {
                    butArray[i].setDisable(false);
                    butArray[i].setVisible(true);
                }
                break;              
            default:
                throw new AssertionError();
        }
        
        row = 0;

        game = new Game(codeLength, poolLength, duplicate.isSelected());
        rule = new Rules(codeLength,duplicate.isSelected());
        guess.clear();
        
        checkButton.setDisable(false);
        checkButton.setVisible(true);
        
        txtLabel.setText("");
        txtLabel.setVisible(false);                
        
        if (serialAlg.isSelected() || simpleAlg.isSelected() || knuthAlg.isSelected() 
            || entropyAlg.isSelected() || expSizeAlg.isSelected() || serialv2Alg.isSelected()) {
            checkButton.setDisable(true);
            checkButton.setVisible(false);
            
            txtLabel.setText("SELECT CODE");
            txtLabel.setVisible(true);
            
            startButton.setDisable(false);
            startButton.setVisible(true);
            
            for (int i=0; i<codeLength; i++)
                codeArray[i].setVisible(true);                      
        }
    }
    
    
    @FXML
    /****************************************************
     * To-Do:   Check which algorithm is selected and   *
     *          run the correct algorithm.              *
     ****************************************************/
    public void startBut(ActionEvent event) throws InterruptedException {
        int codeLength = findCodeLength();
        int poolLength = findPoolLength();

        txtLabel.setText("");
        txtLabel.setVisible(false);
            
        startButton.setDisable(true);
        startButton.setVisible(false);
            
        for (int i=0; i<codeLength; i++)
            codeArray[i].setVisible(false);                
        
        Vector<String> code = new Vector<>();
                        
        for (int i=0; i< codeLength; i++) {
            for(Entry<String, Color> entry : color.entrySet()) {
              if(entry.getValue().equals(codeArray[i].getFill())) {
                code.add(entry.getKey());
                break;
              }
            }    
        }
        
        for (int i = 0; i < codeLength; i++) 
            guessArray[i].setFill(codeArray[i].getFill());
        
        ArrayList<Vector<String>> sol = new ArrayList<>();
        ArrayList<int[]> res = new ArrayList<>();
        
        continueButton.setVisible(true);
        if (serialAlg.isSelected()) {                      
            algSerial = new SerialAlgorithm(duplicate.isSelected(), codeLength, poolLength, code); 
            algSerial.runGame();                
            sol = algSerial.getGuessList();
            res = algSerial.getResultsList();           
        }
        else if (simpleAlg.isSelected()) {          
            algSimple = new SimpleAlgorithm(duplicate.isSelected(), codeLength, poolLength, code);             
            algSimple.runGame();                
            sol = algSimple.getGuessList();
            res = algSimple.getResultsList();                
        }
        else if (knuthAlg.isSelected()) {          
            if (codeLength==4 && poolLength==6) {
                algKnuth = new KnuthAlgorithm(duplicate.isSelected(), codeLength, poolLength, code);
                algKnuth.runGame();
                sol = algKnuth.getGuessList();
                res = algKnuth.getResultsList(); 
            }
            else {
                algKnuthThread = new KnuthThread(duplicate.isSelected(), codeLength, poolLength, code);
                algKnuthThread.runGame();
                sol = algKnuthThread.getGuessList();
                res = algKnuthThread.getResultsList();
            }
        }
        else if (entropyAlg.isSelected()) { 
            if (codeLength == 5) {
                algEntropyThread = new EntropyThread(duplicate.isSelected(), codeLength, poolLength, code);
                algEntropyThread.runGame();
                sol = algEntropyThread.getGuessList();
                res = algEntropyThread.getResultsList(); 
            }
            else {
                algEntropy = new EntropyAlgorithm(duplicate.isSelected(), codeLength, poolLength, code);            
                algEntropy.runGame();                
                sol = algEntropy.getGuessList();
                res = algEntropy.getResultsList(); 
            }
        }
        else if (expSizeAlg.isSelected()) {                      
            if (codeLength == 5) {
                algSizeThread = new ExpectedSizeThread(duplicate.isSelected(), codeLength, poolLength, code);
                algSizeThread.runGame();
                sol = algSizeThread.getGuessList();
                res = algSizeThread.getResultsList(); 
            }
            else {
                algExpSize = new ExpectedSizeAlgorithm(duplicate.isSelected(), codeLength, poolLength, code);
                algExpSize.runGame();
                sol = algExpSize.getGuessList();
                res = algExpSize.getResultsList(); 
            }
        }
        else if (serialv2Alg.isSelected()) {          
            algSerialWorstCase = new SerialWorstCase(duplicate.isSelected(), codeLength, poolLength, code);            
            algSerialWorstCase.runGame();                
            sol = algSerialWorstCase.getGuessList();
            res = algSerialWorstCase.getResultsList(); 
        }
        
        
        fillRec(sol.get(sol.size()-1));

        int red = res.get(res.size()-1)[0];
        int white = res.get(res.size()-1)[1];

        fillCircles(red,white);
        showRec();
        showCircles(red+white);
        checkEnd(red);  
    }

    
    /****************************************
     * Input:   Vector<String> g            *
     *                                      *
     * To-Do:   Fill with color rectangles. *
     ****************************************/
    public void fillRec(Vector<String> g){
        row = 0;
        if (!recArray[0][0].isVisible()) {
            row = 0;       
        }
        else if (!recArray[1][0].isVisible()) {
            row = 1;       
        }
        else if (!recArray[2][0].isVisible()) {
            row = 2;       
        }
        else if (!recArray[3][0].isVisible()) {
            row = 3;       
        }
        else if (!recArray[4][0].isVisible()) {
            row = 4;       
        }
        else if (!recArray[5][0].isVisible()) {
            row = 5;       
        }
        else if (!recArray[6][0].isVisible()) {
            row = 6;       
        }
        else if (!recArray[7][0].isVisible()) {
            row = 7;       
        }
        else if (!recArray[8][0].isVisible()) {
            row = 8;       
        }
        else if (!recArray[9][0].isVisible()) {
            row = 9;       
        }
        else if (!recArray[10][0].isVisible()) {
            row = 10;       
        }
        else if (!recArray[11][0].isVisible()) {
            row = 11;       
        }
        else if (!recArray[12][0].isVisible()) {
            row = 12;       
        }
        else if (!recArray[13][0].isVisible()) {
            row = 13;       
        }
        else if (!recArray[14][0].isVisible()) {
            row = 14;       
        }
        
        int count = 0;
        for (String s : g)
            recArray[row][count++].setFill(color.get(s));                           
    }
    
    
    /************************************
     * Output:  int codeLength          *
     *                                  *
     * To-Do:   Return the code length. *
     ************************************/
    public int findCodeLength(){
        int codeLength = 0;
        
        if (code4.isSelected()){
            codeLength = 4;
        } 
        else if (code5.isSelected()){
            codeLength = 5;
        }

        return codeLength;
    } 
    
    
    /************************************
     * Output:  int codeLength          *
     *                                  *
     * To-Do:   Return the pool length.*
     ************************************/
    public int findPoolLength(){
        int poolLength = 0;
        
        if (pool1.isSelected()) {
            poolLength = Integer.valueOf(pool1.getText());
        }
        else if (pool2.isSelected()) {
            poolLength = Integer.valueOf(pool2.getText());
        }
        
        return poolLength;
    } 
    
    
    /********************************
     * Input:   int red, int white  *
     *                              *
     * To-Do:   Paint feedback.     *
     ********************************/
    public void fillCircles(int red, int white){
        for(int i=0; i<red; i++)
            feedback[row][i].setFill(Color.RED);
                
        for(int i=0; i<white; i++)
            feedback[row][i+red].setFill(Color.WHITE);        
    }
    
    /****************************************
     * To-Do:   Make rectangles visible.    *
     ****************************************/
    public void showRec() throws InterruptedException{
        for (int i=0; i<findCodeLength(); i++) 
            recArray[row][i].setVisible(true); 
                        
        for (Text t : textArray[row])
            t.setVisible(true);
    }
    
    
    /************************************
     * Input:   int total               *
     *                                  *
     * To-Do:   Make feedback visible.  *        
     ************************************/
    public void showCircles(int total){
        for (int i=0; i<total; i++)
            feedback[row][i].setVisible(true);        
    }
    
    
    /********************************************
     * To-Do:   Check if the game has ended.    *
     ********************************************/
    public void checkEnd(int redPegs) {
        if (redPegs == findCodeLength()){
            txtLabel.setText("You Win");
            txtLabel.setVisible(true);
            checkButton.setDisable(true);
            checkButton.setVisible(false);
            continueButton.setVisible(false);
        }
        else if (feedback[14][0].isVisible()){
            txtLabel.setText("You Loose");
            txtLabel.setVisible(true);
            checkButton.setDisable(true);
            checkButton.setVisible(false);            
        }
    }
    
    
    /************************************************
     * To-Do:   Create an array with all guesses.   *
     ************************************************/
    public void initializeButtonArray(){
        butArray[0] = p_11;
        butArray[1] = p_12;
        butArray[2] = p_13;
        butArray[3] = p_14;
        butArray[4] = p_21;
        butArray[5] = p_22;
        butArray[6] = p_23;
        butArray[7] = p_24;
    }
    
    
    /************************************************
     * To-Do:   Create an array with all guesses.   *
     ************************************************/
    public void initializeGuessArray(){
        guessArray[0] = guess_1;
        guessArray[1] = guess_2;
        guessArray[2] = guess_3;
        guessArray[3] = guess_4;
        guessArray[4] = guess_5;
    }
    
    
    /****************************************************
     * To-Do:   Create a 2d array with all rectangles.  *
     ****************************************************/
    public void initializeRecArray(){       
        // 1st Row
        recArray[0][0] = rec_11;
        recArray[0][1] = rec_12;
        recArray[0][2] = rec_13;
        recArray[0][3] = rec_14;
        recArray[0][4] = rec_15;
        // 2nd Row
        recArray[1][0] = rec_21;
        recArray[1][1] = rec_22;
        recArray[1][2] = rec_23;
        recArray[1][3] = rec_24;
        recArray[1][4] = rec_25;
        // 3rd Row
        recArray[2][0] = rec_31;
        recArray[2][1] = rec_32;
        recArray[2][2] = rec_33;
        recArray[2][3] = rec_34;
        recArray[2][4] = rec_35;
        // 4th Row
        recArray[3][0] = rec_41;
        recArray[3][1] = rec_42;
        recArray[3][2] = rec_43;
        recArray[3][3] = rec_44;
        recArray[3][4] = rec_45;
        // 5th Row
        recArray[4][0] = rec_51;
        recArray[4][1] = rec_52;
        recArray[4][2] = rec_53;
        recArray[4][3] = rec_54;
        recArray[4][4] = rec_55;
        // 6th Row
        recArray[5][0] = rec_61;
        recArray[5][1] = rec_62;
        recArray[5][2] = rec_63;
        recArray[5][3] = rec_64;
        recArray[5][4] = rec_65;
        // 7th Row
        recArray[6][0] = rec_71;
        recArray[6][1] = rec_72;
        recArray[6][2] = rec_73;
        recArray[6][3] = rec_74;
        recArray[6][4] = rec_75;
        // 8th Row
        recArray[7][0] = rec_81;
        recArray[7][1] = rec_82;
        recArray[7][2] = rec_83;
        recArray[7][3] = rec_84;
        recArray[7][4] = rec_85;
        // 9th Row
        recArray[8][0] = rec_91;
        recArray[8][1] = rec_92;
        recArray[8][2] = rec_93;
        recArray[8][3] = rec_94;
        recArray[8][4] = rec_95;
        // 10th Row
        recArray[9][0] = rec_101;
        recArray[9][1] = rec_102;
        recArray[9][2] = rec_103;
        recArray[9][3] = rec_104;
        recArray[9][4] = rec_105;
        // 11th Row
        recArray[10][0] = rec_111;
        recArray[10][1] = rec_112;
        recArray[10][2] = rec_113;
        recArray[10][3] = rec_114;
        recArray[10][4] = rec_115;
        // 12th Row
        recArray[11][0] = rec_121;
        recArray[11][1] = rec_122;
        recArray[11][2] = rec_123;
        recArray[11][3] = rec_124;
        recArray[11][4] = rec_125;
        // 13th Row
        recArray[12][0] = rec_131;
        recArray[12][1] = rec_132;
        recArray[12][2] = rec_133;
        recArray[12][3] = rec_134;
        recArray[12][4] = rec_135;
        // 14th Row
        recArray[13][0] = rec_141;
        recArray[13][1] = rec_142;
        recArray[13][2] = rec_143;
        recArray[13][3] = rec_144;
        recArray[13][4] = rec_145;
        // 15th Row
        recArray[14][0] = rec_151;
        recArray[14][1] = rec_152;
        recArray[14][2] = rec_153;
        recArray[14][3] = rec_154;
        recArray[14][4] = rec_155;
    }
    
        
    /************************************************
     * To-Do:   Create a 2d array with all circles. *
     ************************************************/
    public void initializeFeedback() {       
          // 1st Row
        feedback[0][0] = c_11;
        feedback[0][1] = c_12;
        feedback[0][2] = c_13;
        feedback[0][3] = c_14;
        feedback[0][4] = c_15;
        // 2nd Row
        feedback[1][0] = c_21;
        feedback[1][1] = c_22;
        feedback[1][2] = c_23;
        feedback[1][3] = c_24;
        feedback[1][4] = c_25;
        // 3rd Row
        feedback[2][0] = c_31;
        feedback[2][1] = c_32;
        feedback[2][2] = c_33;
        feedback[2][3] = c_34;
        feedback[2][4] = c_35;
        // 4th Row
        feedback[3][0] = c_41;
        feedback[3][1] = c_42;
        feedback[3][2] = c_43;
        feedback[3][3] = c_44;
        feedback[3][4] = c_45;
        // 5th Row
        feedback[4][0] = c_51;
        feedback[4][1] = c_52;
        feedback[4][2] = c_53;
        feedback[4][3] = c_54;
        feedback[4][4] = c_55;
        // 6th Row
        feedback[5][0] = c_61;
        feedback[5][1] = c_62;
        feedback[5][2] = c_63;
        feedback[5][3] = c_64;
        feedback[5][4] = c_65;
        // 7th Row
        feedback[6][0] = c_71;
        feedback[6][1] = c_72;
        feedback[6][2] = c_73;
        feedback[6][3] = c_74;
        feedback[6][4] = c_75;
        // 8th Row
        feedback[7][0] = c_81;
        feedback[7][1] = c_82;
        feedback[7][2] = c_83;
        feedback[7][3] = c_84;
        feedback[7][4] = c_85;
        // 9th Row
        feedback[8][0] = c_91;
        feedback[8][1] = c_92;
        feedback[8][2] = c_93;
        feedback[8][3] = c_94;
        feedback[8][4] = c_95;
        // 10th Row
        feedback[9][0] = c_101;
        feedback[9][1] = c_102;
        feedback[9][2] = c_103;
        feedback[9][3] = c_104;
        feedback[9][4] = c_105;
        // 11th Row
        feedback[10][0] = c_111;
        feedback[10][1] = c_112;
        feedback[10][2] = c_113;
        feedback[10][3] = c_114;
        feedback[10][4] = c_115;                
        // 12th Row
        feedback[11][0] = c_121;
        feedback[11][1] = c_122;
        feedback[11][2] = c_123;
        feedback[11][3] = c_124;
        feedback[11][4] = c_125;        
        // 13th Row
        feedback[12][0] = c_131;
        feedback[12][1] = c_132;
        feedback[12][2] = c_133;
        feedback[12][3] = c_134;
        feedback[12][4] = c_135;       
        // 14th Row
        feedback[13][0] = c_141;
        feedback[13][1] = c_142;
        feedback[13][2] = c_143;
        feedback[13][3] = c_144;
        feedback[13][4] = c_145;        
        // 15th Row
        feedback[14][0] = c_151;
        feedback[14][1] = c_152;
        feedback[14][2] = c_153;
        feedback[14][3] = c_154;
        feedback[14][4] = c_155;

    }
    
    
    /********************************************************
     * To-Do:   Create an array with all code's rectangles. *
     ********************************************************/
    public void initializeCodeArray() {
        codeArray[0] = algRec_1;
        codeArray[1] = algRec_2;
        codeArray[2] = algRec_3;
        codeArray[3] = algRec_4;
        codeArray[4] = algRec_5;
    }
    
    
    /****************************************************
     * To-Do:   Create an array with all text of tries. *
     ****************************************************/
    public void initializeTextArray(){
        textArray[0][0] = try_1_1;
        textArray[0][1] = try_1_2;
        textArray[1][0] = try_2_1;
        textArray[1][1] = try_2_2;
        textArray[2][0] = try_3_1;
        textArray[2][1] = try_3_2;
        textArray[3][0] = try_4_1;
        textArray[3][1] = try_4_2;
        textArray[4][0] = try_5_1;
        textArray[4][1] = try_5_2;
        textArray[5][0] = try_6_1;
        textArray[5][1] = try_6_2;
        textArray[6][0] = try_7_1;
        textArray[6][1] = try_7_2;
        textArray[7][0] = try_8_1;
        textArray[7][1] = try_8_2;
        textArray[8][0] = try_9_1;
        textArray[8][1] = try_9_2;
        textArray[9][0] = try_10_1;
        textArray[9][1] = try_10_2;
        textArray[10][0] = try_11_1;
        textArray[10][1] = try_11_2;
        textArray[11][0] = try_12_1;
        textArray[11][1] = try_12_2;        
        textArray[12][0] = try_13_1;
        textArray[12][1] = try_13_2;
        textArray[13][0] = try_14_1;
        textArray[13][1] = try_14_2;                
        textArray[14][0] = try_15_1;
        textArray[14][1] = try_15_2;
    }
}
