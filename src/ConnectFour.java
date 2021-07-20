public final class ConnectFour
{

	public static void main(String[] args)
	{
		// Creates a model representing the state of the game.
		Model model = new Model();
		
		// This text-based view is used to communicate with the user.
		// It can print the state of the board and handles user input.
		TextView view = new TextView();

		// Class which allows the computer to play
		ComputerPlayer computer = new ComputerPlayer(model);
		
		// The controller facilitates communication between model and view.
		// It also contains the main loop that controls the sequence of events.
		Controller controller = new Controller(model, view, computer);
		
		// Start a new session.
		controller.startSession();
	}
}
