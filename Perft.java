package starfish;

public class Perft {
  public static int perft(Board b, boolean whiteToPlay, int depth) {
    //char[][] b1 = board.clone();
    String availableMoves = Moves.availableMoves(b, whiteToPlay);
    if (depth == 1) return availableMoves.length() / 5;
    int sumMoves = 0;
    for (int i = 0; i < availableMoves.length(); i+= 5) {
      Board b1 = new Board(Utils.deepCopy(b.board));
      try {
        Moves.makeMoveVerified(b1, availableMoves.substring(i, i+5));
      } catch (Exception e) {
        System.out.println("ERROR: " + availableMoves);
        System.out.println("\t" + availableMoves.substring(i, i+5));
        Board.printBoard(b1.board);
        return -1;
      }
      //Board.printBoard(b1);
      sumMoves+= perft(b1, !whiteToPlay, depth-1);
    }
    return sumMoves;
  }
}
