package starfish;

public class Evaluation {
  public static String bestMove = "";
  public static int positionCounter = 0;
  // material evaluation:
  public static final int pawn    = 100;
  public static final int knight  = 280;
  public static final int bishop  = 320;
  public static final int rook    = 500;
  public static final int queen   = 900;
  public static final int king    = 100000;

  // piece-square tables for the middlegame:
  public static final int[][] pawnMiddleGame = {
    {0,  0,  0,  0,  0,  0,  0,  0},
    {50, 50, 50, 50, 50, 50, 50, 50},
    {10, 10, 30, 40, 40, 30, 10, 10},
    {5,  5, 20, 35, 35, 20,  5,  5},
    {0,  0,  0, 30, 30,  0,  0,  0},
    {5, -5,-10,  0,  0,-10, -5,  5},
    {5, 10, 10,-20,-20, 10, 10,  5},
    {0,  0,  0,  0,  0,  0,  0,  0}
  };

  public static final int[][] knightMiddleGame = {
    {-50,-40,-30,-30,-30,-30,-40,-50},
    {-40,-20,  0,  0,  0,  0,-20,-40},
    {-30,  0, 10, 15, 15, 10,  0,-30},
    {-30,  5, 15, 20, 20, 15,  5,-30},
    {-30,  0, 15, 20, 20, 15,  0,-30},
    {-30,  5, 10, 15, 15, 10,  5,-30},
    {-40,-20,  0,  5,  5,  0,-20,-40},
    {-50,-40,-30,-30,-30,-30,-40,-50}
  };

  public static final int[][] bishopMiddleGame = {
    {-20,-10,-10,-10,-10,-10,-10,-20},
    {-10,  0,  0,  0,  0,  0,  0,-10},
    {-10,  0,  5, 10, 10,  5,  0,-10},
    {-10, 15,  5, 10, 10,  5, 15,-10},
    {-10,  0, 15, 10, 10, 15,  0,-10},
    {-10, 10, 10, 10, 10, 10, 10,-10},
    {-10,  5,  0,  0,  0,  0,  5,-10},
    {-20,-10,-10,-10,-10,-10,-10,-20}
  };

  public static final int[][] rookMiddleGame = {
    {0,  0,  0,  0,  0,  0,  0,  0},
    {5, 10, 10, 10, 10, 10, 10,  5},
    {-5,  0,  0,  0,  0,  0,  0, -5},
    {-5,  0,  0,  0,  0,  0,  0, -5},
    {-5,  0,  0,  0,  0,  0,  0, -5},
    {-5,  0,  0,  0,  0,  0,  0, -5},
    {-5,  0,  0,  0,  0,  0,  0, -5},
    {0,  0,  0,  5,  5,  0,  0,  0}
  };

  public static final int[][] queenMiddleGame = {
    {-20,-10,-10, -5, -5,-10,-10,-20},
    {-10,  0,  0,  0,  0,  0,  0,-10},
    {-10,  0,  5,  5,  5,  5,  0,-10},
    {-5,  0,  5,  5,  5,  5,  0, -5},
    {-5,  0,  5,  5,  5,  5,  0, -5},
    {-10,  5,  5,  5,  5,  5,  0,-10},
    {-10,  0,  5,  0,  0,  0,  0,-10},
    {-20,-10,-10, -5, -5,-10,-10,-20}
  };

  public static final int[][] kingMiddleGame = {
    {-30,-40,-40,-50,-50,-40,-40,-30},
    {-30,-40,-40,-50,-50,-40,-40,-30},
    {-30,-40,-40,-50,-50,-40,-40,-30},
    {-30,-40,-40,-50,-50,-40,-40,-30},
    {-20,-30,-30,-40,-40,-30,-30,-20},
    {-10,-20,-20,-20,-20,-20,-20,-10},
    {20, 20,  0,  0,  0,  0, 20, 20},
    {20, 30, 10,  0,  0, 10, 30, 20}
  };

