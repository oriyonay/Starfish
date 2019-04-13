package starfish;

import java.util.Scanner;

public class UCI { // Modified code from Origami
  public static final String ENGINE = "Starfish v0.4";
  public static int UCI_DEPTH = 3;
  public static Board b = new Board();
    public static void uciCommunication() {
      Scanner in = new Scanner(System.in);
      while (true) {
        String input = in.nextLine();
        if (input.equalsIgnoreCase("uci")) inputUCI();
        else if (input.startsWith("setoption")) inputSetOption(input);
        else if (input.equalsIgnoreCase("isready")) inputIsReady();
        else if (input.equalsIgnoreCase("ucinewgame")) inputUCINewGame();
        else if (input.startsWith("position")) inputPosition(input);
        else if (input.equalsIgnoreCase("go")) inputGo();
        else if (input.equalsIgnoreCase("print")) inputPrint();
        else if (input.equalsIgnoreCase("exit")) break;
      }
    }
    public static void inputUCI() {
      System.out.println("id name " + ENGINE);
      System.out.println("id author Ori Yonay");
      // print options - do this later
      System.out.println("uciok");
    }
    public static void inputSetOption(String input) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    public static void inputIsReady() {
      System.out.println("readyok");
    }
    public static void inputUCINewGame() {
      b = new Board();
    }
    public static void inputPosition(String input) {
      b = new Board();
      input=input.substring(9).concat(" ");
      if (input.contains("startpos ")) {
        input=input.substring(9);
        b = new Board();
      } else if (input.contains("fen")) {
        input = input.substring(4);
        b = new Board(input);
      }
      if (input.contains("moves")) {
        input = input.substring(input.indexOf("moves") + 6);
        //make each of the moves:
        String[] moves = input.split(" ");
        for (String a: moves) {
          Moves.makeMove(b, Moves.algebraToMove(a), b.whiteTurn);
          b.whiteTurn = !b.whiteTurn;
        }
      }
    }
    public static void inputGo() {
        // search for the best move. will probably be done on a separate thread
        String bestMove = Evaluation.getBestMove(b, UCI_DEPTH, -Constants.INF, Constants.INF, b.whiteTurn);
        System.out.println("bestmove " + Moves.moveToAlgebra(bestMove).toLowerCase());
    }
    public static void inputPrint() {
      b.printBoard();
    }
}
