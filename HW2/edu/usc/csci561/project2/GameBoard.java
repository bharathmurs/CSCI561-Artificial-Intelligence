package edu.usc.csci561.project2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Nader Noori
 * @date Sep 20, 2009
 * @time 7:05:32 AM
 * This class is for the use of the GameEngine to keep the state of the game
 * your software agent has no access to this class at all. 
 * please do not change anything in this class, changing the behavior of this 
 * class might be misleading for doing your task, we will test your submission 
 * with our own version of GameBoard class. 
 * Your software agent will receive a copy of the field gameboar which is a two
 * dimensional array of integers representing the game board.  
 */
public class GameBoard {
	private int[][] board ;
	private int rows, columns , numOfColors;
	int outputMode;
	
	/**
	 * @param r number of rows
	 * @param c number of columns
	 * @param nc number of colors
	 */
	public GameBoard(int r , int c , int nc ,  int outMode){
		board = new int[r][c] ;
		this.rows = r ;
		this.columns = c ;
		this.numOfColors = nc ;
		this.outputMode = outMode ;
		Random rnd = new Random();
		for (int i = 0 ; i < r ; i++)
			for (int j = 0 ; j < c ; j++)
				board[i][j] = rnd.nextInt(nc);
		
		if(outputMode != 0) System.out.println(toString());
	}
	
	public int getNumOfColumns() {
		return columns;
	}
	
	public int getNumOfRows() {
		return rows;
	}

	public int[][] getBoard() {
		return board;
	}
	
	public int getNumOfColors() {
		return numOfColors;
	}
	
	public int getColorOf(int r , int c){
		return board[r][c];
	}
	
	private int pick(int r , int c){
		int picked = 0 ;
		int color = board[r][c] ;
		if(color!=-1){
			List<Integer> open = new ArrayList<Integer>() ;
			open.add(r*columns + c ); 
			while(!open.isEmpty()){
				int t = open.remove(0) ; int tr = t/columns ; int tc = t%columns ;
				if(board[tr][tc]!=-1){picked++;board[tr][tc] = -1 ;}
				if( tr < rows-1 &&  board[tr+1][tc]==color) open.add(t+columns);
				if( tr>0 &&  board[tr-1][tc]==color) open.add(t-columns) ;
				if( tc< columns-1  &&  board[tr][tc+1] == color) open.add(t+1);
				if( tc>0 && board[tr][tc-1]==color) open.add(t -1) ;
			}
		}
		return picked ;
	}
	
	
	/**
	 * @param r the row of the cell you intend to move
	 * @param c the column of the cell you intend to move
	 * @return number of cell which have popped 
	 * only GameEngine will call this method, you can't call this method from your class
	 */
	public int move(int r , int c , String playerName){
		
		int p = pick(r ,c) ;
		if(outputMode == 2) {
			System.out.println("player:" + playerName+"  chose cell ("+r+","+c+")" );
			System.out.println(toString());
			System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
		}
		
		for(int i = 0 ; i < columns ; i++){
			int d = 0 ;
			for (int j = rows -1  ;  j >= 0 ; j--){
				if(board[j][i]!=-1){
					board[j+d][i] = board[j][i] ;
				}else{
					d++ ;
				}
			}
			
			for(int j = 0 ;  j < d ; j++) board[j][i]=-1 ;	
		}
		if(outputMode==1) {System.out.println("player:" + playerName+"  chose cell ("+r+","+c+")" );}
		if(outputMode !=0) System.out.println(toString());
		return p ;
	}
	
	/*
	in the case you want to see a fancy output of the latest state of 
	the gameboard just call this method, in the printed output -1 s are
	replace by *s 
	*/
	public String toString(){
		String s = "";
		for(int i = 0 ; i < columns+2;i++)s +="--";
		s+="\n";
		for(int r = 0 ; r < rows ; r++){
			s+="|" ;
			for(int c = 0 ; c < columns ; c++){
				if(board[r][c]!=-1) {s += board[r][c]+"|";}else{s +="*|";}
			}
			s+="\n" ;
		}
		for(int i = 0 ; i < columns+2;i++)s +="--";
		return s ;
	}
}
