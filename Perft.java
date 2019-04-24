package starfish;

public class Perft {
  public static int perft(Board b, boolean isWhite, int depth) {
    if (depth == 1) return Moves.availableMoves(b, isWhite).length()/5;
    String moves = Moves.availableMoves(b, isWhite);
    int sumMoves = 0;
    for (int i = 0; i < moves.length(); i+= 5) {
      Moves.makeMove(b, moves.substring(i, i+5), isWhite);
      sumMoves+= perft(b, !isWhite, depth-1);
      Moves.undoMove(b, moves.substring(i, i+5));
    }
    return sumMoves;
  }
}
