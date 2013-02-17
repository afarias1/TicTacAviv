package edu.swarthmore.cs.cs71;

import java.util.Hashtable;
import java.util.Random;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * HINTS and TIPS:
 * 
 *  - You will definitely want to write extra methods to help the game state. 
 *    Modularize where you can, particularly in the end game condition. If you 
 *    try and do it all in the same method, you'll go crazy.
 *  
 *  - Review the resources files, res/layout/activity_main.xml and res/values/string.xml
 *    * Note that the Buttons and their initial text is set in activity_main.xml
 *    * Ignore the warnings in activity_main.xml, what I did was fine, but the XML 
 *      compiler for eclipse is slow and annoying. 
 *  
 *  - One challenge is mapping a Button to which of the buttons was clicked, 
 *    e.g., was it the top right or top left Button? Consider using a generic data structure 
 *    to help with this mapping
 *    
 *  - There is a google challenge for the toast messages at the bottom. Feel free to 
 *    implement that however you feel and grab code from the web.
 *  
 *  - Use the logging facility, Log.i( ) or Log.d( ), to help the debugging. You should use 
 *    the provided LOG_TAG defined in the class, i.e., "Tic-Tac-Toe".
 *     * You can trace log messages in LogCat output. 
 *     * I've provide a utility function matrixToString() to covert the current game state into
 *       a string to aid the debugging process.
 *  
 *  - Eclipse is your friend. Allow it to help you
 *  
 */



/**
 * The Activity class for the Tic-Tac-Toe Android application. 
 * @author aviv
 *
 */
public class MainActivity extends Activity {

	/**
	 * Tag to use when writing Log messages
	 */
	private final String LOG_TAG = "Tic-Tac-Toe";
	
	/**
	 * associates Buttons with integer pair in matrix */
	
	private Hashtable<ImageButton,IntegerPair> buttonTable;
	private Hashtable<String,ImageButton> reverseTable;
	
	
	
	/**
	 * Matrix to keep track of the game state
	 */
	private int i_matrix[][];
	
	/**
	 * Determine whose turn it is, 0 for X and 1 for O
	 */
	private int turn; 
	
	/**
	 * Textviews to display score of matches won
	 */
	
	private TextView results_cs_71;
	
	private TextView results_eclipse;
	
	/**
	 * ints storing score of matches won
	 */
	 private int score_cs71;
	 private int score_eclipse;
	 
	
	/**
	 * A private class to store the a pair for the x,y coordinate of the button
	 * pressed
	 * 
	 * 
	 * [0,0] [0,1] [0,2] 
	 * [1,0] [1,1] [1,2] 
	 * [2,0] [2,1] [2,2]
	 * 
	 * @author aviv
	 * 
	 */
	private class IntegerPair{
		public int x,y;
		public IntegerPair(int i,int j){
			this.x=i; this.y=j;
			
		}
		
	}
	
