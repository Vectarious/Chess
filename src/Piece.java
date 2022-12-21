// an enumerator which I only sort of understand what it's doing, but I found it while searching
// for an easy way to get subclasses for the piece class and it works very well
enum PieceKind {
    ROOK,
    KNIGHT,
    BISHOP,
    QUEEN,
    KING,
    PAWN
}
public class Piece {
    // Each piece object has variables for what color it is and also if it is white/black
    public PieceKind pieceKind;
    public boolean isWhite;
    // isWhite is a boolean since there are only 2 colors, while pieceKind is a PieceKind (from the enumerator)
    public Piece(boolean isWhite, PieceKind pieceKind){
        this.isWhite = isWhite;
        this.pieceKind = pieceKind;
    }
    // I found really nice ascii chess piece characters so this function
    // turns the subclasses into pieces when I print the board
    public String toString() {
        switch (pieceKind) {
            case ROOK:
                return " ♖ ";
            case KNIGHT:
                return " ♘ ";
            case BISHOP:
                return " ♗ ";
            case QUEEN:
                return " ♕ ";
            case KING:
                return " ♔ ";
            case PAWN:
                return " ♙ ";
            default:
                return "pobingus";
        }
    }
}
