/**
 * 
 */
package edu.usc.csci561.project2;

import java.lang.management.*;

/**
 * @author Nader Noori
 * @date Sep 20, 2009
 * This is a part of supplementary material for homework2 of CSCI460 
 * 
 */
public class GameEngine {
	
	static final int ROWS = 32 ;
	static final int COLUMNS = 32;
	static final int NUM_OF_COLORS = 5 ;
	static final long TIME_LIMIT = 10000000000l;
	static final int PENALTY_FOR_MILI_SEC = -400 ;//for every millisecond the agent will lose 400 points

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int outputMode = 0 ;
		
		if (args.length < 2) {
			System.out.println("usage : java edu.usc.csci460.hw2.GameEngine edu.usc.csci460.hw2.{Player1}  edu.usc.csci460.hw2.{Player2} [o/O]"  );
			System.out.println("Player1 and Player2 are classes extended edu.usc.csci460.hw2.Player");
			System.out.println("o for regular output to show the moves and the current state of the game board, O to show the selection");
			System.exit(-1);
		}
		if (args.length==3 && args[2].equalsIgnoreCase("o") ){
			if(args[2].equals("o")){outputMode=1;}else{outputMode=2;}
		}
		Player.rows = ROWS ;
		Player.columns = COLUMNS ;
		Player.numOfColor = NUM_OF_COLORS;
		GameBoard  gmbrd = new GameBoard(ROWS,COLUMNS,NUM_OF_COLORS,outputMode);
		
		
		Player player1 = null ;
		Player player2 = null ;
		try {
			player1 = (Player)(Class.forName(args[0]).newInstance());
			player2 = (Player)(Class.forName(args[1]).newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		int sc1 = 0 ;
		int sc2 = 0 ;
		int[] m = new int[2];
		long p1TimeLeft = TIME_LIMIT ;long p2TimeLeft = TIME_LIMIT ; 
		while(getSum(gmbrd.getBoard()) >-ROWS*COLUMNS){
			
			int[][] tboard = getACopyOfTheBoard(gmbrd.getBoard());
			long startCpuTime = Player.getCpuTime();
			m = player1.findMove(tboard, p1TimeLeft,p2TimeLeft);
			long endCpuTime = Player.getCpuTime();
			p1TimeLeft -= endCpuTime - startCpuTime ;
			int p = gmbrd.move(m[0], m[1],player1.getClass().getName()) ;
			sc1 += p*p ;
			if(getSum(gmbrd.getBoard()) == -ROWS*COLUMNS) break ;
	
			tboard = getACopyOfTheBoard(gmbrd.getBoard());
			startCpuTime = Player.getCpuTime();
			m = player2.findMove(tboard, p2TimeLeft,p1TimeLeft);
			endCpuTime = Player.getCpuTime();
			p2TimeLeft -= endCpuTime - startCpuTime ;
			p =  gmbrd.move(m[0], m[1],player1.getClass().getName());
			sc2 += p*p ;
		}
		
		sc1 += PENALTY_FOR_MILI_SEC*(int)(-Math.min(0,p1TimeLeft)/1000000) ;
		sc2 += PENALTY_FOR_MILI_SEC*(int)(-Math.min(0,p2TimeLeft)/1000000) ;
		System.out.println(p1TimeLeft + " " + p2TimeLeft + " " + SmartPlayer.count + " " + _5399443653Player.count);
		System.out.println( player1.getClass().getName() + " scored "+ sc1 + "\n"+player2.getClass().getName() +"  scored "+sc2) ;
	}
	
	public static int[][] getACopyOfTheBoard(int[][] b){
		int[][] cb = new int[b.length][b[0].length]; 
		for(int i = 0 ; i < b.length ; i++)
			for(int j= 0 ; j < b[0].length ; j++)
				cb[i][j] = b[i][j];
				
		return cb ;
	}
	
	public static int getSum(int[][] b){
		int s =0 ;
		for (int i = 0 ; i < b.length ;i++)
			for(int j = 0 ; j < b[0].length ; j++)
				s += b[i][j];
		return s ;
	}
	
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
