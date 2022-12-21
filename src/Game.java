// Import the scanner to continuously take in user input for moves
import java.util.Scanner;
// The Game class is the main class for running the chess game
public class Game {
    // In the main function, create a new Board called board and print the board
    public static void main(String[] args) {
        Board board = new Board();
        board.setup_board();
        board.print_board();
        // Chess starts with white to move
        boolean white_to_move = true;
        // Import the scanner called input_scanner
        Scanner input_scanner = new Scanner(System.in);
        // Initialize the move string
        String move = "";
        // The main while loop of the game, check to see if either player resigns
        while (!move.equalsIgnoreCase("resign")) {
            // Print whose move it is to notify the players
            if (white_to_move){
                System.out.println("White to move");
            }
            else{
                System.out.println("Black to move");
            }
            // Update the move string to the user input
            move = input_scanner.nextLine();
            // If the move is "resign", continue the while loop which will break it
            if (move.equalsIgnoreCase("resign")){
                continue;
            }
            // Get the result from getMoveFromInput in order to see if the move is valid
            int[] result = board.getMoveFromInput(move, white_to_move);
            // If the result is null, continue the while loop
            if (result == null){
                continue;
            }
            // If the move is valid:
            if(board.moveIsValid(result, white_to_move)) {
                // Store everything about the position (castling array, the position, the positions of the kings)
                // This is done in case the move entered puts/leaves the king in check
                boolean[] castling = board.castling.clone();
                Piece[][] previous = new Piece[8][8];
                int[] previous_black_king = board.black_king.clone();
                int[] previous_white_king = board.white_king.clone();
                for (int i = 0; i < previous.length; i++){
                    previous[i] = board.position[i].clone();
                }
                // Now make the move on the board
                board.makeMove(result);
                // If the board is in check now, alert that the game is in check and reset the position to previous
                if (board.checkIsCheck(board,white_to_move)){
                    System.out.println("Illegal Move: You're in Check!");
                    board.position = previous;
                    board.black_king = previous_black_king;
                    board.white_king = previous_white_king;
                    white_to_move = !white_to_move;
                    board.castling = castling;
                }
                // Once a move has been completed, print the new position and update whose move it is
                board.print_board();
                white_to_move = !white_to_move;
            }
            // If the move is immediately not valid, alert the user
            else {
                System.out.println("Invalid Move Entered");
            }
        }
        // Finally, once a player has resigned, declare the winner based on whose turn it was
        if (white_to_move){
            System.out.println("Black Wins!");
        }
        else{
            System.out.println("White Wins!");
        }
    }
}