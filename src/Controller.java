public final class Controller
{
	private final Model model;
	private final TextView view;
	private final ComputerPlayer computer;
	
	public Controller(Model model, TextView view, ComputerPlayer computer){
		this.model = model;
		this.view = view;
		this.computer = computer;
	}

	public void startSession(){

		// This begins a session by asking for user input
		// essentially a start menu appears, prompting the user to enter a command

		boolean endOfSession = false;

		while(!endOfSession){
			char decision = view.askForDecision();
			switch(decision){
				case 'n':
					model.wipeBoard();
					twoPlayerGameCommand();
					break;

				case 'e':
					// End game command
					System.out.println("Session ending");
					endOfSession = true;
					break;

				case 'r':
					resizeBoardCommand();
					break;

				case 'x':
					connectXCommand();
					break;

				case 'c':
					model.wipeBoard();
					playAgainstComputerCommand();
					break;

				case 's':
					saveBoardCommand();
					break;

				case 'l':
					loadBoardCommand();
					break;

				default:
					System.out.println("Invalid char, please try again.");
			}
		}
	}

	public void twoPlayerGame(){

		// This activates a game of connectFour between two human players

		view.displayNewGameMessage();
		view.displayBoard(model);

		boolean endOfGame = false;

		while(!endOfGame){
			boolean playerOneTurn = model.getTurn();

			String playerString;
			if(playerOneTurn){playerString = "Player 1";} else {playerString = "Player 2";}

			int input = playerMove();                // Get the player's input
			int nextMove = input - 1;

			// If a user enters a negative number, they want to end the game (break while loop)
			if(input <= -1){
				endOfGame = true;
				break;
			}

			// Raise a message indicating the move
			System.out.println(playerString + " places a disc in column " + input);

			// Make the move and show the board
			model.makeMove(nextMove);
			view.displayBoard(model);

			endOfGame = checkEndgame(nextMove, playerString);

		}

		// Once we exit the while loop, the game has ended and we will be sent back to the startSession while loop
		System.out.println("Game over");
	}

	public void gameAgainstComputer(){

		//This activates a human vs cpu game of connectFour

		view.displayNewGameMessage();
		view.displayBoard(model);

		boolean endOfGame = false;

		while(!endOfGame){
			boolean playerOneTurn = model.getTurn();
			String playerString;
			if(playerOneTurn){playerString = "You";} else {playerString = "The computer";}

			int nextMove;
			if(playerOneTurn){

				// Ask user for the next move, subtract 1 since humans count from 1, whereas the array counts from 0.
				int input = playerMove();
				nextMove = input - 1;

				// If a user enters a negative number, they want to end the game.
				if(input <= -1){
					System.out.println("Player entered negative number, exiting game...");
					break;
				}

			} else {
				nextMove = computer.playForWin();
			}

			// Once move has been validated, raise a message indicating the move
			System.out.println(playerString + " places a disc in column " + (nextMove + 1));

			model.makeMove(nextMove);
			view.displayBoard(model);

			endOfGame = checkEndgame(nextMove, playerString);

		}

		System.out.println("Game over");
	}

	// Helper methods
	// These are methods which help the control flow of the games of connectFour

	public int playerMove(){

		// Asks user for the next move, if negative then returns instantly, otherwise validates move
		int input = view.askForMove();

		if(input < 0){return input;} // Negative number implies player would like to quit game

		// Whilst the move is invalid, keep asking for a valid move (move = input - 1 (since humans count from 1))
		while(!model.isMoveValid(input - 1)){
			if(input < 0){ return input; }
			System.out.println("Column is either full or out of bounds, please try again: ");
			input = view.askForMove();
		}

		return input;
	}

	public boolean checkEndgame(int move, String playerString){

		// Given a move, check whether a player has reached the end of the game

		if(model.isGameWon(move)){
			System.out.println(playerString + " has won the game!");
			model.wipeBoard(); // This is done so a user cannot save a won game after the game ends
			return true;
		} else if (model.isBoardFull()){
			System.out.println("Board is full, game has been drawn!");
			return true;
		}

		return false;
	}

	// Command methods
	// These are all methods which execute code when a command is given by the user

	public void twoPlayerGameCommand(){
		// Two player game command
		System.out.println("Playing a 2 player game of Connect-" + model.getNumberToWin());
		System.out.println("There is " + model.getNrRows() + " rows and " + model.getNrCols() + " columns");
		System.out.println("New game beginning...");
		twoPlayerGame();
	}

	public void resizeBoardCommand(){
		// Resize board command
		System.out.println("Resizing...");

		System.out.println("Row dimensions ");
		int rows = view.askForSize();

		// If user tries to have less rows than the number of discs it takes to win, then we ask them to try again
		// This also handles the case where dimensions are chosen to be 0 or negative
		while(rows < model.getNumberToWin()){
			System.out.println("Not enough rows to win, please enter a dimension over or equal to " + model.getNumberToWin());
			System.out.println("Row dimensions ");
			rows = view.askForSize();
		}

		System.out.println("Column dimensions ");
		int cols = view.askForSize();

		// Validate column number choice as well
		while(cols < model.getNumberToWin()){
			System.out.println("Not enough columns to win, please enter a dimension over or equal to " + model.getNumberToWin());
			System.out.println("Column dimensions ");
			cols = view.askForSize();
		}

		model.setRows(rows);
		model.setCols(cols);
	}

	public void connectXCommand(){
		// Change number of discs required in a row
		System.out.println("How many in a row would you like for a win? ");
		int discsToWin = view.askForNum();

		// Input validation, if number of discs > nrRows, nrCols then it would be impossible to win
		while(discsToWin > model.getNrCols() || discsToWin > model.getNrRows()){
			System.out.println("Number of discs to win is greater than rows or columns");
			System.out.println("Enter a number less than the smallest dimension, rows: " + model.getNrRows() + " cols: " + model.getNrCols());
			discsToWin = view.askForNum();
		}

		while(discsToWin < 2){
			System.out.println("Need at least 2 or more discs in a row for a game to work.");
			System.out.println("Please enter a valid number greater than or equal to 2");
			discsToWin = view.askForNum();
		}

		model.setNumberToWin(discsToWin);
	}

	public void playAgainstComputerCommand(){
		// Play against the computer
		System.out.println("Playing a game of Connect-" + model.getNumberToWin() + " against the computer");
		System.out.println("There is " + model.getNrRows() + " rows and " + model.getNrCols() + " columns");
		System.out.println("New game beginning...");
		gameAgainstComputer();
	}

	public void saveBoardCommand(){

		int[][] board = model.getBoard();
		String[] rows = new String[model.getNrRows()];

		System.out.println("Creating a file...");
		String filename = view.askForFilename(); // Ask user to choose and create file
		FileHandling.createFile(filename);

		// Convert the board into a 1D array of Strings where each String is a line
		// Note that += is causing concatenation of strings, not the addition of numbers
		for(int r = 0; r < model.getNrRows(); r++){
			String row = "";
			for(int c = 0; c < model.getNrCols(); c++){
				if(board[r][c] == -1){
					row += 2; // -1 would write two characters to the file, which is problematic, so we write 2 instead
				} else{
					row += board[r][c];
				}
			}
			rows[r] = row;
		}

		// To write all the existing files to validFilesnames, we need an array of all the filenames
		String[] existingFilenames = FileHandling.readFile("validFilenames", FileHandling.numberOfLinesInFile("validFilenames"));
		String[] allFilenames = new String[existingFilenames.length + 1];

		for(int i = 0; i < existingFilenames.length; i++){
			allFilenames[i] = existingFilenames[i];
		}
		allFilenames[allFilenames.length -1] = filename;

		FileHandling.writeToFile(filename, rows); // save the board
		FileHandling.writeToFile("validFilenames", allFilenames); // save the filenames
	}

	public void loadBoardCommand(){
		int numberOfLines = model.getNrRows();
		model.setNumberOfPieces(0);

		System.out.println("Loading a file...");
		System.out.println("Valid files to load: ");
		String[] filenames = FileHandling.readFile("validFilenames", FileHandling.numberOfLinesInFile("validFilenames"));
		for(String name : filenames){
			System.out.println(name);
		}
		System.out.println();

		String filename = view.askForFilename();

		// Go back to main menu when invalid filename is entered.
		if(!FileHandling.validFilename(filename)){
			System.out.println("Invalid filename");
			return;
		}

		String[] rows = FileHandling.readFile(filename, numberOfLines);
		int numberOfPlayerOneDiscs = 0;
		int numberOfPlayerTwoDiscs = 0;

		// Fill the board back up, using information from the file
		for(int r = 0; r < model.getNrRows(); r++){
			String row = rows[r];
			for(int c = 0; c < model.getNrCols(); c++){
				char character = row.charAt(c);
				if(character == '0'){
					model.setBoard(r, c, Model.emptySpace);
				} else if (character == '1'){
					numberOfPlayerOneDiscs++;
					model.setBoard(r, c,Model.playerOne);
				} else if (character == '2'){
					numberOfPlayerTwoDiscs++;
					model.setBoard(r,c,Model.playerTwo);
				}
			}
		}

		model.setNumberOfPieces(numberOfPlayerOneDiscs + numberOfPlayerTwoDiscs);

		// In this case its player two's turn. Since model always begins with player one turn
		// we reverse the turn
		if(numberOfPlayerOneDiscs > numberOfPlayerTwoDiscs){
			model.reverseTurn();
		}

		System.out.println("Board state loaded");

		System.out.println("Would you like to load this game against computer (c) or another human (n)?");
		char decision = InputUtil.readCharFromUser();
		while(decision != 'c' && decision != 'n'){
			System.out.println("Please either enter (c) for computer or (n) for another human?");
			decision = InputUtil.readCharFromUser();
		}

		if(decision == 'c'){
			playAgainstComputerCommand();
		} else {
			twoPlayerGameCommand();
		}

	}
}


