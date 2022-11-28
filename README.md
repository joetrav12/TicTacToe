# TicTacToe
Tic-tac-toe AI.

---

### TTT.java

Prints out the board and takes user input. Alternates between user and CPU turn, and uses minimax function to get CPU's best move. More on the minimax function:

The minimax function takes the tic-tac-toe board, a boolean representing whether or not it is the CPU’s turn within the search algorithm, and integers alpha and beta as agrguments. This is a recursive function, so we also need several conditionals at the beginning. They check if the CPU has won, the user has won or the game has ended in a tie, respectively.
 Next is the recursive part. For both the maximizing and minimizing player, we have to look at all of the potential moves. For the maximizing player, we first create an integer called bestScore and set it equal to -infinity. Then we iterate through all the potential moves. For each potential move, we put the CPU’s symbol (whether it is a cross or a circle) in the board at the position of said move. We then set bestScore to the maximum of itself and another call of the minimax function. This is why we initially set bestScore equal to -infinity; the call of the minimax function will eventually return -1, 0, or 1, and all of these are greater than -infinity. Thus, the score of the first potential move will always be greater than and therefore replace the initial bestScore.
We also use alpha/beta pruning. This means we set the integer alpha to the maximum of itself and bestScore. We then have to check if integer beta is less than alpha. This prunes the tree and shortens the search algorithm. Essentially, alpha and beta keep track of the current maximum and minimum. The values of the new branches are compared to alpha and beta, and if the branches do not affect the outcome, they are pruned.
  
Finally, as shown above, we need to undo the move we made on the board at position i.
For the minimizing player, much of the code is the same. We just need to change a few things, included setting the initial variable (worstScore) to +infinity and getting the minimum of bestScore and the call of minimax.
The minimax function searches to the terminal node from each potential move and returns either -1, 0, or 1. These numbers mean that were the CPU to mark a position with a corresponding move score of +1 AND were the user to play perfectly, then the CPU would still win. Thus, we need to create a function called bestMove to find the highest move score of all potential moves.
In bestMove, we create a variable called best score and set it equal to -infinity. We then iterate through each potential move and set a variable called moveScore equal to a call of minimax on the board after we put CPU’s symbol at the position of said potential move. Once we get the moveScore, we undo the move, and then set it equal to the bestScore if it is greater than bestScore.
The reason we use the random variable is because we don’t want the CPU to just go in position 1 every time it starts. The moveScore of position 1 is at least greater than or equal to many other potential first moves, so we need to use the random variable to let the CPU go in different positions with the same moveScore.
After bestMove is finished, we use a conditional to ensure that if the CPU can make a move to win the game immediately, it will do so, instead of keeping the user at bay and winning the game eventually.

---

To compile, cd into TicTacToe-master and run "javac TTT.java".

To execute, run "java TTT".
