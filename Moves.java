package starfish;

public class Moves {
  public static String moveToAlgebra(String move) {
    String moveString = (char)(move.charAt(1) + 15) + "" + (58 - move.charAt(0));
    moveString += (char)(move.charAt(3) + 15) + "" + (58 - move.charAt(2));
    return moveString + (move.length() == 4 ? ' ' : move.charAt(4));
  }
  public static String algebraToMove(String algebra) {
    algebra = algebra.toUpperCase();
    String moveString = (58 - algebra.charAt(1)) + "" + (algebra.charAt(0) -  63);
    moveString+= (58 - algebra.charAt(3)) + "" + (algebra.charAt(2) -  63);
    return moveString + (algebra.length() == 4 ? ' ' : algebra.charAt(4));
  }
  public static boolean makeMove(Board b, String move, boolean whiteToPlay) {
    // Does not assume validity of input!
    String availableMoves = (whiteToPlay) ? availableMoves(b, true) : availableMoves(b, false);
    if (availableMoves.indexOf(move) < 0) {
      System.out.println("Error: illegal move! " + moveToAlgebra(move));
      b.printBoard();
      System.out.println("White's turn: ");
      return false;
    }
    int from = Integer.parseInt(move.substring(0, 2));
    boolean possiblyCastle = (move.equals("9698 ") || move.equals("9694 ") || move.equals("2628 ") || move.equals("2624 "));
    if (possiblyCastle && Character.toUpperCase(b.board[from/10][from%10]) == 'K') return makeMoveCastle(b, move, whiteToPlay);
    makeMoveVerified(b, move);
    return true;
  }
  public static void makeMoveVerified(Board b, String move) {
    // NOTE: This function assumes validity of input!
    // Generate to and from values from input:
    int from = Integer.parseInt(move.substring(0, 2));
    int to = Integer.parseInt(move.substring(2, 4));
    // Append castle rights histories:
    b.castleRightsHistory[0]+= b.CWK ? "Y" : "N";
    b.castleRightsHistory[1]+= b.CWQ ? "Y" : "N";
    b.castleRightsHistory[2]+= b.CBK ? "Y" : "N";
    b.castleRightsHistory[3]+= b.CBQ ? "Y" : "N";
    // Update position of the kings in board object:
    if (b.board[from/10][from%10] == 'K') {
      b.kingLocs[0] = to/10;
      b.kingLocs[1] = to%10;
      b.CWK = false;
      b.CWQ = false;
    } else if (b.board[from/10][from%10] == 'k') {
      b.kingLocs[2] = to/10;
      b.kingLocs[3] = to%10;
      b.CBK = false;
      b.CBQ = false;
    }
    // Check for first-time rook (corner) movement or capture before first-time rook movement:
    if (move.startsWith("99") || move.endsWith("99 ")) b.CWK = false;
    if (move.startsWith("92") || move.endsWith("92 ")) b.CWQ = false;
    if (move.startsWith("29") || move.endsWith("29 ")) b.CBK = false;
    if (move.startsWith("22") || move.endsWith("22 ")) b.CBQ = false;
    // In case of en passant, remove taken pawn:
    /*if (Math.abs((to%10)-(from%10)) == 1 && Character.toUpperCase(b.board[from/10][from%10]) == 'P' && b.board[to/10][to%10] == ' ') {
      System.out.println("DEBUG: En passant performed");
      if (to/10 == 4) {
        b.board[5][b.possibleEP] = ' ';
      } else if (to/10 == 7) {
        b.board[6][b.possibleEP] = ' ';
      }
      b.printBoard();
    }*/
    // Update epRightsHistory:
    b.epRightsHistory+= b.possibleEP;
    // Add possible en passant in case of double pawn push:
    b.possibleEP = (Math.abs(from-to) == 20 && Character.toUpperCase(b.board[from/10][from%10]) == 'P') ? from%10 : 0;
    // Make the move on the board:
    b.moveHistoryPiecesRemoved += b.board[to/10][to%10];
    b.board[to/10][to%10] = b.board[from/10][from%10];
    b.board[from/10][from%10] = ' ';
    if (move.charAt(4) != ' ') b.board[to/10][to%10] = move.charAt(4);
  }
  public static boolean makeMoveCastle(Board b, String move, boolean whiteToPlay) {
    // NOTE: We can explicitly define castling here to shave processing time a tiny bit.
    // Why? Because we know that this must be the case for castling, always. We'll need to test it out though!
    // ANOTHER NOTE: We may not need to check for b.C** rights since the move must be valid if it passes through here?
    // Verify that castling can happen:
    switch (move) {
      case "9698 ": // White castle kingside
        if (!whiteToPlay || !b.CWK) return false;
        if (b.board[9][7] != ' ' || b.board[9][8] != ' ') return false;
        // then White kingside castle is legal and we perform it:
        b.board[9][6] = ' ';
        b.board[9][9] = ' ';
        b.board[9][8] = 'K';
        b.board[9][7] = 'R';
        b.CWK = false;
        b.CWQ = false;
        return true;
      case "9694 ": // White castle queenside
        if (!whiteToPlay || !b.CWQ) return false;
        if (b.board[9][5] != ' ' || b.board[9][4] != ' ' || b.board[9][3] != ' ') return false;
        // then White queenside castle is legal and we perform it:
        b.board[9][6] = ' ';
        b.board[9][2] = ' ';
        // b.board[9][3] is ' ', otherwise the function would have returned false, so no need doing anything
        b.board[9][4] = 'K';
        b.board[9][5] = 'R';
        b.CWK = false;
        b.CWQ = false;
        return true;
      case "2628 ": // Black castle kingside
        if (whiteToPlay || !b.CBK) return false;
        if (b.board[2][7] != ' ' || b.board[2][8] != ' ') return false;
        // then Black kingside castle is legal and we perform it:
        b.board[2][6] = ' ';
        b.board[2][9] = ' ';
        b.board[2][8] = 'k';
        b.board[2][7] = 'r';
        b.CBK = false;
        b.CBQ = false;
        return true;
      case "2624 ": // Black castle queenside
        if (whiteToPlay || !b.CBQ) return false;
        if (b.board[2][5] != ' ' || b.board[2][4] != ' ' || b.board[2][3] != ' ') return false;
        // then Black queenside castle is legal and we perform it:
        b.board[2][6] = ' ';
        b.board[2][2] = ' ';
        // b.board[2][3] is ' ', otherwise the function would have returned false, so no need doing anything
        b.board[2][4] = 'k';
        b.board[2][5] = 'r';
        b.CBK = false;
        b.CBQ = false;
        return true;
    }
    return false;
  }
  public static void undoMove(Board b, String move) {
    // THIS ASSUMES VALID INPUT (as the user doesn't touch this function)
    // note: this does not mean perfect input ;)
    int to = Integer.parseInt(move.substring(0, 2));
    int from = Integer.parseInt(move.substring(2, 4));
    // Undo castle rights history:
    b.CWK = b.castleRightsHistory[0].endsWith("Y");
    b.CWQ = b.castleRightsHistory[1].endsWith("Y");
    b.CBK = b.castleRightsHistory[2].endsWith("Y");
    b.CBQ = b.castleRightsHistory[3].endsWith("Y");
    for (int i = 0; i < 4; i++) {
      b.castleRightsHistory[i] = b.castleRightsHistory[i].substring(0, b.castleRightsHistory[i].length()-1);
    }
    if (b.CWK && move.equals("9698 ")) { // if the last move was a castle, we have to move the rook back
      b.board[9][7] = ' ';
      b.board[9][9] = 'R';
    } else if (b.CWQ && move.equals("9694 ")) {
      b.board[9][5] = ' ';
      b.board[9][2] = 'R';
    } else if (b.CWK && move.equals("2628 ")) {
      b.board[2][7] = ' ';
      b.board[2][9] = 'r';
    } else if (b.CWQ && move.equals("2624 ")) {
      b.board[2][5] = ' ';
      b.board[2][2] = 'r';
    }
    // Update possibleEP and undo en passant history:
    b.possibleEP = b.epRightsHistory.charAt(b.epRightsHistory.length()-1) - 48; // -48 converts it to its respective character
    b.epRightsHistory = b.epRightsHistory.substring(0, b.epRightsHistory.length()-1);
    // in case of en passant, return the taken pawn to its original position:
    /*if (b.possibleEP == from && Math.abs((to%10)-(from%10)) == 1 && Character.toUpperCase(b.board[from/10][from%10]) == 'P') {
      System.out.println("DEBUG: En passant undone, move " + move);
      if (to/10 == 5) b.board[5][b.possibleEP] = 'p'; // return black pawn
      else if (to/10 == 6) b.board[6][b.possibleEP] = 'P'; // return white pawn
    }*/
    // undo king location movement:
    if (b.board[from/10][from%10] == 'K') {
      b.kingLocs[0] = to/10;
      b.kingLocs[1] = to%10;
    } else if (b.board[from/10][from%10] == 'k') {
      b.kingLocs[2] = to/10;
      b.kingLocs[3] = to%10;
    }
    b.board[to/10][to%10] = b.board[from/10][from%10];
    b.board[from/10][from%10] = b.moveHistoryPiecesRemoved.charAt(b.moveHistoryPiecesRemoved.length()-1);
    b.moveHistoryPiecesRemoved = b.moveHistoryPiecesRemoved.substring(0, b.moveHistoryPiecesRemoved.length()-1);
  }
  public static String availableMoves(Board b, boolean isWhite) {
    if (isInCheck(b.board, isWhite, b.kingLocs)) return availableMovesIIC(b, isWhite);
    return eliminateIllegalMoves(b, availableMovesNCDE(b, isWhite) + possibleCastle(b, isWhite), isWhite);
  }
  public static String availableMovesIIC(Board b, boolean isWhite) {
    // This method assumes the king is in check!!
    String availableMoves = eliminateIllegalMoves(b, availableMovesNCDE(b, isWhite), isWhite);
    String moves = "";
    for (int i = 0; i < availableMoves.length(); i+= 5) {
      Moves.makeMoveVerified(b, availableMoves.substring(i, i+5));
      if (!isInCheck(b.board, isWhite, b.kingLocs)) {
        moves+= availableMoves.substring(i, i+5);
      }
      Moves.undoMove(b, availableMoves.substring(i, i+5));
    }
    return availableMoves;
    //return eliminateIllegalMoves(b, moves, isWhite);
  }
  public static String availableMovesNCDE(Board b, boolean isWhite) { // NC = No Check, DE = Don't eliminate
    // This method assumes the king is NOT in check!
    String moves = "";
    // check for possible en passant:
    /*switch (b.possibleEP) {
      case 0: break;
      case 2:
        if (b.board[5][3] == 'P' && isWhite) moves+= "5342 ";
        else if (b.board[6][3] == 'p' && !isWhite) moves+= "6372 ";
        break;
      case 9:
        if (b.board[5][8] == 'P' && isWhite) moves+= "5849 ";
        else if (b.board[6][8] == 'p' && isWhite) moves+= "6879 ";
        break;
      default:
        if (isWhite) {
          if (b.board[5][b.possibleEP+1] == 'P') moves+= "5" + (b.possibleEP+1) + "4" + (b.possibleEP) + " ";
          if (b.board[5][b.possibleEP-1] == 'P') moves+= "5" + (b.possibleEP-1) + "4" + (b.possibleEP) + " ";
        } else {
          if (b.board[6][b.possibleEP+1] == 'p') moves+= "6" + (b.possibleEP+1) + "7" + (b.possibleEP) + " ";
          if (b.board[6][b.possibleEP-1] == 'p') moves+= "6" + (b.possibleEP-1) + "7" + (b.possibleEP) + " ";
        }
        break;
    }*/
    // NOTE: We could make this more efficient by sorting the moves by probability of success, in case a time limit is introduced
    if (isWhite) {
      for (int i = 2; i < 10; i++) {
        for (int j = 2; j < 10; j++) {
          switch (b.board[i][j]) {
            case ' ': break;
            case 'P':
              moves+= movesWP(b, i, j);
              break;
            case 'N':
              moves+= movesN(b, i, j);
              break;
            case 'R':
              moves+= movesR(b, i, j, true);
              break;
            case 'B':
              moves+= movesB(b, i, j, true);
              break;
            case 'Q':
              moves+= movesQ(b, i, j, true);
              break;
            case 'K':
              moves+= movesK(b, i, j, true);
              break;
          }
        }
      }
    } else {
      // black moves:
      for (int i = 2; i < 10; i++) {
        for (int j = 2; j < 10; j++) {
          switch(b.board[i][j]) {
            case ' ': break;
            case 'p':
              moves+= movesBP(b, i, j);
              break;
            case 'n':
              moves+= movesN(b, i, j);
              break;
            case 'r':
              moves+= movesR(b, i, j, false);
              break;
            case 'b':
              moves+= movesB(b, i, j, false);
              break;
            case 'q':
              moves+= movesQ(b, i, j, false);
              break;
            case 'k':
              moves+= movesK(b, i, j, false);
          }
        }
      }
    }
    return moves;
  }
  public static String eliminateIllegalMoves(Board b, String moves, boolean isWhite) {
    // eliminate illegal moves (a.k.a moves that result in current player being in check)
    String eliminated = "";
    for (int i = 0; i < moves.length(); i+= 5) {
      Board b1 = new Board(b.board);
      Moves.makeMoveVerified(b1, moves.substring(i, i+5));
      if (!isInCheck(b1.board, isWhite, b.kingLocs)) eliminated+= moves.substring(i, i+5);
      undoMove(b1, moves.substring(i, i+5));
    }
    return eliminated;
  }
  public static String possibleCastle(Board b, boolean whiteToPlay) { //9698 9694 2628 2624
    if (isInCheck(b.board, whiteToPlay, b.kingLocs)) return ""; // can't castle out of check!
    // TODO: By the way, that ^^ line may not be necessary as AvailableMovesIIC checks for this anyway. Will have to double-check in the future though.
    String possibleCastle = "";
    if (whiteToPlay) {
      boolean kingsideSquaresChecked = isInCheck(b.board, true, new int[]{9, 7, 0, 0}) || isInCheck(b.board, true, new int[]{9, 8, 0, 0});
      boolean queensideSquaresChecked = isInCheck(b.board, true, new int[]{9, 5, 0, 0}) || isInCheck(b.board, true, new int[]{9, 4, 0, 0}) || isInCheck(b.board, true, new int[]{9, 3, 0, 0});
      if (b.CWK && b.board[9][7] == ' ' && b.board[9][8] == ' ' && !kingsideSquaresChecked) possibleCastle+= "9698 ";
      if (b.CWQ && b.board[9][5] == ' ' && b.board[9][4] == ' ' && b.board[9][3] == ' ' && !queensideSquaresChecked) possibleCastle+= "9694 ";
    } else {
      boolean kingsideSquaresChecked = isInCheck(b.board, false, new int[]{0, 0, 2, 7}) || isInCheck(b.board, false, new int[]{0, 0, 2, 8});
      boolean queensideSquaresChecked = isInCheck(b.board, false, new int[]{0, 0, 2, 5}) || isInCheck(b.board, false, new int[]{0, 0, 2, 4}) || isInCheck(b.board, false, new int[]{0, 0, 2, 3});
      if (b.CBK && b.board[2][7] == ' ' && b.board[2][8] == ' ' && !kingsideSquaresChecked) possibleCastle+= "2628 ";
      if (b.CBQ && b.board[2][5] == ' ' && b.board[2][4] == ' ' && b.board[2][3] == ' ' && !queensideSquaresChecked) possibleCastle+= "2624 ";
    }
    return possibleCastle;
  }
  public static String movesWP(Board b, int i, int j) {
    // moves for the white pawn
    String moves = "";
    if (i == 8 && b.board[7][j] == ' ' && b.board[6][j] == ' ') {
      moves+= "" + i + j + (i-2) + j + " ";
    }
    if (b.board[i-1][j] == ' ' && i != 3) {
      moves+= "" + i + j + (i-1) + j + " ";
    }
    if (Character.isLowerCase(b.board[i-1][j-1])) {
      moves+= "" + i + j + (i-1) + (j-1) + " ";
    }
    if (Character.isLowerCase(b.board[i-1][j+1])) {
      moves+= "" + i + j + (i-1) + (j+1) + " ";
    }
    if (i == 3 && b.board[2][j] == ' ') {
      moves+= "" + i + j + (i-1) + j + "Q";
      //moves+= "" + i + j + (i-1) + j + "N";
    }
    return moves;
  }
  public static String movesBP(Board b, int i, int j) {
    // moves for the black pawn
    String moves = "";
    if (i == 3 && b.board[4][j] == ' ' && b.board[5][j] == ' ') {
      moves+= "" + i + j + (i+2) + j + " ";
    }
    if (b.board[i+1][j] == ' ' && i != 9) {
      moves+= "" + i + j + (i+1) + j + " ";
    }
    if (Character.isUpperCase(b.board[i+1][j-1])) {
      moves+= "" + i + j + (i+1) + (j-1) + " ";
    }
    if (Character.isUpperCase(b.board[i+1][j+1])) {
      moves+= "" + i + j + (i+1) + (j+1) + " ";
    }
    if (i == 9 && b.board[9][j] == ' ') {
      moves+= "" + i + j + (i+1) + j + "Q";
      //moves+= "" + i + j + (i+1) + j + "N";
    }
    return moves;
  }
  public static String movesN(Board b, int i, int j) {
    String moves = "";
    if (Character.isUpperCase(b.board[i][j])) {
      if (isAvailableForWhite(b.board[i-2][j+1])) {
        moves+= "" + i + j + (i-2) + (j+1) + " ";
      }
      if (isAvailableForWhite(b.board[i-1][j+2])) {
        moves+= "" + i + j + (i-1) + (j+2) + " ";
      }
      if (isAvailableForWhite(b.board[i+1][j+2])) {
        moves+= "" + i + j + (i+1) + (j+2) + " ";
      }
      if (isAvailableForWhite(b.board[i+2][j+1])) {
        moves+= "" + i + j + (i+2) + (j+1) + " ";
      }
      if (isAvailableForWhite(b.board[i+2][j-1])) {
        moves+= "" + i + j + (i+2) + (j-1) + " ";
      }
      if (isAvailableForWhite(b.board[i+1][j-2])) {
        moves+= "" + i + j + (i+1) + (j-2) + " ";
      }
      if (isAvailableForWhite(b.board[i-1][j-2])) {
        moves+= "" + i + j + (i-1) + (j-2) + " ";
      }
      if (isAvailableForWhite(b.board[i-2][j-1])) {
        moves+= "" + i + j + (i-2) + (j-1) + " ";
      }
    } else {
      if (isAvailableForBlack(b.board[i-2][j+1])) {
        moves+= "" + i + j + (i-2) + (j+1) + " ";
      }
      if (isAvailableForBlack(b.board[i-1][j+2])) {
        moves+= "" + i + j + (i-1) + (j+2) + " ";
      }
      if (isAvailableForBlack(b.board[i+1][j+2])) {
        moves+= "" + i + j + (i+1) + (j+2) + " ";
      }
      if (isAvailableForBlack(b.board[i+2][j+1])) {
        moves+= "" + i + j + (i+2) + (j+1) + " ";
      }
      if (isAvailableForBlack(b.board[i+2][j-1])) {
        moves+= "" + i + j + (i+2) + (j-1) + " ";
      }
      if (isAvailableForBlack(b.board[i+1][j-2])) {
        moves+= "" + i + j + (i+1) + (j-2) + " ";
      }
      if (isAvailableForBlack(b.board[i-1][j-2])) {
        moves+= "" + i + j + (i-1) + (j-2) + " ";
      }
      if (isAvailableForBlack(b.board[i-2][j-1])) {
        moves+= "" + i + j + (i-2) + (j-1) + " ";
      }
    }
    return moves;
  }
  public static String movesR(Board b, int i, int j, boolean isWhite) {
    if (isWhite) return movesWR(b, i, j);
    return movesBR(b, i, j);
  }
  public static String movesWR(Board b, int i, int j) {
    String moves = "";
    for (int a = i+1; a < 10; a++) {
      // going down:
      if (b.board[a][j] == ' ') {
        moves+= "" + i + j + a + j + " ";
        continue;
      } else if (Character.isUpperCase(b.board[a][j])) {
        break;
      } else {
        moves+= "" + i + j + a + j + " ";
        break;
      }
    }
    for (int a = i-1; a >= 2; a--) {
      // going up:
      if (b.board[a][j] == ' ') {
        moves+= "" + i + j + a + j + " ";
        continue;
      } else if (Character.isUpperCase(b.board[a][j])) {
        break;
      } else {
        moves+= "" + i + j + a + j + " ";
        break;
      }
    }
    for (int a = j+1; a < 10; a++) {
      // going right:
      if (b.board[i][a] == ' ') {
        moves+= "" + i + j + i + a + " ";
        continue;
      } else if (Character.isUpperCase(b.board[i][a])) {
        break;
      } else {
        moves+= "" + i + j + i + a + " ";
        break;
      }
    }
    for (int a = j-1; a >= 2; a--) {
      // going left:
      if (b.board[i][a] == ' ') {
        moves+= "" + i + j +  + i + a + " ";
        continue;
      } else if (Character.isUpperCase(b.board[i][a])) {
        break;
      } else {
        moves+= "" + i + j + i + a + " ";
        break;
      }
    }
    return moves;
  }
  public static String movesBR(Board b, int i, int j) {
    String moves = "";
    for (int a = i+1; a < 10; a++) {
      // going down:
      if (b.board[a][j] == ' ') {
        moves+= "" + i + j + a + j + " ";
        continue;
      } else if (Character.isLowerCase(b.board[a][j])) {
        break;
      } else {
        //System.out.println("can capture down ");
        moves+= "" + i + j + a + j + " ";
        break;
      }
    }
    for (int a = i-1; a >= 2; a--) {
      // going up:
      if (b.board[a][j] == ' ') {
        moves+= "" + i + j + a + j + " ";
        continue;
      } else if (Character.isLowerCase(b.board[a][j])) {
        break;
      } else {
        moves+= "" + i + j + a + j + " ";
        break;
      }
    }
    for (int a = j+1; a < 10; a++) {
      // going right:
      if (b.board[i][a] == ' ') {
        moves+= "" + i + j + i + a + " ";
        continue;
      } else if (Character.isLowerCase(b.board[i][a])) {
        break;
      } else {
        moves+= "" + i + j + i + a + " ";
        break;
      }
    }
    for (int a = j-1; a >= 2; a--) {
      // going left:
      if (b.board[i][a] == ' ') {
        moves+= "" + i + j + i + a + " ";
        continue;
      } else if (Character.isLowerCase(b.board[i][a])) {
        break;
      } else {
        moves+= "" + i + j + i + a + " ";
        break;
      }
    }
    return moves;
  }
  public static String movesB(Board b, int i, int j, boolean isWhite) {
    if (isWhite) return movesWB(b, i, j);
    return movesBB(b, i, j);
  }
  public static String movesWB(Board b, int i, int j) {
    String moves = "";
    for (int a = 1; a < 8; a++) {
      if (i + a < 10 && j + a < 10) {
        if (b.board[i+a][j+a] == ' ') {
          moves+= "" + i + j + (i+a) + (j+a) + " ";
          continue;
        } else if (Character.isUpperCase(b.board[i+a][j+a])) {
          break;
        } else {
          moves+= "" + i + j + (i+a) + (j+a) + " ";
          break;
        }
      }
    }
    for (int a = 1; a < 8; a++) {
      if (i - a >= 2 && j + a < 10) {
        if (b.board[i-a][j+a] == ' ') {
          moves+= "" + i + j + (i-a) + (j+a) + " ";
        } else if (Character.isUpperCase(b.board[i-a][j+a])) {
          break;
        } else {
          moves+= "" + i + j + (i-a) + (j+a) + " ";
          break;
        }
      }
    }
    for (int a = 1; a < 8; a++) {
      if (i + a < 10 && j - a >= 2) {
        if (b.board[i+a][j-a] == ' ') {
          moves+= "" + i + j + (i+a) + (j-a) + " ";
        } else if (Character.isUpperCase(b.board[i+a][j-a])) {
          break;
        } else {
          moves+= "" + i + j + (i+a) + (j-a) + " ";
          break;
        }
      }
    }
    for (int a = 1; a < 8; a++) {
      if (i - a >= 2 && j - a >= 2) {
        if (b.board[i-a][j-a] == ' ') {
          moves+= "" + i + j + (i-a) + (j-a) + " ";
        } else if (Character.isUpperCase(b.board[i-a][j-a])) {
          break;
        } else {
          moves+= "" + i + j + (i-a) + (j-a) + " ";
          break;
        }
      }
    }
    return moves;
  }
  public static String movesBB(Board b, int i, int j) {
    String moves = "";
    for (int a = 1; a < 8; a++) {
      if (i + a < 10 && j + a < 10) {
        if (b.board[i+a][j+a] == ' ') {
          moves+= "" + i + j + (i+a) + (j+a) + " ";
          continue;
        } else if (Character.isLowerCase(b.board[i+a][j+a])) {
          break;
        } else {
          moves+= "" + i + j + (i+a) + (j+a) + " ";
          break;
        }
      }
    }
    for (int a = 1; a < 8; a++) {
      if (i - a >= 2 && j + a < 10) {
        if (b.board[i-a][j+a] == ' ') {
          moves+= "" + i + j + (i-a) + (j+a) + " ";
        } else if (Character.isLowerCase(b.board[i-a][j+a])) {
          break;
        } else {
          moves+= "" + i + j + (i-a) + (j+a) + " ";
          break;
        }
      }
    }
    for (int a = 1; a < 8; a++) {
      if (i + a < 10 && j - a >= 2) {
        if (b.board[i+a][j-a] == ' ') {
          moves+= "" + i + j + (i+a) + (j-a) + " ";
        } else if (Character.isLowerCase(b.board[i+a][j-a])) {
          break;
        } else {
          moves+= "" + i + j + (i+a) + (j-a) + " ";
          break;
        }
      }
    }
    for (int a = 1; a < 8; a++) {
      if (i - a >= 2 && j - a >= 2) {
        if (b.board[i-a][j-a] == ' ') {
          moves+= "" + i + j + (i-a) + (j-a) + " ";
        } else if (Character.isLowerCase(b.board[i-a][j-a])) {
          break;
        } else {
          moves+= "" + i + j + (i-a) + (j-a) + " ";
          break;
        }
      }
    }
    return moves;
  }
  public static String movesQ(Board b, int i, int j, boolean isWhite) {
    // the queen is a rook-bishop hybrid:
    return movesR(b, i, j, isWhite) + movesB(b, i, j, isWhite);
  }
  public static String movesK(Board b, int i, int j, boolean isWhite) {
    String moves = "";
    if (isWhite) {
      if (isAvailableForWhite(b.board[i+1][j])) {
        moves+= "" + i + j + (i+1) + j + " ";
      }
      if (isAvailableForWhite(b.board[i+1][j+1])) {
        moves+= "" + i + j + (i+1) + (j+1) + " ";
      }
      if (isAvailableForWhite(b.board[i][j+1])) {
        moves+= "" + i + j + i + (j+1) + " ";
      }
      if (isAvailableForWhite(b.board[i-1][j+1])) {
        moves+= "" + i + j + (i-1) + (j+1) + " ";
      }
      if (isAvailableForWhite(b.board[i-1][j])) {
        moves+= "" + i + j + (i-1) + j + " ";
      }
      if (isAvailableForWhite(b.board[i-1][j-1])) {
        moves+= "" + i + j + (i-1) + (j-1) + " ";
      }
      if (isAvailableForWhite(b.board[i][j-1])) {
        moves+= "" + i + j + i + (j-1) + " ";
      }
      if (isAvailableForWhite(b.board[i-1][j+1])) {
        moves+= "" + i + j + (i-1) + (j+1) + " ";
      }
      //if (b.CWK && b.board[9][7] == ' ' && b.board[9][8] == ' ') moves+= "9698 "; // White kingside castling
      //if (b.CWQ && b.board[9][5] == ' ' && b.board[9][4] == ' ' && b.board[9][3] == ' ') moves+= "9694 ";
    } else {
      if (isAvailableForBlack(b.board[i+1][j])) {
        moves+= "" + i + j + (i+1) + j + " ";
      }
      if (isAvailableForBlack(b.board[i+1][j+1])) {
        moves+= "" + i + j + (i+1) + (j+1) + " ";
      }
      if (isAvailableForBlack(b.board[i][j+1])) {
        moves+= "" + i + j + i + (j+1) + " ";
      }
      if (isAvailableForBlack(b.board[i-1][j+1])) {
        moves+= "" + i + j + (i-1) + (j+1) + " ";
      }
      if (isAvailableForBlack(b.board[i-1][j])) {
        moves+= "" + i + j + (i-1) + j + " ";
      }
      if (isAvailableForBlack(b.board[i-1][j-1])) {
        moves+= "" + i + j + (i-1) + (j-1) + " ";
      }
      if (isAvailableForBlack(b.board[i][j-1])) {
        moves+= "" + i + j + i + (j-1) + " ";
      }
      if (isAvailableForBlack(b.board[i-1][j+1])) {
        moves+= "" + i + j + (i-1) + (j+1) + " ";
      }
      //if (b.CWK && b.board[2][7] == ' ' && b.board[2][8] == ' ') moves+= "2628 "; // White kingside castling
      //if (b.CWQ && b.board[2][5] == ' ' && b.board[2][4] == ' ' && b.board[2][3] == ' ') moves+= "2624 ";
    }
    return moves;
  }
  public static boolean isInCheck(char[][] board, boolean white, int[] kingLocs) {
    // New, much more efficient method:
    if (white) {
      int kingLocI = kingLocs[0];
      int kingLocJ = kingLocs[1];
      // Find possible pawn checks:
      if (board[kingLocI-1][kingLocJ+1] == 'p' || board[kingLocI-1][kingLocJ-1] == 'p') return true;
      // Find possible knight checks:
      if (board[kingLocI-2][kingLocJ+1] == 'n') return true;
      if (board[kingLocI-1][kingLocJ+2] == 'n') return true;
      if (board[kingLocI+1][kingLocJ+2] == 'n') return true;
      if (board[kingLocI+2][kingLocJ+1] == 'n') return true;
      if (board[kingLocI+2][kingLocJ-1] == 'n') return true;
      if (board[kingLocI+1][kingLocJ-2] == 'n') return true;
      if (board[kingLocI-1][kingLocJ-2] == 'n') return true;
      if (board[kingLocI-2][kingLocJ-1] == 'n') return true;
      // Find possible bishop / queen (diagonal) checks:
      for (int a = 1; a < 8; a++) {
        if (kingLocI - a >= 2 && kingLocJ + a < 10) {
          if (board[kingLocI-a][kingLocJ+a] == ' ') continue;
          if (Character.isUpperCase(board[kingLocI-a][kingLocJ+a])) break;
          else if (board[kingLocI-a][kingLocJ+a] == 'b' || board[kingLocI-a][kingLocJ+a] == 'q') return true;
        }
      }
      for (int a = 1; a < 8; a++) {
        if (kingLocI - a >= 2 && kingLocJ - a >= 2) {
          if (board[kingLocI-a][kingLocJ-a] == ' ') continue;
          if (Character.isUpperCase(board[kingLocI-a][kingLocJ-a])) break;
          else if (board[kingLocI-a][kingLocJ-a] == 'b' || board[kingLocI-a][kingLocJ-a] == 'q') return true;
        }
      }
      for (int a = 1; a < 8; a++) {
        if (kingLocI + a < 10 && kingLocJ - a >= 2) {
          if (board[kingLocI+a][kingLocJ-a] == ' ') continue;
          if (Character.isUpperCase(board[kingLocI+a][kingLocJ-a])) break;
          else if (board[kingLocI+a][kingLocJ-a] == 'b' || board[kingLocI+a][kingLocJ-a] == 'q') return true;
        }
      }
      for (int a = 1; a < 8; a++) {
        if (kingLocI + a < 10 && kingLocJ + a < 10) {
          if (board[kingLocI+a][kingLocJ+a] == ' ') continue;
          if (Character.isUpperCase(board[kingLocI+a][kingLocJ+a])) break;
          else if (board[kingLocI+a][kingLocJ+a] == 'b' || board[kingLocI+a][kingLocJ+a] == 'q') return true;
        }
      }
      // Find possible rook / queen (straight) checks:
      for (int a = kingLocI+1; a < 10; a++) {
        if (board[a][kingLocJ] == ' ') continue;
        if (Character.isUpperCase(board[a][kingLocJ])) break;
        else if (board[a][kingLocJ] == 'r' || board[a][kingLocJ] == 'q') return true;
      }
      for (int a = kingLocI-1; a >= 2; a--) {
        if (board[a][kingLocJ] == ' ') continue;
        if (Character.isUpperCase(board[a][kingLocJ])) break;
        else if (board[a][kingLocJ] == 'r' || board[a][kingLocJ] == 'q') return true;
      }
      for (int a = kingLocJ+1; a < 10; a++) {
        if (board[kingLocI][a] == ' ') continue;
        if (Character.isUpperCase(board[kingLocI][a])) break;
        else if (board[kingLocI][a] == 'r' || board[kingLocI][a] == 'q') return true;
      }
      for (int a = kingLocJ-1; a >= 2; a--) {
        if (board[kingLocI][a] == ' ') continue;
        if (Character.isUpperCase(board[kingLocI][a])) break;
        else if (board[kingLocI][a] == 'r' || board[kingLocI][a] == 'q') return true;
      }
      // possible BUG: kings would be allowed to touch? maybe we need to add another part to this to avoid that?
    } else {
      int kingLocI = kingLocs[2];
      int kingLocJ = kingLocs[3];
      // Find possible pawn checks:
      if (board[kingLocI+1][kingLocJ+1] == 'P' || board[kingLocI+1][kingLocJ-1] == 'P') return true;
      // Find possible knight checks:
      if (board[kingLocI-2][kingLocJ+1] == 'N') return true;
      if (board[kingLocI-1][kingLocJ+2] == 'N') return true;
      if (board[kingLocI+1][kingLocJ+2] == 'N') return true;
      if (board[kingLocI+2][kingLocJ+1] == 'N') return true;
      if (board[kingLocI+2][kingLocJ-1] == 'N') return true;
      if (board[kingLocI+1][kingLocJ-2] == 'N') return true;
      if (board[kingLocI-1][kingLocJ-2] == 'N') return true;
      if (board[kingLocI-2][kingLocJ-1] == 'N') return true;
      // Find possible bishop / queen (diagonal) checks:
      for (int a = 1; a < 8; a++) {
        if (kingLocI + a < 10 && kingLocJ - a >= 2) {
          if (board[kingLocI+a][kingLocJ-a] == ' ') continue;
          if (Character.isLowerCase(board[kingLocI+a][kingLocJ-a])) break;
          else if (board[kingLocI+a][kingLocJ-a] == 'B' || board[kingLocI+a][kingLocJ-a] == 'Q') return true;
        }
      }
      for (int a = 1; a < 8; a++) {
        if (kingLocI + a < 10 && kingLocJ + a < 10) {
          if (board[kingLocI+a][kingLocJ+a] == ' ') continue;
          if (Character.isLowerCase(board[kingLocI+a][kingLocJ+a])) break;
          else if (board[kingLocI+a][kingLocJ+a] == 'B' || board[kingLocI+a][kingLocJ+a] == 'Q') return true;
        }
      }
      for (int a = 1; a < 8; a++) {
        if (kingLocI - a >= 2 && kingLocJ + a < 10) {
          if (board[kingLocI-a][kingLocJ+a] == ' ') continue;
          if (Character.isLowerCase(board[kingLocI-a][kingLocJ+a])) break;
          else if (board[kingLocI-a][kingLocJ+a] == 'B' || board[kingLocI-a][kingLocJ+a] == 'Q') return true;
        }
      }
      for (int a = 1; a < 8; a++) {
        if (kingLocI - a >= 2 && kingLocJ - a >= 2) {
          if (board[kingLocI-a][kingLocJ-a] == ' ') continue;
          if (Character.isLowerCase(board[kingLocI-a][kingLocJ-a])) break;
          else if (board[kingLocI-a][kingLocJ-a] == 'B' || board[kingLocI-a][kingLocJ-a] == 'Q') return true;
        }
      }
      // Find possible rook / queen (straight) checks:
      for (int a = kingLocI+1; a < 10; a++) {
        if (board[a][kingLocJ] == ' ') continue;
        if (Character.isLowerCase(board[a][kingLocJ])) break;
        else if (board[a][kingLocJ] == 'R' || board[a][kingLocJ] == 'Q') return true;
      }
      for (int a = kingLocI-1; a >= 2; a--) {
        if (board[a][kingLocJ] == ' ') continue;
        if (Character.isLowerCase(board[a][kingLocJ])) break;
        else if (board[a][kingLocJ] == 'R' || board[a][kingLocJ] == 'Q') return true;
      }
      for (int a = kingLocJ+1; a < 10; a++) {
        if (board[kingLocI][a] == ' ') continue;
        if (Character.isLowerCase(board[kingLocI][a])) break;
        else if (board[kingLocI][a] == 'R' || board[kingLocI][a] == 'Q') return true;
      }
      for (int a = kingLocJ-1; a >= 2; a--) {
        if (board[kingLocI][a] == ' ') continue;
        if (Character.isLowerCase(board[kingLocI][a])) break;
        else if (board[kingLocI][a] == 'R' || board[kingLocI][a] == 'Q') return true;
      }
    }
    return false;
  }
  public static boolean isAvailableForWhite(char x) {
    // helper function to determine wether white can move onto 'x':
    return (Character.isLowerCase(x) || x == ' ');
  }
  public static boolean isAvailableForBlack(char x) {
    // helper function to determine wether black can move onto 'x':
    return (Character.isUpperCase(x) || x == ' ');
  }
}
