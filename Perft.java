// NOTE: THIS HAS NOT BEEN WORKING SINCE MOVING FROM CHAR[][] REPRESENTATION TO
// BOARD CLASS REPRESENTATION!

package starfish;

public class Perft {
  public static int perft(Board b, boolean whiteToPlay, int depth) {
    if (depth == 1) return Moves.availableMoves(b, whiteToPlay).length()/5;
    String availableMoves = Moves.availableMoves(b, whiteToPlay);
    int sumMoves = 0;
    for (int i = 0; i < availableMoves.length(); i+=5) {
      Moves.makeMoveVerified(b, availableMoves.substring(i, i+5));
      sumMoves+= perft(b, !whiteToPlay, depth-1);
      Moves.undoMove(b, availableMoves.substring(i, i+5)); // THERE'S A PROBLEM WITH THIS?
    }
    return sumMoves;
  }
}
