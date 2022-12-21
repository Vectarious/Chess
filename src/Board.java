// Import the scanner which scans for user input (in this case for promotion)
import java.util.Scanner;

// This is the board class which handles almost all the work
public class Board {
    // These are colors which are used to print the board to the console
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_BRIGHT_BLACK_BACKGROUND = "\u001B[100m";

    // Initialize the board (a 2x2 piece array)
    Piece[][] position;
    // These keep track of the coordinates of the kings to check if the position is in check
    int[] black_king;
    int[] white_king;
    // This is an array of booleans that keep track of whether castling is legal for all 4 possible ways of castling
    boolean[] castling;

    // Initialize the board, 8x8 piece array
    public Board(){
        position = new Piece[8][8];
    }
    // This is the function that prints the current position
    public void print_board(){
        // A nested for-loop to run through each square
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                // This first if statement prints the rank number on the left side
                if (j == 0){
                    System.out.print(" " + (8-i) + " ");
                }
                // If the position is null, print a white square if i + j is even and a black one if i + j is odd
                if (position[i][j] == null){
                    if ((i + j) % 2 == 0){
                        System.out.print(ANSI_WHITE_BACKGROUND + "   " + ANSI_RESET);
                    }
                    else{
                        System.out.print(ANSI_BRIGHT_BLACK_BACKGROUND + "   " + ANSI_RESET);
                    }
                }
                // If there's a piece, make the text color whatever color the piece is using .isWhite
                else {
                    String text_color;
                    if (position[i][j].isWhite) {
                        text_color = ANSI_BRIGHT_WHITE;
                    }
                    else {
                        text_color = ANSI_BLACK;
                    }
                    // Once the text color has been determined, print the piece on the board
                    if ((i + j) % 2 == 0){
                        System.out.print(ANSI_WHITE_BACKGROUND + text_color + position[i][j] + ANSI_RESET);
                    }
                    else{
                        System.out.print(ANSI_BRIGHT_BLACK_BACKGROUND + text_color + position[i][j] + ANSI_RESET);
                    }
                }
            }
            // Print a new line, so it starts the next line after the internal for-loop ends
            System.out.println();
        }
        // Finally, print the file letters on the bottom of the board
        System.out.println("    a  b  c  d  e  f  g  h ");
    }
    // This is essentially part of the game initializer, but I wanted to make it its own function here
    public void setup_board(){
        // Initialize the positions of the kings and set all castling components to true
        white_king = new int[]{4,7};
        black_king = new int[]{4,0};
        castling = new boolean[]{true,true,true,true};
        // Place all pieces on their starting squares
        position[7][0] = new Piece(true, PieceKind.ROOK);
        position[7][1] = new Piece(true, PieceKind.KNIGHT);
        position[7][2] = new Piece(true, PieceKind.BISHOP);
        position[7][3] = new Piece(true, PieceKind.QUEEN);
        position[7][4] = new Piece(true, PieceKind.KING);
        position[7][5] = new Piece(true, PieceKind.BISHOP);
        position[7][6] = new Piece(true, PieceKind.KNIGHT);
        position[7][7] = new Piece(true, PieceKind.ROOK);
        for (int i = 0; i < 8; i++)
        {
            position[6][i] = new Piece(true, PieceKind.PAWN);
        }
        position[0][0] = new Piece(false, PieceKind.ROOK);
        position[0][1] = new Piece(false, PieceKind.KNIGHT);
        position[0][2] = new Piece(false, PieceKind.BISHOP);
        position[0][3] = new Piece(false, PieceKind.QUEEN);
        position[0][4] = new Piece(false, PieceKind.KING);
        position[0][5] = new Piece(false, PieceKind.BISHOP);
        position[0][6] = new Piece(false, PieceKind.KNIGHT);
        position[0][7] = new Piece(false, PieceKind.ROOK);
        for (int i = 0; i<8; i++){
            position[1][i] = new Piece(false, PieceKind.PAWN);
        }
    }
    // This function takes in the user input from the Game class and translates
    // it into a move which my program can understand
    public int[] getMoveFromInput(String move, boolean white_to_move){
        // First, lowercase everything and get rid of white space in case the user isn't using proper formatting
        move = move.replaceAll("\\s+","").toLowerCase();
        // A couple special cases for castling, by default it moves the king over 2 in the correct direction
        if (move.equals("o-o")){
            if (white_to_move){
                return new int[] {4, 7, 6, 7};
            }
            else{
                return new int[] {4, 0, 6, 0};
            }
        }
        if (move.equals("o-o-o")){
            if (white_to_move){
                return new int[] {4, 7, 2, 7};
            }
            else{
                return new int[] {4, 0, 2, 0};
            }
        }
        // For all other moves, they need to contain exactly 5 characters with the middle being "-"
        if (move.length() != 5){
            System.out.println("Invalid Move");
            return null;
        }
        if (move.charAt(2) != '-'){
            System.out.println("Invalid Move");
            return null;
        }
        // Assuming it is correct formatting, translate from input notation to board notation
        char start_letter = move.charAt(0);
        int start_number = Integer.parseInt(Character.toString(move.charAt(1)));
        char end_letter = move.charAt(3);
        int end_number = Integer.parseInt(Character.toString(move.charAt(4)));

        int start_col = (start_letter) - 97;
        int start_row = 8 - start_number;
        int end_col = (end_letter) - 97;
        int end_row = 8 - end_number;
        // Error check to make sure the move is in bounds!
        if (start_col < 0 || start_col > 7 || start_row < 0 || start_row > 7 || end_col < 0 || end_col > 7 || end_row < 0 || end_row > 7){
            return null;
        }
        // If everything is okay, return the 4 components in an int array
        return new int[] {start_col, start_row, end_col, end_row};
    }
    // This was my way to work around running the scanner when the program checks if it's in check...
    // it's not really important, but basically I use this function for multiple purposes, and I need it to do
    // slightly different things each time
    public boolean moveIsValid(int[] moveComponents, boolean whiteToMove) {
        return moveIsValid(moveComponents,whiteToMove,false);
    }
    // This is the main function to determine if the move is valid
    public boolean moveIsValid(int[] moveComponents, boolean whiteToMove, boolean isAuto) {
        // It takes in the components created from getMoveFromInput function and finds the relevant pieces
        Piece selectedPiece = position[moveComponents[1]][moveComponents[0]];
        Piece endSquarePiece = position[moveComponents[3]][moveComponents[2]];
        // If the original square has nothing in it, this is not a valid move
        if (selectedPiece == null){
            return false;
        }
        // If you're trying to move a piece that isn't yours, this is not a valid move
        if (selectedPiece.isWhite != whiteToMove){
            return false;
        }
        // If you're trying to take your own piece, this is not a valid move
        if (endSquarePiece != null && selectedPiece.isWhite == endSquarePiece.isWhite){
            return false;
        }
        // I use the rowDifference and columnDifference all the time, so I decided to just make them variables
        int rowDifference = moveComponents[3] - moveComponents[1];
        int columnDifference = moveComponents[2] - moveComponents[0];
        // Check if selected piece exists. If it does, check that the end square contains a piece that is
        // the opposite color of the moving piece, this is also illegal
        if (selectedPiece == null || (endSquarePiece != null && (endSquarePiece.isWhite == selectedPiece.isWhite))) {
            return false;
        }
        // Finally, make sure a piece can't move to the same square it started on
        if (selectedPiece == endSquarePiece) {
            return false;
        }
        // Here are the cases for the actual pieces themselves
        switch (selectedPiece.pieceKind) {
            case ROOK: {
                // For the rook, either the rowDifference or columnDifference must be 0
                if (rowDifference == 0 || columnDifference == 0){
                    // Use the pathClear function below to finalize the validation and return true
                    if (pathClear(moveComponents)){
                        return true;
                    }
                }
                break;
            }
            case KNIGHT: {
                // The knight is fairly easy, just make sure there is 1 component with abs(1) and 1 component with abs(2)
                if ((Math.abs(rowDifference) == 1 && Math.abs(columnDifference) == 2) || (Math.abs(rowDifference) == 2 && Math.abs(columnDifference) == 1)){
                    return true;
                }
                return false;
            }
            case BISHOP: {
                // For the bishop, the rowDifference must equal the columnDifference
                if (Math.abs(rowDifference) == Math.abs(columnDifference)) {
                    // And the path must be clear!
                    if (pathClear(moveComponents)) {
                        return true;
                    }
                }
                break;
            }
            case QUEEN: {
                // The queen is simply the combination of the rook and bishop
                if ((rowDifference == 0 || columnDifference == 0) || (Math.abs(rowDifference) == Math.abs(columnDifference))) {
                    if (pathClear(moveComponents)) {
                        return true;
                    }
                }
                break;
            }
            case KING: {
                // The king is hard because of castling, but for standard moves just make sure it doesn't
                // move greater than ±1 in any direction
                if (Math.abs(rowDifference) <= 1 && Math.abs(columnDifference) <= 1)
                    return true;
                // Here we need to clone the move components to run pathClear on an altered component set
                int[] moveComps = moveComponents.clone();
                // If the king is castling (moving 2 squares in a direction), and it's not checking if it's check
                if (Math.abs(rowDifference) == 0 && Math.abs(columnDifference) == 2 && isAuto == false){
                    // For white, if it's going king-side (columnDifference == 2), check if the path is clear on that side
                    if (whiteToMove){
                        if (columnDifference == 2){
                            // Edit the moveComps to include until the end of the board
                            moveComps[0]+=1;
                            return (castling[0] && pathClear(moveComps));
                        }
                        else{
                            // Pretty much all of these work the same way just with slight variations. Just check
                            // the proper castling component and if the path is clear for the right moveComps
                            moveComps[0]-=1;
                            return (castling[1] && pathClear(moveComps));
                        }
                    }
                    else{
                        if (columnDifference == 2){
                            moveComps[0]+=1;
                            return castling[2]  && pathClear(moveComps);
                        }
                        else {
                            moveComps[0]-=1;
                            return castling[3]  && pathClear(moveComps);
                        }
                    }
                }
                break;
            }
            case PAWN: {
                // Pawn is the most complicated piece, and sometimes it errored without this validMove boolean
                boolean validMove = false;
                // For white pawns (since the direction is different, the colors follow different rules)
                if (selectedPiece.isWhite){
                    // Moves forward 1
                    if (rowDifference == -1 && endSquarePiece == null){
                        validMove = true;
                    }
                    // Moves forward 2
                    if (rowDifference == -2 && endSquarePiece == null && pathClear(moveComponents)) {
                        validMove = true;
                    }
                    // Capture diagonally
                    if (rowDifference == -1 && Math.abs(columnDifference) == 1 && endSquarePiece != null){
                        validMove = true;
                    }
                    // Check if promotion
                    if (moveComponents[3] == 0 && !isAuto && selectedPiece.pieceKind == PieceKind.PAWN && validMove){
                        // If it's promoting, use the scanner to take in what the user would like to promote to
                        Scanner promotion_scanner = new Scanner(System.in);
                        System.out.println("How would you like to promote?");
                        String piece = promotion_scanner.nextLine();
                        piece = piece.replaceAll("\\s+","").toLowerCase();
                        // Whatever piece the user wants, change the pieceKind to that kind
                        if (piece.equals("rook")) {
                            selectedPiece.pieceKind = PieceKind.ROOK;
                            return true;
                        }
                        else if (piece.equals("knight")){
                            selectedPiece.pieceKind = PieceKind.KNIGHT;
                            return true;
                        }
                        else if (piece.equals("bishop")) {
                            selectedPiece.pieceKind = PieceKind.BISHOP;
                            return true;
                        }
                        else if (piece.equals("queen")) {
                            selectedPiece.pieceKind = PieceKind.QUEEN;
                            return true;
                        }
                        // If the input isn't valid, return false
                        return false;
                    }
                }
                else{
                    // The same stuff here for black to move
                    // Moves forward 1
                    if (rowDifference == 1 && endSquarePiece == null){
                        validMove = true;
                    }
                    // Moves forward 2
                    if (rowDifference == 2 && endSquarePiece == null && pathClear(moveComponents)) {
                        validMove = true;
                    }
                    // Captures diagonally
                    if (rowDifference == 1 && Math.abs(columnDifference) == 1 && endSquarePiece != null){
                        validMove = true;
                    }
                    // Check if promotion
                    if (moveComponents[3] == 7 && !isAuto && selectedPiece.pieceKind == PieceKind.PAWN && validMove){
                        Scanner promotion_scanner = new Scanner(System.in);
                        System.out.println("How would you like to promote?");
                        String piece = promotion_scanner.nextLine();
                        piece = piece.toLowerCase();
                        if (piece.equals("rook")) {
                            selectedPiece.pieceKind = PieceKind.ROOK;
                            return true;
                        }
                        else if (piece.equals("knight")){
                            selectedPiece.pieceKind = PieceKind.KNIGHT;
                            return true;
                        }
                        else if (piece.equals("bishop")) {
                            selectedPiece.pieceKind = PieceKind.BISHOP;
                            return true;
                        }
                        else if (piece.equals("queen")) {
                            selectedPiece.pieceKind = PieceKind.QUEEN;
                            return true;
                        }
                        return false;
                    }
                }
                return validMove;
            }
        }
        return false;
    }
    // This function checks if the board has been left in check, making the move invalid
    public boolean checkIsCheck(Board board, boolean isWhiteTurn){
        if (isWhiteTurn){
            // If white is to move, check every square to see if it can take the white king using a double
            // for loop and the moveIsValid function (with isAuto set to true so promotion isn't triggered)
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (moveIsValid(new int[]{i,j,white_king[0],white_king[1]},!isWhiteTurn,true)){
                        return true;
                    }
                }
            }
        }
        else{
            // The same process for the black king if black is to move
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (moveIsValid(new int[]{i, j, black_king[0], black_king[1]}, !isWhiteTurn,true)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    // This function checks if the path is clear for the rooks, bishops, and queens, and also sometimes for pawns/castling
    public boolean pathClear(int[] moveComponents){
        // Make the various components into easier to understand variables
        int x1 = moveComponents[0];
        int y1 = moveComponents[1];
        int x2 = moveComponents[2];
        int y2 = moveComponents[3];
        int flip;
        if ((x1 - x2) == 0 || (y1 - y2) == 0){
            // For rooks/queens moving along y-axis (and pawns moving 2 squares)
            if ((x1 - x2) == 0){
                // Make y1 < y2
                if (y1 > y2){
                    flip = y1;
                    y1 = y2;
                    y2 = flip;
                }
                // Use this for loops to check each square in the position in this path (not including start/end squares)
                for (int i=1; i<(Math.abs(y1-y2)); i++){
                    // If it ever hits a non-null square, return false
                    if (position[y1 + i][x1] != null){
                        return false;
                    }
                }
            }
            // For rooks/queens moving along x-axis (and castling)
            else{
                // This is the same process as before, only the for loop increases with column instead of row
                if (x1 > x2){
                    flip = x1;
                    x1 = x2;
                    x2 = flip;
                }
                for (int i=1; i<(Math.abs(x1-x2)); i++) {
                    if (position[y1][x1 + i] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        // For bishops/queens moving diagonally
        else if (Math.abs(x1 - x2) == Math.abs(y1 - y2)){
            // This case was slightly harder, essentially we need direction vectors to know which diagonal to check
            // Default them to 1, then change to -1 depending on which component is greater
            int xdir = 1;
            int ydir = 1;
            if (x1 > x2){
                xdir = -1;
            }
            if (y1 > y2){
                ydir = -1;
            }
            // Check on the diagonal of the direction of the vector created by xdir and ydir
            for (int i=1; i<(Math.abs(x1-x2)); i++) {
                if (position[y1 + (i*ydir)][x1 + (i*xdir)] != null) {
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }
    // Finally, this function actually makes the move on the board
    public Piece makeMove(int[] moveComponents){
        // Once again, unpack the moveComponents into easy to understand variables
        int x1 = moveComponents[0];
        int y1 = moveComponents[1];
        int x2 = moveComponents[2];
        int y2 = moveComponents[3];
        // If the king is moved, we want to update the location of the king, so we can check for checks there
        if (position[y1][x1].pieceKind == PieceKind.KING){
            if (position[y1][x1].isWhite){
                white_king[0] = x2;
                white_king[1] = y2;
            }
            else{
                black_king[0] = x2;
                black_king[1] = y2;
            }
        }
        // Give names for the starting square and ending square (selectedPiece and captured)
        Piece selectedPiece = position[y1][x1];
        Piece captured = position[y2][x2];
        // Now simply turn the piece on the final square into the piece on the starting square
        position[y2][x2] = position[y1][x1];
        // And then remove the piece on the starting square (since it moved)
        position[y1][x1] = null;
        // If the selected piece is a king, turn off castling rights for the color that moved the king
        if (selectedPiece != null && selectedPiece.pieceKind == PieceKind.KING){
            if (selectedPiece.isWhite){
                castling[0] = false;
                castling[1] = false;
            }
            else{
                castling[2] = false;
                castling[3] = false;
            }
        }
        // If the selectedPiece is a rook, determine which rook moved and turn off its respective castling rights
        if (selectedPiece != null && selectedPiece.pieceKind == PieceKind.ROOK){
            if (selectedPiece.isWhite){
                if (x1 == 0 && y1 == 7){
                    castling[1] = false;
                }
                else if (x1 == 7 && y1 == 7){
                    castling[0] = false;
                }
            }
            else{
                if (x1 == 0 && y1 == 0){
                    castling[3] = false;
                }
                else if (x1 == 7 && y1 == 0) {
                    castling[2] = false;
                }
            }
        }
        // If the piece is a king, and it is castling, enter the correct moveComponents to makeMove
        if (selectedPiece != null && selectedPiece.pieceKind == PieceKind.KING){
            int deltaX = x2 - x1;
            if (selectedPiece.isWhite){
                if (deltaX == 2){
                    makeMove(new int[]{7,7,5,7});
                }
                else if (deltaX == -2){
                    makeMove(new int[]{0,7,3,7});
                }
            }
            else{
                if (deltaX == 2){
                    makeMove(new int[]{7,0,5,0});
                }
                else if (deltaX == -2) {
                    makeMove(new int[]{0,0,3,0});
                }
            }
        }
        return captured;
    }
}