	/**
	 * The onCreate() method is where the activity starts
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		turn = 0; // x's turn first
		i_matrix = new int[3][3];
		reset();
		score_cs71 = 0;
		score_eclipse = 0;
		results_cs_71= (TextView) this.findViewById(R.id.tv_score_cs71);
		results_eclipse= (TextView) this.findViewById(R.id.tv_score_Eclipse);
		
		
		CharSequence ScoreHeaderCS71 = getString(R.string.CS71);
		results_cs_71.setText(ScoreHeaderCS71 +" "+ score_cs71);
		
		CharSequence ScoreHeaderEclipse = getString(R.string.Eclipse);
		results_eclipse.setText(ScoreHeaderEclipse +" "+score_eclipse);
		
		buttonTable = new Hashtable<ImageButton,IntegerPair>();
		reverseTable = new Hashtable<String,ImageButton>();
		associateButtons();
		
		toastBegin();
		
				
	}
	

	
	private void associateButtons(){
		
		//get all buttons, associate integer table, put them in hashtable
		buttonTable.put((ImageButton) this.findViewById(R.id.button_0_0),(new IntegerPair(0,0)));
		buttonTable.put((ImageButton) this.findViewById(R.id.button_0_1), (new IntegerPair(0,1)));
		buttonTable.put((ImageButton) this.findViewById(R.id.button_0_2), (new IntegerPair(0,2)));
		buttonTable.put((ImageButton) this.findViewById(R.id.button_1_0), (new IntegerPair(1,0)));
		buttonTable.put((ImageButton) this.findViewById(R.id.button_1_1), (new IntegerPair(1,1)));
		buttonTable.put((ImageButton) this.findViewById(R.id.button_1_2), (new IntegerPair(1,2)));
		buttonTable.put((ImageButton) this.findViewById(R.id.button_2_0), (new IntegerPair(2,0)));
		buttonTable.put((ImageButton) this.findViewById(R.id.button_2_1), (new IntegerPair(2,1)));
		buttonTable.put((ImageButton) this.findViewById(R.id.button_2_2), (new IntegerPair(2,2)));	
		

		// can not use anonymous classes because need to refer to these later
		
		
	}
	/**
	 * The function called whenever a button is clicked
	 * 
	 * @param v the view that was clicked, i.e., the Button that was pressed
	 */	
	public void buttonOnClick(View v){
		ImageButton clickedButton = (ImageButton) v; //cast this View to a Button
		IntegerPair pair_clicked = this.buttonTable.get(clickedButton);
		Log.i(this.LOG_TAG,"x:"+ Integer.toString(pair_clicked.x) + " "+ "y:" + Integer.toString(pair_clicked.y) );
		Log.i(this.LOG_TAG,(matrixToString()));
		
		
		
		if(pair_clicked == null){
			Log.i(this.LOG_TAG,"bad");	
		}
		
		else if(i_matrix[pair_clicked.x][pair_clicked.y]>=0){
			// if button already has a value
			return;
			
		}

		if(i_matrix[pair_clicked.x][pair_clicked.y]< 0 ){
		i_matrix[pair_clicked.x][pair_clicked.y]= 0;
		setButton(clickedButton); 
		
		
		turn++;
		endCondition();
		ComputersPlay();
		turn--;
		
		}
		
	
			
		Log.i(this.LOG_TAG,(matrixToString()) );
		
		endCondition(); // check to see if game has ended
				 
		
	}
	
	private void ComputersPlay(){
		
		Random rand = new Random();
		int i, j;
		i = rand.nextInt(3);
		j = rand.nextInt(3);
		
		Log.i(this.LOG_TAG,"x:"+ Integer.toString(i) + " "+ "y:" + Integer.toString(j) );
		
		
		while(i_matrix[i][j] ==1 || i_matrix[i][j] == 0){ // this is a terrible solution for randomness-- the program crashes after waiting too long
			i = (rand.nextInt(3)) ;
			j = (rand.nextInt(3)) ;	
			Log.i(this.LOG_TAG,"x:"+ Integer.toString(i) + " "+ "y:" + Integer.toString(j));
			
		}
		
		i_matrix[i][j]= 1;
		
		String selected =  Integer.toString(i)+Integer.toString(j);
		
		String str_00 = Integer.toString(0)+Integer.toString(0);
		String str_01 =  Integer.toString(0)+Integer.toString(1);
		String str_02 =  Integer.toString(0)+Integer.toString(2);
		String str_10 =  Integer.toString(1)+Integer.toString(0);
		String str_11 =  Integer.toString(1)+Integer.toString(1);
		String str_12 =  Integer.toString(1)+Integer.toString(2);	
		String str_20 =  Integer.toString(2)+Integer.toString(0);
		String str_21 =  Integer.toString(2)+Integer.toString(1);
		String str_22 =  Integer.toString(2)+Integer.toString(2);
		
	
		reverseTable.put(str_00,(ImageButton) this.findViewById(R.id.button_0_0));
		reverseTable.put(str_01,(ImageButton) this.findViewById(R.id.button_0_1));
		reverseTable.put(str_02,(ImageButton) this.findViewById(R.id.button_0_2));
		reverseTable.put(str_10,(ImageButton) this.findViewById(R.id.button_1_0));
		reverseTable.put(str_11,(ImageButton) this.findViewById(R.id.button_1_1));
		reverseTable.put(str_12,(ImageButton) this.findViewById(R.id.button_1_2));
		reverseTable.put(str_20,(ImageButton) this.findViewById(R.id.button_2_0));
		reverseTable.put(str_21,(ImageButton) this.findViewById(R.id.button_2_1));
		reverseTable.put(str_22,(ImageButton) this.findViewById(R.id.button_2_2));
		
		setButton(this.reverseTable.get(selected));
		
	} 

