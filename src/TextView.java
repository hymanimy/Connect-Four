public final class TextView
{
	public TextView()
	{
	
	}
	
	public final void displayNewGameMessage()
	{
		System.out.println("---- NEW GAME STARTED ----");
	}
	
	public final int askForMove() {
		System.out.print("Select a free column (columns begin at 1, enter a negative number if you would like to quit): ");
		return InputUtil.readIntFromUser();
	}

	public final int askForSize(){
		System.out.print("Select a dimension: ");
		return InputUtil.readIntFromUser();
	}

	public final char askForDecision(){
		System.out.print("Enter a command (n: new game, e: end session, r: resize board, x: change game to Connect-X, c: game against computer, s: save previous game, l: load game from file)?: ");
		return InputUtil.readCharFromUser();
	}

	public final String askForFilename(){
		System.out.print("Please enter a filename (do not type .txt): ");
		return InputUtil.readStringFromUser();
	}

	public final int askForNum(){
		System.out.print("Select a number: ");
		return InputUtil.readIntFromUser();
	}
	
	public final void displayBoard(Model model) {

		// Get the board representation.
		int nrRows    = model.getNrRows();
		int nrCols    = model.getNrCols();
		int[][] board = model.getBoard();

		// Values which represent type of each space within the 2d array
		int emptySpace     =  0;
		int playerOneSpace =  1;
		int playerTwoSpace = -1;

		// Characters which represent each type of space
		String emptyChar     = "   ";
		String playerOneChar = " A ";
		String playerTwoChar = " B ";

		String rowDivider = " - ".repeat(nrCols);

		// Print a row divider per row, then print the contents of each row
		System.out.println(rowDivider);

		// Since we loop through the rows forward, graphically the top row is board[0], bottom row is board[nrCols -1]
		for(int[] row : board){

			// Print contents of the row
			for(int i = 0; i < row.length; i++){
				if(row[i] == playerOneSpace){
					System.out.print(playerOneChar);
				}
				else if(row[i] == playerTwoSpace){
					System.out.print(playerTwoChar);
				}
				else {
					System.out.print(emptyChar);
				}
			}

			System.out.println("\n" + rowDivider);

		}

		// Add the number of the each column at the bottom
		for(int i = 1; i <= nrCols; i++){
			System.out.print(" " + i + " ");
		}
		System.out.println();
	}
}
