package starfish;

public class Board {
  public static char[][] board;
  public static boolean whiteTurn = true; // we assume white goes first, even if FEN is imported
  public static String moveHistoryPiecesRemoved = " ";
  public static int[] kingLocs = {9, 6, 2, 6};
  public static boolean CWK = true, CWQ = true, CBK = true, CBQ = true; // castle rights
  public Board() {
    // standard chess board
    board = getStandardBoard();
  }
  public Board(char[][] board) {
    this.board = Utils.deepCopy(board);
  }
  public Board(String FEN) {
    // for FEN importing:
    board = new char[12][12];
    CWK = false; CWQ = false; CBK = false; CBQ = false;
    // Set up the blank board:
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        if (i < 2 || i > 9 || j < 2 || j > 9) board[i][j] = '-';
        else board[i][j] = ' ';
      }
    }
    String[] fenSplit = FEN.split(" ");
    String[] lineSplit = fenSplit[0].split("/");
    if (lineSplit.length < 8) {
      System.out.println("Error: Invalid FEN string");
      board = getStandardBoard();
    }
    char c;
    for (int i = 2; i < board.length - 2; i++) {
      int strIterator = 0;
      for (int j = 2; j < board[0].length - 2; j++) {
        c = lineSplit[i-2].charAt(strIterator);
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) board[i][j] = c; // if c is alphabetic
        else if (Character.isDigit(c) && c != '0') j+= (c - 49); // convert character to int equivalent
        strIterator++;
      }
    }
    try {
      if (fenSplit[1].indexOf('b') >= 0) whiteTurn = false;
      // check castling rights:
      if (fenSplit[2].indexOf('K') >= 0) CWK = true;
      if (fenSplit[2].indexOf('Q') >= 0) CWQ = true;
      if (fenSplit[2].indexOf('k') >= 0) CBK = true;
      if (fenSplit[2].indexOf('q') >= 0) CBQ = true;
    } catch (Exception e) {} // do nothing
    // find kings:
    for (int i = 2; i < 10; i++) {
      for (int j = 2; j < 10; j++) {
        if (board[i][j] == 'K') {
          kingLocs[0] = i;
          kingLocs[1] = j;
        } else if (board[i][j] == 'k') {
          kingLocs[2] = i;
          kingLocs[3] = j;
        }
      }
    }
    printBoard();
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
  public static String getFEN() {
    String FEN = "";
    int numSpaces = 0;
    char c;
    for (int i = 2; i < 10; i++) {
      for (int j = 2; j < 10; j++) {
        c = board[i][j]; // not needed, but is here for simplicity of code
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) { // if c is alphabetic
          FEN+= c;
          continue;
        }
        while (c == ' ') {
          j++;
          numSpaces++;
          c = board[i][j];
        }
        FEN+= numSpaces;
        numSpaces = 0;
        j--;
      }
      FEN+= "/";
    }
    FEN+= whiteTurn ? " w " : " b ";
    if (CWK) FEN+= "K";
    if (CWQ) FEN+= "Q";
    if (CBK) FEN+= "k";
    if (CBQ) FEN+= "q";
    return FEN;
  }
  /*public static boolean[][] paddedSafetyBoard() {
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
  }*/ 

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

  public static void printBoardBlack() {
    // prints itself from Black's perspective
    for (int i = 9; i > 1; i--) {
      for (int j = 9; j > 1; j--) {
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