  /* ENDGAME
  public static final int pawn    = 150;
  public static final int knight  = 250;
  public static final int bishop  = 350;
  public static final int rook    = 600;
  public static final int queen   = 1000;

  static int pawn_endgame[] =
{
   0,  0,  0,  0,  0,  0,  0,  0,
  50, 50, 50, 50, 50, 50, 50, 50,
  10, 10, 20, 30, 30, 20, 10, 10,
   5,  5, 10, 25, 25, 10,  5,  5,
   0,  0,  0, 20, 20,  0,  0,  0,
   5, -5,-10,  0,  0,-10, -5,  5,
   5, 10, 10,-20,-20, 10, 10,  5,
   0,  0,  0,  0,  0,  0,  0,  0
};

static int knight_endgame[] =
{
  -50,-40,-30,-30,-30,-30,-40,-50,
  -40,-20,  0,  0,  0,  0,-20,-40,
  -30,  0, 10, 15, 15, 10,  0,-30,
  -30,  5, 15, 20, 20, 15,  5,-30,
  -30,  0, 15, 20, 20, 15,  0,-30,
  -30,  5, 10, 15, 15, 10,  5,-30,
  -40,-20,  0,  5,  5,  0,-20,-40,
  -50,-40,-30,-30,-30,-30,-40,-50
};

static int bishop_endgame[] =
{
  -20,-10,-10,-10,-10,-10,-10,-20,
  -10,  0,  0,  0,  0,  0,  0,-10,
  -10,  0,  5, 10, 10,  5,  0,-10,
  -10,  5,  5, 10, 10,  5,  5,-10,
  -10,  0, 10, 10, 10, 10,  0,-10,
  -10, 10, 10, 10, 10, 10, 10,-10,
  -10,  5,  0,  0,  0,  0,  5,-10,
  -20,-10,-10,-10,-10,-10,-10,-20
};

static int rook_endgame[] =
{
   0,  0,  0,  0,  0,  0,  0,  0,
   5, 10, 10, 10, 10, 10, 10,  5,
  -5,  0,  0,  0,  0,  0,  0, -5,
  -5,  0,  0,  0,  0,  0,  0, -5,
  -5,  0,  0,  0,  0,  0,  0, -5,
  -5,  0,  0,  0,  0,  0,  0, -5,
  -5,  0,  0,  0,  0,  0,  0, -5,
   0,  0,  0,  5,  5,  0,  0,  0
};

static int queen_endgame[] =
{
  -20,-10,-10, -5, -5,-10,-10,-20,
  -10,  0,  0,  0,  0,  0,  0,-10,
  -10,  0,  5,  5,  5,  5,  0,-10,
   -5,  0,  5,  5,  5,  5,  0, -5,
   -5,  0,  5,  5,  5,  5,  0, -5,
  -10,  5,  5,  5,  5,  5,  0,-10,
  -10,  0,  5,  0,  0,  0,  0,-10,
  -20,-10,-10, -5, -5,-10,-10,-20
};

static int king_end_game[] =
{
  -50,-40,-30,-20,-20,-30,-40,-50,
  -30,-20,-10,  0,  0,-10,-20,-30,
  -30,-10, 20, 30, 30, 20,-10,-30,
  -30,-10, 30, 40, 40, 30,-10,-30,
  -30,-10, 30, 40, 40, 30,-10,-30,
  -30,-10, 20, 30, 30, 20,-10,-30,
  -30,-30,  0,  0,  0,  0,-30,-30,
  -50,-30,-30,-30,-30,-30,-30,-50
};

  */