	private boolean boardComplete(){
	
		if(i_matrix[0][0] < 0){ return false;}
		else if(i_matrix[0][1] < 0){ return false;}
		else if(i_matrix[0][2] < 0){ return false;}
		else if(i_matrix[1][0] < 0){ return false;}
		else if(i_matrix[1][1] < 0){ return false;}
		else if(i_matrix[1][2] < 0){ return false;}
		else if(i_matrix[2][0] < 0){ return false;}
		else if(i_matrix[2][1] < 0){ return false;}
		else if(i_matrix[2][2] < 0){ return false;}
		else{ return true;}
		
	}
	
	private boolean diagonalsCheck(){
		
		if(i_matrix[0][0] == 1 && i_matrix[1][1] == 1 && i_matrix[2][2] == 1){ return true;}
		else if(i_matrix[0][0] == 0 && i_matrix[1][1] == 0 && i_matrix[2][2] == 0){ return true;}
		else if(i_matrix[0][2] == 1 && i_matrix[1][1] == 1 && i_matrix[2][0] == 1){ return true;}
		else if(i_matrix[0][2] == 0 && i_matrix[1][1] == 0 && i_matrix[2][0] == 0){ return true;}
		else{ return false; }		
		
	}
	
	private boolean colsCheck(){
		if(i_matrix[0][0] == 1 && i_matrix[1][0] == 1 && i_matrix[2][0] == 1){ return true;}
		else if(i_matrix[0][0] == 0 && i_matrix[1][0] == 0 && i_matrix[2][0] == 0){ return true;}
		else if(i_matrix[0][1] == 1 && i_matrix[1][1] == 1 && i_matrix[2][1] == 1){ return true;}
		else if(i_matrix[0][1] == 0 && i_matrix[1][1] == 0 && i_matrix[2][1] == 0){ return true;}
		else if(i_matrix[0][2] == 1 && i_matrix[1][2] == 1 && i_matrix[2][2] == 1){ return true;}
		else if(i_matrix[0][2] == 0 && i_matrix[1][2] == 0 && i_matrix[2][2] == 0){ return true;}
		
		else{ return false; }		
		
	}
	
	
	private boolean rowsCheck(){
		if(i_matrix[0][0] == 1 && i_matrix[0][1] == 1 && i_matrix[0][2] == 1){ return true;}
		else if(i_matrix[0][0] == 0 && i_matrix[0][1] == 0 && i_matrix[0][2] == 0){ return true;}
		else if(i_matrix[1][0] == 1 && i_matrix[1][1] == 1 && i_matrix[1][2] == 1){ return true;}
		else if(i_matrix[1][0] == 0 && i_matrix[1][1] == 0 && i_matrix[1][2] == 0){ return true;}
		else if(i_matrix[2][0] == 1 && i_matrix[2][1] == 1 && i_matrix[2][2] == 1){ return true;}
		else if(i_matrix[2][0] == 0 && i_matrix[2][1] == 0 && i_matrix[2][2] == 0){ return true;}
		else{ return false; }		
		
	}

