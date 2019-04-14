// NOTE: THIS HAS NOT BEEN WORKING SINCE MOVING FROM CHAR[][] REPRESENTATION TO
// BOARD CLASS REPRESENTATION!

package starfish;

public class Perft {
  public static int perft(Board b, boolean whiteToPlay, int depth) {
    //char[][] b1 = board.clone();
    String availableMoves = Moves.availableMoves(b, whiteToPlay);
    if (depth == 0) b.printBoard();
    //if (depth == 1) return availableMoves.length() / 5;
    // more 'correct' way would be if (depth == 0) return 1
    int sumMoves = 0;
    for (int i = 0; i < availableMoves.length(); i+= 5) {
      try {
        Moves.makeMove(b, availableMoves.substring(i, i+5), whiteToPlay);
        sumMoves+= perft(b, !whiteToPlay, depth-1);
        Moves.undoMove(b, availableMoves.substring(i, i+5));
      } catch (Exception e) {
        System.out.println("ERROR: " + availableMoves);
        System.out.println("\t" + availableMoves.substring(i, i+5));
        Board.printBoard(b.board);
        return -1;
      }
    }
    if (depth >= 2) System.out.println("Depth: " + depth + ", sumMoves = " + sumMoves);
    return sumMoves;
  }
}
