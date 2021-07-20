public final class Model
{
	// ===========================================================================
	// ================================ CONSTANTS ================================
	// ===========================================================================
	// The most common version of Connect Four has 7 rows and 6 columns.
	// I have swapped the numbers given above, in accordance with the pdf
	public static final int DEFAULT_NR_ROWS = 6;
	public static final int DEFAULT_NR_COLS = 7;
	public static final int DEFAULT_NUMBER_TO_WIN = 4;

	// Numbers which represent empty spaces and player discs within the 2D board array
	public static final int emptySpace = 0;
	public static final int playerOne = 1;
	public static final int playerTwo = -1;
	
	// ========================================================================
	// ================================ FIELDS ================================
	// ========================================================================
	// The size of the board.
	private int nrRows;
	private int nrCols;

	// The number of pieces in a row required for a win
	private int numberToWin;

	// Board is a 2D array of strings, currently all set to null
	private int[][] board;

	// Number of pieces currently held in the board
	private int numberOfPieces;

	// Boolean for keeping track of whose turn it is, initially player 1
	private boolean playerOneTurn = true;

	// =============================================================================
	// ================================ CONSTRUCTOR ================================
	// =============================================================================
	public Model() {
		// Initialise the board size to its default values.
		nrRows = DEFAULT_NR_ROWS;
		nrCols = DEFAULT_NR_COLS;
		numberToWin = DEFAULT_NUMBER_TO_WIN;

		wipeBoard();
	}

	public void wipeBoard(){
		// Resets the board and makes player one player first
		// Note if the dimensions of the board are changed, if a game is restarted then the same dimensions will remain

		numberOfPieces = 0;
		playerOneTurn = true;
		board = new int[nrRows][nrCols];

		// Fill the board with empty spaces
		for(int r = 0; r < nrRows; r++){
			for(int c = 0; c < nrCols; c++){
				board[r][c] = emptySpace;
			}
		}
	}

	// ====================================================================================
	// ================================ MODEL INTERACTIONS ================================
	// ====================================================================================
	public boolean isMoveValid(int col) {
		// Ensures the column is within the bounds of the array
		if(col < 0 || col > nrCols - 1){
			return false;
		}

		// If the top is empty, then the column has space
		return board[0][col] == emptySpace;
	}
	
	public void makeMove(int col) {
		int firstEmptyRow = -1;

		// Loop through each row, from base up
		for(int r = nrRows - 1; r >= 0; r--){
			// If the current cell is empty, then that is our first empty row and we can exit loop
			if(board[r][col] == emptySpace){
				firstEmptyRow = r;
				break;
			}
		}

		// Actually place move
		if(playerOneTurn){board[firstEmptyRow][col] = playerOne;} else {board[firstEmptyRow][col] = playerTwo;}

		// Swap turn by negating boolean
		playerOneTurn = !playerOneTurn;

		numberOfPieces++;

	}

	public void undoMove(int col){
		// Undoes the most recent move in a column

		// if we make a move, the turn switches. If we want to undo the move, then we must switch the turn again
		playerOneTurn = !playerOneTurn;

		// Loop from top down, first non-empty space becomes empty
		for(int r = 0; r < nrRows; r++){
			if(board[r][col] != emptySpace){
				board[r][col] = emptySpace;
				return;
			}
		}

		numberOfPieces--;
	}

	public boolean isBoardFull(){
		return nrRows * nrCols == numberOfPieces;
	}

	public int getHighestDisc(int col){
		// Gets the highest up disc in a column (the most recently dropped one)
		for(int r = 0; r < nrRows; r++){
			if(board[r][col] != emptySpace){
				return r;
			}
		}
		return nrRows - 1; // The entire column is empty in this case
	}

	// Win check methods

	public boolean isGameWon(int col){
		// Given a the column where the most recent piece was placed, this checks if a win has been reached
		// Note: this is much more efficient than doing a wincheck on every single piece
		// we only need to check the most recent piece, since that is the only area a win could have occurred

		int lastPieceRow = getHighestDisc(col);
		int lastPieceCol = col;

		if(board[lastPieceRow][lastPieceCol] == emptySpace){
			return false;
		}

		// We then check for the four types of win that can occur
		// Look inside horizontalWinCheck for the explanation on how the method operates.
		// The other winChecks are very similar in logic, just changing the direction in which they check
		// The main idea is to obtain an array of elements which are adjacent in a certain direction (vertical, horizontal or diagonal)
		// And then check if there are X many adjacent elements in the array (this is done by the "containsXInRow" method)
		return  horizontalWinCheck(lastPieceRow, lastPieceCol)   ||
				verticalWinCheck(lastPieceRow, lastPieceCol)     ||
				downDiagonalWinCheck(lastPieceRow, lastPieceCol) ||
				upDiagonalWinCheck(lastPieceRow, lastPieceCol);

	}

		// Note these are private since we never use them individually
		// They are only called within the isGameWon method, which is public since it's used elsewhere
	
	private boolean horizontalWinCheck(int row, int col){
		// Given a piece in the board (board[row][col]) check if that piece placed has caused a horizontal win

		int piece = board[row][col];

		// In a game of connect4 we need to check the three pieces to either side of recently dropped piece (as thats where a win could occur)
		// Therefore we check 7 adjacent pieces. In a game of connectX, check 2x-1 pieces for similar reasons.

		int[] pieces = new int[numberToWin*2 - 1];

		for(int i = 0; i < numberToWin*2-1; i++){

			// The reason for col - (x-1) is because we go back x-1 columns and then go forward by one by one using i
			if(inBounds(row, col - (numberToWin - 1) + i)){
				pieces[i] = board[row][col - (numberToWin - 1) + i];
			}
			else {
				pieces[i] = emptySpace; // If we have gone out the bounds of the board, just pretend the space is empty
			}

		}

		return containsXInRow(pieces, piece, numberToWin);
	}

	private boolean verticalWinCheck(int row, int col){
		int piece = board[row][col];

		int[] pieces = new int[numberToWin*2 - 1];
		for(int i = 0; i < numberToWin*2-1; i++){

			// Obtaining all pieces before and after the recently placed piece in the column, so we keep col constant
			if(inBounds(row - (numberToWin - 1) + i, col)){
				pieces[i] = board[row - (numberToWin - 1) + i][col];
			}
			else {
				pieces[i] = emptySpace;
			}

		}

		return containsXInRow(pieces, piece, numberToWin);
	}

	private boolean downDiagonalWinCheck(int row, int col){
		int piece = board[row][col];

		int[] pieces = new int[numberToWin*2 - 1];
		for(int i = 0; i < numberToWin*2-1; i++){
			// For down diagonal we go back on rows and columns and iterate over both
			if(inBounds(row - (numberToWin - 1) + i, col - (numberToWin - 1) + i)){
				pieces[i] = board[row - (numberToWin - 1) + i][col - (numberToWin - 1) + i];
			} else {
				pieces[i] = emptySpace;
			}
		}

		return containsXInRow(pieces, piece, numberToWin);
	}

	private boolean upDiagonalWinCheck(int row, int col){
		int piece = board[row][col];

		int[] pieces = new int[numberToWin*2 - 1];
		for(int i = 0; i < numberToWin*2-1; i++){

			// Here we go back x - 1 rows and then go forward one by one using i
			// and we go forward by x - 1 columns and then go backward one by one using -i

			if(inBounds(row - (numberToWin - 1) + i, col + numberToWin - 1 - i)){
				pieces[i] = board[row - (numberToWin - 1) + i][col + numberToWin - 1 - i];
			} else {
				pieces[i] = emptySpace;
			}
		}

		return containsXInRow(pieces, piece, numberToWin);
	}

	public boolean inBounds(int r, int c){
		// Method which checks whether a given row and column number are within the boards range
		return r >= 0 && r < nrRows && c >= 0 && c < nrCols;
	}

	// Static methods which help the winCheck methods

	public static int[] slice(int[] arr, int start, int end){
		// Given an array called arr, this method returns an array containing the elements indexed from start to end - 1
		// e.g. slice([7,8,9,0,1], 1, 3) == [8,9]

		int[] newArr = new int[end - start];
		for(int i = start; i < end; i++){
			newArr[i - start] = arr[i];
		}
		return newArr;
	}

	public static boolean containsXInRow(int[] arr, int elem, int x){
		// array called arr, an element to check for called elem, and a number of times called x.
		// This method returns true if elem appears consecutively x times within arr.

		// Take moving slices of length x from the array
		for(int i = 0; i < arr.length - x + 1; i++){

			int[] slicedArr = slice(arr, i, i+x);   // Take a slice of the original array
			boolean hasXInARow = true;                         // Assume the array has x elems in a row

			for(int e : slicedArr){
				if(e != elem){                           // counterexample => flag is false then break the inner loop
					hasXInARow = false;
					break;
				}
			}

			if(hasXInARow){return true;}
		}

		return false;
	}

	// =========================================================================
	// ================================ GETTERS ================================
	// =========================================================================

	public int getNrRows(){ return nrRows; }
	
	public int getNrCols() { return nrCols; }

	public int[][] getBoard(){ return board; }

	public boolean getTurn(){return playerOneTurn;}

	public int getNumberToWin(){return numberToWin;}

	// =========================================================================
	// ================================ SETTERS ================================
	// =========================================================================

	public void setRows(int r){ nrRows = r; }

	public void setCols(int c){ nrCols = c; }

	public void setNumberToWin(int n){ numberToWin = n; }

	public void setBoard(int r, int c, int value){
		board[r][c] = value;
	}

	public void setNumberOfPieces(int n){ numberOfPieces = n;}

	public void reverseTurn(){playerOneTurn = !playerOneTurn;}

}
