package starfish;

public class Board {
  public static char[][] board;
  //public static char lastMovePieceRemoved = ' '; // if the most recent move was D2D4, this would be what used to be on D4
  public static String moveHistoryPiecesRemoved = " ";
  public static boolean CWK = true, CWQ = true, CBK = true, CBQ = true; // castle rights
  public Board() {
    // standard chess board
    board = getStandardBoard();
  }
  public Board(char[][] board) {
    this.board = Utils.deepCopy(board);
  }
  public Board(String FEN) {
    // TO BE IMPLEMENTED
    // for FEN importing:
    int charIndex = 0, boardIndex = 0;
    while (FEN.charAt(charIndex) != ' ') {
      /*switch(FEN.charAt(charIndex)) {
        case 'P':
          break;
      }*/
    }
  }
  public static char[][] getStandardBoard() {
    return new char[][] {
      {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
      {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
      {'-', '-', 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r', '-', '-'},
      {'-', '-', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', '-', '-'},
      {'-', '-', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '-', '-'},
      {'-', '-', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '-', '-'},
      {'-', '-', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '-', '-'},
      {'-', '-', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '-', '-'},
      {'-', '-', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', '-', '-'},
      {'-', '-', 'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R', '-', '-'},
      {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
      {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'}};
  }
  public static char[][] getSunilBoard() {
    return new char[][] {
      {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
      {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
      {'-', '-', ' ', ' ', 'Q', ' ', 'n', 'k', ' ', 'r', '-', '-'},
      {'-', '-', 'P', ' ', ' ', ' ', ' ', ' ', 'p', ' ', '-', '-'},
      {'-', '-', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '-', '-'},
      {'-', '-', ' ', ' ', ' ', ' ', 'p', ' ', ' ', 'p', '-', '-'},
      {'-', '-', ' ', ' ', ' ', 'p', ' ', ' ', ' ', ' ', '-', '-'},
      {'-', '-', ' ', ' ', ' ', 'P', ' ', ' ', ' ', ' ', '-', '-'},
      {'-', '-', ' ', 'P', ' ', ' ', ' ', 'P', 'P', 'P', '-', '-'},
      {'-', '-', ' ', ' ', ' ', ' ', 'K', ' ', ' ', 'R', '-', '-'},
      {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
      {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'}};
  }
  public static boolean[][] paddedSafetyBoard() {
    // not the most efficient, but it works.
    // plus, this method is called twice in the game so it doesn't really matter
    boolean[][] safetyBoard = new boolean[12][12];
    for (int i = 0; i < safetyBoard.length; i++) {
      for (int j = 0; j < safetyBoard[0].length; j++) {
        if (i < 2 || i > 9 || j < 2 || j > 9) safetyBoard[i][j] = false;
        else safetyBoard[i][j] = true;
      }
    }
    return safetyBoard;
  }
  public static void printBoard(char[][] board) {
    // class function that prints any board
    for (int i = 2; i < 10; i++) {
      for (int j = 2; j < 10; j++) {
        System.out.print(board[i][j] + "|");
      }
      System.out.print(System.lineSeparator());
    }
    System.out.print(System.lineSeparator());
  }
  public static void printBoard() {
    // prints itself
    for (int i = 2; i < 10; i++) {
      for (int j = 2; j < 10; j++) {
        System.out.print(board[i][j] + "|");
      }
      System.out.print(System.lineSeparator());
    }
    System.out.print(System.lineSeparator());
  }
  public static void printSafetyBoard(boolean[][] safety) {
    for (int i = 0; i < 12; i++) {
      for (int j = 0; j < 12; j++) {
        if (safety[i][j]) {
          System.out.print(" |");
        } else {
          System.out.print("X|");
        }
      }
      System.out.print(System.lineSeparator());
    }
    System.out.print(System.lineSeparator());
  }
}
