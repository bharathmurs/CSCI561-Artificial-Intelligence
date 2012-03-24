package edu.usc.csci561.project2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Nader Noori
 * @date Sep 20, 2009
 * This is a part of supplementary material for homework2 of CSCI460 
 * 
 *||||||||YOU DON'T NEED TO CHANGE ANYTHING HERE!||||||
 *
 * This is an implementation of a an agent that looks on step ahead
 * this is the agent you need to beat strongly to get your programming point
 */
public class GreedyPlayer extends Player{

	Random rnd ;

	public GreedyPlayer() {
		rnd = new Random() ;
	}

	@Override
	public int[] findMove(int[][] board, long myTime , long enTime) {
		int[] m = new int[2];
		//when we have enough time we think one step ahead otherwise we will pick our next move randomly
		if( myTime > 1000000l){
			Map<Integer,Integer> scoreMap = new HashMap<Integer , Integer>();
			Collection<Integer> nodes = new ArrayList<Integer>() ;
			int color = 0 ;
			int node = 0 ; int nr = 0 ; int nc = 0 ;
			for (int i = 0 ;  i < rows*columns  ; i++) {
				if(board[i/columns][i%rows]!=-1)
					nodes.add(i) ;
			}
			while(!nodes.isEmpty()){
				node = ((ArrayList<Integer>)nodes).remove(0);
				nr = node/columns ; nc = node%columns;  
				color = board[nr][nc];
				List<Integer> open = new ArrayList<Integer>() ;
				open.add(node); 
				int p = 0 ;
				while(!open.isEmpty()){
					int t = open.remove(0) ; int tr = t/columns ; int tc = t%columns ;
					p++;board[tr][tc] = -1 ;
					if( tr < rows-1 &&  board[tr+1][tc]==color) {open.add(t+columns);nodes.remove(t+columns);}
					if( tr>0 &&  board[tr-1][tc]==color) {open.add(t-columns) ;nodes.remove(t-columns);}
					if( tc< columns-1  &&  board[tr][tc+1] == color) {open.add(t+1);nodes.remove(t+1);}
					if( tc>0 && board[tr][tc-1]==color) {open.add(t -1) ;nodes.remove(t-1);}
				}
				scoreMap.put(node, p) ;
			}
		
			int maxP = 0 ;
			int nm = 0 ;
			for(int p : scoreMap.values()) maxP = Math.max(maxP,p) ;
			for(int n : scoreMap.keySet()) if(scoreMap.get(n)==maxP){nm=n;break;}
			m[0] = nm/columns ;m[1] = nm%columns ;
		}else{
			m[0] = -1;
			while(m[0]==-1){
				int r = rnd.nextInt(rows);
				int c = rnd.nextInt(columns);
				if(board[r][c]!=-1) {
					m[0] = r ; m[1] = c ;
				}
			}
		}
		return m;
	}


}