	/**
	 * Check the end condition of the game
	 */
	private void endCondition(){
		Handler handler = new Handler();
		if(diagonalsCheck() || colsCheck() || rowsCheck()){
				
				String winner = null;
				if (turn == 0){
					winner = "CS71"; // can I make XML element, if so make winner= X
					score_eclipse++;
					CharSequence ScoreHeaderEclipse = getString(R.string.Eclipse);
					results_eclipse.setText(ScoreHeaderEclipse +" "+score_eclipse);
					
					
				}
				else if (turn == 1){
					winner = "Eclipse"; //can I make XML element, if  so make winner = O
					score_cs71++;
					CharSequence ScoreHeaderCS71 = getString(R.string.CS71);
					results_cs_71.setText(ScoreHeaderCS71 +" "+ score_cs71);
				}
				toastWinner(winner);	
				handler.postDelayed( new Runnable(){	
					public void run(){	
						reset();
						}
					}, 1000);	
				return;
				
			}
			else if(boardComplete()){
				toastCats();
				handler.postDelayed( new Runnable(){
					public void run(){
						reset();
					}
				}, 1000);
				return;
			}
			
	}	
		
			
	
	
	

	/**
	 * Reset the game: The text of the buttons and the index matrix
	 */
	private void reset(){
		//reset values in hashtable-- why doesn't the for loop work?
		
		turn = 0;
		
		i_matrix[0][0]= -9999;
		i_matrix[0][1]= -9999;
		i_matrix[0][2]= -9999;
		i_matrix[1][0]= -9999;
		i_matrix[1][1]= -9999;
		i_matrix[1][2]= -9999;
		i_matrix[2][0]= -9999;
		i_matrix[2][1]= -9999;
		i_matrix[2][2]= -9999;
		
		ImageButton button_0_0 = (ImageButton) this.findViewById(R.id.button_0_0);
		button_0_0.setImageResource(0);
		ImageButton button_0_1 = (ImageButton) this.findViewById(R.id.button_0_1);
		button_0_1.setImageResource(0);
		ImageButton button_0_2 = (ImageButton) this.findViewById(R.id.button_0_2);
		button_0_2.setImageResource(0);
		ImageButton button_1_0 = (ImageButton) this.findViewById(R.id.button_1_0);
		button_1_0.setImageResource(0);
		ImageButton button_1_1 = (ImageButton) this.findViewById(R.id.button_1_1);
		button_1_1.setImageResource(0);
		ImageButton button_1_2 = (ImageButton) this.findViewById(R.id.button_1_2);
		button_1_2.setImageResource(0);
		ImageButton button_2_0 = (ImageButton) this.findViewById(R.id.button_2_0);
		button_2_0.setImageResource(0);
		ImageButton button_2_1 = (ImageButton) this.findViewById(R.id.button_2_1);
		button_2_1.setImageResource(0);
		ImageButton button_2_2 = (ImageButton) this.findViewById(R.id.button_2_2);
		button_2_2.setImageResource(0);
		
				
	}
	
	/**
	 * Toast the winner of the game by declaring them the winer
	 * @param winner A string representing the winner, either "X" or "O"
	 */
	private void toastWinner(String winner){
		Context context= getApplicationContext();
		CharSequence text = "Winner is" + " "+ winner;
		int duration =Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		
	}
	
	/**
	 * Toast a tie, "CATS!
	 */
	private void toastCats(){
		Context context= getApplicationContext();
		CharSequence text = "Cats!" ;
		int duration =Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	}
	
	private void toastBegin(){
		Context context= getApplicationContext();
		CharSequence text = "Welcome to TicTacAviv! Who will win?";
		int duration =Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	}
	
	/**
	 * Set a button text to either "X" or "O" depending on whose turn it is 
	 * @param b
	 */
	private void setButton(ImageButton b){
		
		if (turn == 0 ){
			b.setImageResource(R.drawable.aviv);		
			return;
		}
		
		else if (turn == 1 ){
			b.setImageResource(R.drawable.eclipse);
			return;
		}
			
		
}
	

	/**
	 * Debug function to convert the i_matrix to a nice readable string
	 * @return
	 */
	private String matrixToString(){
		String ret = "";
		for(int i=0;i<i_matrix.length;i++){
			for(int j=0;j<i_matrix.length;j++){
				if(i_matrix[i][j] < 0 ){
					ret += "_"+" ";	
				}else if(i_matrix[i][j] == 0){
					ret += "X"+" ";	
				}else{
					ret += "O"+" ";	
				}
						
				
			}
			ret += "\n";
		}
		return ret;
	}
}

