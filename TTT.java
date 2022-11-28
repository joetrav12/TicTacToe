import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class TTT {

	protected static String empty = "empty";
	protected static String userSymbol;
	protected static String cpuSymbol;
	protected static Random rando = new Random();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Would you like to be Xs or Os?");
		boolean cpuTurn = true;
		
		while (true) {
			String s = scanner.nextLine();
			
			if (s.equals("x")) {
				userSymbol = "cross";
				cpuSymbol = "circle";
				cpuTurn = false;
				break;
			}
			else if (s.equals("o")) {
				userSymbol = "circle";
				cpuSymbol = "cross";
				break;
			}
			else {
				System.err.println("Please input either 'x' or 'o'.");
			}
		}
		
		String [] board = new String[9];
		for (int i = 0; i < board.length; i++) {
			board[i] = empty;
		}
		
		int turnCount = 0;
		boolean gameOver = false;
		while (!gameOver) {
			System.out.println("Board after " + turnCount + " turns:");

			//Printing out the board:
			for (int i = 0; i < board.length; i++) {
				if (board[i] == empty) {
					System.out.print("-");
				}
				if (board[i] == "cross") {
					System.out.print("x");
				}
				if (board[i] == "circle") {
					System.out.print("o");
				}
				if ((i + 1) % 3 == 0) {
					System.out.println();
				}
			}
			System.out.println();
			
			//If the game is tied or the user has won or the CPU has won:
			if (turnCount == 9 || checkWin(userSymbol, board) || checkWin(cpuSymbol, board)) {
				if (turnCount == 9 && !checkWin(userSymbol, board) && !checkWin(cpuSymbol, board)) {
					System.out.println("Tie game.");
				}
				if (checkWin(userSymbol, board)) {
					System.out.println("You win!");
				}
				if (checkWin(cpuSymbol, board)) {
					System.out.println("CPU wins.");
				}
				System.out.println();
				
				//Start a new game:
				System.out.println("Would you like to be Xs or Os?");
				String s = scanner.nextLine();
				
				while (true) {
					s = scanner.nextLine();
					
					if (s.equals("x")) {
						userSymbol = "cross";
						cpuSymbol = "circle";
						cpuTurn = false;
						break;
					}
					else if (s.equals("o")) {
						userSymbol = "circle";
						cpuSymbol = "cross";
						break;
					}
					else {
						System.err.println("Please input either 'x' or 'o'.");
					}
				}
				System.out.println();
				
				//Reset board and turnCount:
				for (int i = 0; i < board.length; i++) {
					board[i] = empty;
				}

				turnCount = 0;
			}
			else {
				if (cpuTurn) {
					//get bestMove of all potential moves on the board:
					board[bestMove(board)] = cpuSymbol;
					cpuTurn = false;
				}
				else {
					int x = 0;
					System.out.println("In which space on the board would you like to go?");

					while (x == 0) {
						try {
							int y = 0;
							
							while (y == 0) {
								int userMove = scanner.nextInt() - 1;

								if (userMove < 0 || userMove > 8) {
									System.err.println("Space number must be between 1 and 9.");
									y = 0;
								}
								else if (board[userMove] != empty) {
									System.err.println("Space filled.");
									y = 0;
								}
								else {
									board[userMove] = userSymbol;
									break;
								}
							}

							break;
						}
						catch (InputMismatchException e) {
							System.err.println("Not an integer.");
							x = 0;
							scanner.nextLine();
						}
					}

					cpuTurn = true;
				}
				
				turnCount++;
			}
		}
		
		scanner.close();
	}

	public static int minimax(String [] board, boolean maximizing, int alpha, int beta) {
		//if we've reached a terminal state where CPU has won:
		if (checkWin(cpuSymbol, board)) {
			return 1;
		}
		//if we've reached a terminal state where user has won:
		if (checkWin(userSymbol, board)) {
			return -1;
		}
		//if we've reached a terminal state where it's a tie:
		if (isFull(board)) {
			return 0;
		}

		if (maximizing) {
			int bestScore = Integer.MIN_VALUE;

			for (int i = 0; i < board.length; i++) {
				//if position is open to make a move:
				if (board[i] == empty) {
					//make the move:
					board[i] = cpuSymbol;
					//keep calling minimax and switching between maximizing and minimizing player, and then comparing that value to bestScore:
					bestScore = getMax(bestScore, minimax(board, false, alpha, beta));
					//set alpha to max:
					alpha = getMax(alpha, bestScore);
					//undo the move:
					board[i] = empty;
					
					//stop searching if beta <= alpha:
					if (beta <= alpha) {
						break;
					}
				}
			}

			return bestScore;
		}
		else {
			int worstScore = Integer.MAX_VALUE;

			for (int i = 0; i < board.length; i++) {
				if (board[i] == empty) {
					board[i] = userSymbol;
					worstScore = getMin(worstScore, minimax(board, true, alpha, beta));
					//set beta to min:
					beta = getMin(beta, worstScore);
					board[i] = empty;
					
					if (beta <= alpha) {
						break;
					}
				}
			}
			
			return worstScore;
		}
	}
	
	public static int bestMove(String [] board) {
		int bestScore = Integer.MIN_VALUE;
		int move = 0;
		
		//I want to introduce a bit of randomness to this function so that the AI does not just go in position 1 every time:
		int random = rando.nextInt(2);
		
		for (int i = 0; i < board.length; i++) {
			if (board[i] == empty) {
				//make the move:
				board[i] = cpuSymbol;
				
				//If the AI can make a move to win immediately, I'd like it to do that (as opposed to keeping the user at bay and winning later).
				if (checkWin(cpuSymbol, board)) {
					board[i] = empty;
					return i;
				}
				
				//get score of each potential move:
				int moveScore = minimax(board, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
				//undo the move:
				board[i] = empty;
		
				//find move with max score:
				if (random == 0) {
					if (moveScore > bestScore) {
						bestScore = moveScore;
						move = i;
					}
				}
				else {
					if (moveScore >= bestScore) {
						bestScore = moveScore;
						move = i;
					}
				}
			}
		}
		
		return move;
	}

	public static boolean checkWin(String symbol, String [] board) {
		//ways a player can win:
		if (board[0] == symbol && board[1] == symbol && board[2] == symbol) {
			return true;
		}
		if (board[3] == symbol && board[4] == symbol && board[5] == symbol) {
			return true;
		}
		if (board[6] == symbol && board[7] == symbol && board[8] == symbol) {
			return true;
		}
		if (board[0] == symbol && board[3] == symbol && board[6] == symbol) {
			return true;
		}
		if (board[1] == symbol && board[4] == symbol && board[7] == symbol) {
			return true;
		}
		if (board[2] == symbol && board[5] == symbol && board[8] == symbol) {
			return true;
		}
		if (board[0] == symbol && board[4] == symbol && board[8] == symbol) {
			return true;
		}
		if (board[2] == symbol && board[4] == symbol && board[6] == symbol) {
			return true;
		}

		return false;
	}

	public static boolean isFull(String [] board) {
		for (int i = 0; i < board.length; i++) {
			if (board[i] == empty) {
				return false;
			}
		}

		return true;
	}

	public static int getMax(int a, int b) {
		if (a > b) {
			return a;
		}
		else {
			return b;
		}
	}

	public static int getMin(int a, int b) {
		if (a < b) {
			return a;
		}
		else {
			return b;
		}
	}
}