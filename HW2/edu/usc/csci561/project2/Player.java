package edu.usc.csci561.project2;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * @author Nader Noori
 * @date Sep 20, 2009
 * This is a part of supplementary material for homework2 of CSCI460 
 * 
 *||||||||YOU DON'T NEED TO CHANGE ANYTHING HERE!||||||
 * This is the class you need to extend in order to build your agent
 * it has only one method inherited form IPlayer that you need to implement.
 * You can access the values of number rows and columns and number of colors 
 * via static fields of this class.
 */
public abstract class Player implements IPlayer{
	
	static int rows, columns, numOfColor ; 
	
	
	/* (non-Javadoc)
	 * @see edu.usc.csci460.hw2.IPlayer#findMove(int[][], long)
	 * !!!!!!!!!!!!This is the place to start! you need to implement this abstract method 
	 * in a class that extends Player !!!!!!!!!!!!!!!!! 
	 */
	public abstract int[] findMove(int[][] board, long urTime , long opTime) ;
	
	public static long getCpuTime(){
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ?
				bean.getCurrentThreadCpuTime() : 0L;
	}
	
	public static long getUserTime(){
		ThreadMXBean bean = ManagementFactory.getThreadMXBean() ;
		return bean.isCurrentThreadCpuTimeSupported() ? 
				bean.getCurrentThreadUserTime() : 0L ;
	}
	
	public static long getSystemTime(){
		ThreadMXBean bean = ManagementFactory.getThreadMXBean() ;
		return bean.isCurrentThreadCpuTimeSupported() ? 
				(bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime()) : 0L ;
	}

	

}
