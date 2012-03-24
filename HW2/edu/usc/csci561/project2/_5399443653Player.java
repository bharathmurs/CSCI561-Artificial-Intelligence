/**
 * 
 */
package edu.usc.csci561.project2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Bharath Urs
 * 
 */

public class _5399443653Player extends Player {
	Random rnd = new Random();
    public static int count = 0;
	public int _pickMax(int[][] board, int myScore) {
		Map<Integer, Integer> scoreMap = new HashMap<Integer, Integer>();
		Collection<Integer> nodes = new ArrayList<Integer>();
		int node = 0;
		int nr = 0;
		int nc = 0;
		int maxP = 0;

		for (int i = 0; i < rows * columns; i++) {
			if (board[i / columns][i % rows] != -1)
				nodes.add(i);
		}
		while (!nodes.isEmpty()) {
			node = ((ArrayList<Integer>) nodes).remove(0);
			nr = node / columns;
			nc = node % columns;
			List<Integer> open = new ArrayList<Integer>();
			open.add(node);
			int p = _pickNeighbours(board, nr, nc, nodes);
			maxP = Math.max(maxP, p);
			if ((myScore * myScore) - (maxP * maxP) < 0) {
				break;
			}
			scoreMap.put(node, p);
		}
		return maxP;
	}

	public int[][] _moveBoard(int[][] board, int r, int c) {
		pick(board, r, c);
		for (int i = 0; i < columns; i++) {
			int d = 0;
			for (int j = rows - 1; j >= 0; j--) {
				if (board[j][i] != -1) {
					board[j + d][i] = board[j][i];
				} else {
					d++;
				}
			}
			for (int j = 0; j < d; j++)
				board[j][i] = -1;
		}
		return board;
	}

	private int pick(int[][] board, int r, int c) {
		int picked = 0;
		int color = board[r][c];
		if (color != -1) {
			List<Integer> open = new ArrayList<Integer>();
			open.add(r * columns + c);
			while (!open.isEmpty()) {
				int t = open.remove(0);
				int tr = t / columns;
				int tc = t % columns;
				if (board[tr][tc] != -1) {
					picked++;
					board[tr][tc] = -1;
				}
				if (tr < rows - 1 && board[tr + 1][tc] == color)
					open.add(t + columns);
				if (tr > 0 && board[tr - 1][tc] == color)
					open.add(t - columns);
				if (tc < columns - 1 && board[tr][tc + 1] == color)
					open.add(t + 1);
				if (tc > 0 && board[tr][tc - 1] == color)
					open.add(t - 1);
			}
		}
		return picked;
	}

	public static int[][] getACopyOfTheBoard(int[][] b) {
		int[][] cb = new int[b.length][b[0].length];
		for (int i = 0; i < b.length; i++)
			for (int j = 0; j < b[0].length; j++)
				cb[i][j] = b[i][j];

		return cb;
	}

	public static int _pickNeighbours(int[][] board, int r, int c,
			Collection<Integer> nodes) {
		int picked = 0;
		int color = board[r][c];
		if (color != -1) {
			List<Integer> open = new ArrayList<Integer>();
			open.add(r * columns + c);
			while (!open.isEmpty()) {
				int t = open.remove(0);
				int tr = t / columns;
				int tc = t % columns;
				if (board[tr][tc] != -1) {
					picked++;
					board[tr][tc] = -1;
				}
				if (tr < rows - 1 && board[tr + 1][tc] == color) {
					open.add(t + columns);
					nodes.remove(t + columns);
				}
				if (tr > 0 && board[tr - 1][tc] == color) {
					open.add(t - columns);
					nodes.remove(t - columns);
				}
				if (tc < columns - 1 && board[tr][tc + 1] == color) {
					open.add(t + 1);
					nodes.remove(t + 1);
				}
				if (tc > 0 && board[tr][tc - 1] == color) {
					open.add(t - 1);
					nodes.remove(t - 1);
				}
			}
		}
		return picked;
	}

	@Override
	public int[] findMove(int[][] board, long myTime, long opTime) {
		int[] m = new int[2];
		int[][] board_copy = new int[rows][columns];
		if (myTime > 1000000000l) {
			count++;
			// if(true){
			Map<Integer, Integer> scoreMap = new HashMap<Integer, Integer>();
			Map<Integer, Integer> opponentScore = new HashMap<Integer, Integer>();

			Collection<Integer> nodes = new ArrayList<Integer>();
			int node = 0;
			int nr = 0;
			int nc = 0;
			int minH = rows * columns;
			for (int i = 0; i < rows * columns; i++) {
				if (board[i / columns][i % rows] != -1)
					nodes.add(i);
				board_copy[i / columns][i % rows] = board[i / columns][i % rows];
			}
			while (!nodes.isEmpty()) {
				node = ((ArrayList<Integer>) nodes).remove(0);
				nr = node / columns;
				nc = node % columns;
				List<Integer> open = new ArrayList<Integer>();
				open.add(node);
				int p = _pickNeighbours(board, nr, nc, nodes);
				minH = Math.min(minH, p);
				scoreMap.put(node, p);
			}
			int r;
			int c;
			int[][] new_board;
			int nm = 0;
			int maxH = -rows * columns;

			for (int n : scoreMap.keySet()) {
				r = n / columns;
				c = n % columns;
				int myScore = scoreMap.get(n);
				if (myScore == minH) {
					continue;
				}
				board = getACopyOfTheBoard(board_copy);
				new_board = _moveBoard(board, r, c);
				int p = _pickMax(new_board, myScore);
				opponentScore.put(n, p);
				int diff = (myScore * myScore) - (p * p);
				if (maxH < diff) {
					maxH = diff;
					nm = n;
				}

			}

			m[0] = nm / columns;
			m[1] = nm % columns;
		} else if (myTime < 1000000l) {

			Map<Integer, Integer> scoreMap = new HashMap<Integer, Integer>();
			Collection<Integer> nodes = new ArrayList<Integer>();
			int node = 0;
			int nr = 0;
			int nc = 0;
			for (int i = 0; i < rows * columns; i++) {
				if (board[i / columns][i % rows] != -1)
					nodes.add(i);
			}
			while (!nodes.isEmpty()) {
				node = ((ArrayList<Integer>) nodes).remove(0);
				nr = node / columns;
				nc = node % columns;
				List<Integer> open = new ArrayList<Integer>();
				open.add(node);
				int p = _pickNeighbours(board, nr, nc, nodes);
				scoreMap.put(node, p);
			}

			int maxP = 0;
			int nm = 0;
			for (int p : scoreMap.values())
				maxP = Math.max(maxP, p);
			for (int n : scoreMap.keySet())
				if (scoreMap.get(n) == maxP) {
					nm = n;
					break;
				}
			m[0] = nm / columns;
			m[1] = nm % columns;
		} else {

			m[0] = -1;
			while (m[0] == -1) {
				int r = rnd.nextInt(rows);
				int c = rnd.nextInt(columns);
				if (board[r][c] != -1) {
					m[0] = r;
					m[1] = c;
				}
			}

		}
		return m;
	}
}
