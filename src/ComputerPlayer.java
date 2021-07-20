public class ComputerPlayer {

    private final Model model;

    // Pass through the model object so it can be referenced within this class

    ComputerPlayer(Model model){
        this.model = model;
    }

    public int randomMove(){

        // Note this will never be called if the board is full due to validation in Controller, otherwise an infinite loop would occur

        int nrCols = model.getNrCols();

        int randomCol = (int) (Math.random() * nrCols); // Select a random column (cast the random float to an int)
        System.out.println("random number is " + randomCol);

        while(model.getHighestDisc(randomCol) == 0){ // Whilst a column is full, select another column
            randomCol = (int) (Math.random() * nrCols);
        }

        return randomCol;
    }

    public int playForWin(){
        // If a winning move is available, then the cpu shall play it
        // If the opposite player can win in a single move, it shall prevent a win
        // otherwise, play randomly

        if(winningMove() != -1){
            return winningMove();
        } else if (preventLosingMove() != -1){
            return preventLosingMove();
        } else {
            return randomMove();
        }
    }

    public int winningMove(){
        // Returns the number of the column if a winning move can be made there
        // If no winning moves exist, return -1.

        // Loop through each column, try the move and if we get a win then stop.
        // Otherwise undo moves
        int nrCols = model.getNrCols();

        for(int c = 0; c < nrCols; c++){
            if(model.isMoveValid(c)){      // If the move is valid, try the move
                model.makeMove(c);
                if(model.isGameWon(c)){
                    model.undoMove(c);     // undo the move and return the column which gives a winning move
                    return c;
                }
                model.undoMove(c);         // undo move if it did not grant a win
            }
        }
        return -1;
    }

    public int preventLosingMove(){
        // If it can prevent a losing move, return the column of the losing move
        // Otherwise return -1
        int nrCols = model.getNrCols();

        for (int c = 0; c < nrCols; c++){
            model.reverseTurn();           // play as opponent (as we want to see if the opponent is about to win)
            if(model.isMoveValid(c)) {
                model.makeMove(c);
                if(model.isGameWon(c)){
                    model.undoMove(c);     // undo the move and return the column which gives a winning move
                    model.reverseTurn();   // reverse the turn so it remains the cpu turn
                    return c;
                }
                model.undoMove(c);         // undo move if it did not grant a win
            }
            model.reverseTurn();           // reverse the turn so it remains the cpu turn
        }
        return -1;
    }
}

