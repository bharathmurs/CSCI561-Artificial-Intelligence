package edu.usc.csci561.project2;

/**
 * @author Nader Noori
 * @date Sep 20, 2009
 * This is a part of supplementary material for homework2 of CSCI460 
 * 
 **||||||||YOU DON'T NEED TO CHANGE ANYTHING HERE!||||||
 * This is the interface for the Player, it has only one method for implementation,
 * You do not have to implement this interface directly, you need to extend the abstract 
 * class Player which implements this interface.
 */
public interface IPlayer {
	
	/**
	 * @param board : a copy of the current state of the game board
	 * @param urTime : your time left in nano-seconds
	 * @param opTime : your opponent's time left in nano-seconds
	 * @return
	 */
	public int[] findMove(int[][] board, long urTime , long opTime);

}
