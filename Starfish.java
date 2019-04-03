package starfish;

import java.util.Scanner;
import java.util.ArrayList;

public class Starfish {
  public static boolean whiteToPlay = true;
  public static void main(String[] args) {
    // testing shit
    Utils.printLogo();
    playStarfish();
    /*long startTime = System.nanoTime();
    Evaluation.getBestMove(b, 3, -Constants.INF, Constants.INF, false);
    long endTime = System.nanoTime();
    System.out.println("Execution took " + (endTime - startTime)/1000000 + "ms"); */
    //playTwoPlayerChess();

    // Perft testing:
    /*long startTime = System.nanoTime();
    int p = Perft.perft(b, true, 4);
    System.out.println(p);
    long endTime = System.nanoTime();
    System.out.println("Took " + (endTime - startTime)/1000000 + "ms to compute " + p + " positions.");
    */
  }
  public static void playTwoPlayerChess() {
    System.out.println("Welcome to Starfish's two-player chess interface!");
    System.out.println("To make a move, enter it in algebraic notation.");
    System.out.println("For example: D2D4 will move the piece on D2 to D4");
    System.out.println("Happy playing!\n");
    Board b = new Board();
    boolean whiteTurn = b.whiteTurn;
    b.printBoard();
    Scanner in = new Scanner(System.in);
    String userMove = "";
    while (!Evaluation.isGameOver(b, whiteTurn)) {
      if (whiteTurn) {
        System.out.println("White's turn: ");
      } else {
        System.out.println("Black's turn: ");
      }
      userMove = in.nextLine();
      if (userMove.equalsIgnoreCase("exit")) return;
      while (!Moves.makeMove(b, Moves.algebraToMove(userMove), whiteTurn)) {
        userMove = in.nextLine();
      }
      whiteTurn = !whiteTurn;
      b.printBoard();
    }
  }
  public static void playStarfish() {
    //Board b = new Board(Board.getSunilBoard());
    Board b = new Board();
    boolean whiteTurn = b.whiteTurn; // could just be set to true, but could be useful later
    b.printBoard();
    Scanner in = new Scanner(System.in);
    String userMove = "";
    String computerMove = "";
    //String lastLegalUserMove = ""; // used for undoing moves
    ArrayList<String> moveHistory = new ArrayList<String>();
    while (!Evaluation.isGameOver(b, whiteTurn)) {
      if (whiteTurn) {
        System.out.print("\nWhite's turn: ");
        /* CASTLING STUFF
        if (Moves.possibleCastle(b, whiteTurn).length() > 0) {
          System.out.println("Possible castling options: " + Moves.possibleCastle(b, whiteTurn));
        } else {
          System.out.println("\tCannot castle right now. DEBUG: ");
          System.out.println("\tCWK = " + b.CWK);
          System.out.println("\tCWQ = " + b.CWQ);
          System.out.println("\tCBK = " + b.CBK);
          System.out.println("\tCBQ = " + b.CBQ);
          System.out.println();
        }*/
        userMove = in.nextLine();
        if (userMove.equalsIgnoreCase("exit")) return;
        if (userMove.equalsIgnoreCase("resign")) {
          System.out.println("User resigns. Starfish wins!");
          return;
        }
        if (userMove.equalsIgnoreCase("undo")) {
          if (moveHistory.size() < 2) {
            System.out.println("No moves in move history!");
            continue;
          }
          System.out.println("Undoing move");
          Moves.undoMove(b, moveHistory.get(moveHistory.size()-1));
          moveHistory.remove(moveHistory.size()-1);
          Moves.undoMove(b, moveHistory.get(moveHistory.size()-1));
          moveHistory.remove(moveHistory.size()-1);
          b.printBoard();
          continue;
        }
        if (userMove.startsWith("import")) {
          try {
            b = new Board(userMove.substring(userMove.indexOf(" ") + 1));
            whiteTurn = b.whiteTurn;
          } catch (Exception e) {} // do nothing
          continue;
        }
        if (userMove.startsWith("export")) {
          System.out.println("Current board FEN: " + b.getFEN());
          if (userMove.startsWith("export!")) return;
          continue;
        }
        while (!Moves.makeMove(b, Moves.algebraToMove(userMove), whiteTurn)) {
          userMove = in.nextLine();
        }
        // for undoing moves, we keep track of the moves made
        moveHistory.add(Moves.algebraToMove(userMove));
        //b.printBoardBlack();
      } else {
        System.out.print("\nStarfish's turn: ");
        // IDEA: Perhaps add a feature where Starfish could decide to resign if static eval < SOME_NUMBER?
        long startTime = System.nanoTime();
        computerMove = Evaluation.getBestMove(b, 4, -Constants.INF, Constants.INF, false);
        Moves.makeMove(b, computerMove, whiteTurn);
        // add Starfish's move to the move history
        moveHistory.add(computerMove);
        long endTime = System.nanoTime();
        System.out.println("Calculation took " + (endTime - startTime)/1000000 + "ms\n");
        //b.printBoard();
      }
      whiteTurn = !whiteTurn;
      b.printBoard();
    }
  }
}
