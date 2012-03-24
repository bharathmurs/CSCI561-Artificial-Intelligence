package edu.usc.csci561.project2;

import java.util.Random;

/**
 * @author Nader Noori
 * @date Sep 20, 2009
 * This is a part of supplementary material for homework2 of CSCI460 
 * 
 *||||||||YOU DON'T NEED TO CHANGE ANYTHING HERE!||||||
 *
 * This is an implementation of a dummy agent that plays randomly
 * is it really dumm to play randomly under any circumstance? 
 * there are conditions that this agents can beat your agent no matter how smart it is! 
 */
public class RandomPlayer extends Player{
	
	Random rnd ;

	public RandomPlayer() {
		rnd = new Random() ;
	}

	@Override
	public int[] findMove(int[][] board, long myTime , long enTime) {
		int[] m = {-1,-1};
		while(m[0]==-1){
			int r = rnd.nextInt(rows);
			int c = rnd.nextInt(columns);
			if(board[r][c]!=-1) {
				m[0] = r ; m[1] = c ;
			}
		}
		return m;
	}

}