  public static int evaluate(Board board, boolean whiteToPlay) {
    // check if checkmate:
    // NOTE: THIS ASSUMES THAT AVAILABLEMOVES WORKS CORRECTLY REGARDING CHECK EVASION
    if (Moves.availableMoves(board, whiteToPlay).length() == 0) {
      if (Moves.isInCheck(board.board, whiteToPlay)) { // checkmate
        if (whiteToPlay) return -Constants.CHECKMATE_SCORE;
        else return Constants.CHECKMATE_SCORE;
      } else return 0; // draw */
    }
    int eval = 0;
    for (int i = 2; i < 10; i++) {
      for (int j = 2; j < 10; j++) {
        switch (board.board[i][j]) {
          case ' ':
            continue;
          case 'p':
            eval-= (pawn + pawnMiddleGame[9-i][9-j]);
            break;
          case 'n':
            eval-= (knight + knightMiddleGame[9-i][9-j]);
            break;
          case 'b':
            eval-= (bishop + bishopMiddleGame[9-i][9-j]);
            break;
          case 'r':
            eval-= (rook + rookMiddleGame[9-i][9-j]);
            break;
          case 'q':
            eval-= (queen + queenMiddleGame[9-i][9-j]);
            break;
          case 'k':
            eval-= (king + kingMiddleGame[9-i][9-j]);
            break;
          case 'P':
            eval+= (pawn + pawnMiddleGame[i-2][j-2]);
            break;
          case 'N':
            eval+= (knight + knightMiddleGame[i-2][j-2]);
            break;
          case 'B':
            eval+= (bishop + bishopMiddleGame[i-2][j-2]);
            break;
          case 'R':
            eval+= (rook + rookMiddleGame[i-2][j-2]);
            break;
          case 'Q':
            eval+= (queen + queenMiddleGame[i-2][j-2]);
            break;
          case 'K':
            eval+= (king + kingMiddleGame[i-2][j-2]);
            break;
        }
      }
    }
    return eval;
  }
  public static boolean isGameOver(Board b, boolean currPlayer) {
    return Moves.availableMoves(b, currPlayer).length() == 0;
  }
  public static String getBestMove(Board currPos, int depth, int alpha, int beta, boolean isWhite) {
    positionCounter = 0; // reset position counter
    alphaBeta(currPos, depth, alpha, beta, isWhite);
    System.out.print(Moves.moveToAlgebra(bestMove));
    System.out.println("\nPositions analyzed: " + positionCounter);
    return bestMove;
  }
  public static int alphaBeta(Board currPos, int depth, int alpha, int beta, boolean isWhite) {
    if (depth == 0 || isGameOver(currPos, isWhite)) {
      positionCounter++;
      return Evaluation.evaluate(currPos, isWhite);
    }
    String moves = Moves.availableMoves(currPos, isWhite);
    int bestMoveIndex = -1;
    Board tempBoard = new Board(currPos.board);
    if (isWhite) { // maximizing player
      int value = -Constants.INF;
      for (int i = 0; i < moves.length(); i+=5) {
        Moves.makeMove(tempBoard, moves.substring(i, i+5), true);
        int ab = alphaBeta(new Board(tempBoard.board), depth-1, alpha, beta, false);
        Moves.undoMove(tempBoard, moves.substring(i, i+5));
        //tempBoard.printBoard();
        if (ab > value) {
          value = ab;
          bestMoveIndex = i;
        }
        if (value > alpha) alpha = value;
        if (alpha >= beta) break; // beta cut-off
      }
      return value;
    } else {
      int value = Constants.INF;
      for (int i = 0; i < moves.length(); i+=5) {
        Moves.makeMove(tempBoard, moves.substring(i, i+5), false);
        int ab = alphaBeta(new Board(tempBoard.board), depth-1, alpha, beta, true);
        Moves.undoMove(tempBoard, moves.substring(i, i+5));
        //tempBoard.printBoard();
        if (ab < value) {
          value = ab;
          bestMoveIndex = i;
        }
        if (value < beta) beta = value;
        if (alpha >= beta) break; // beta cut-off
      }
      bestMove = moves.substring(bestMoveIndex, bestMoveIndex+5);
      //System.out.println("Depth: " + depth + ", bestMoveIndex = " + bestMoveIndex + ", which is " + Moves.moveToAlgebra(moves.substring(bestMoveIndex, bestMoveIndex+5)));
      return value;
    }
  }
}
