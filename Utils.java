package starfish;

import java.util.Arrays;

public class Utils {
  // extra utilities
  public static char[][] deepCopy(char[][] original) {
    if (original == null) return null;
    final char[][] result = new char[original.length][];
    for (int i = 0; i < original.length; i++) {
        result[i] = Arrays.copyOf(original[i], original[i].length);
    }
    return result;
  }
  public static void printLogo() {
    System.out.println(" _____ _____ ___  ____________ _____ _____ _   _ ");
    System.out.println("/  ___|_   _/ _ \\ | ___ \\  ___|_   _/  ___| | | |");
    System.out.println("\\ `--.  | |/ /_\\ \\| |_/ / |_    | | \\ `--.| |_| |");
    System.out.println(" `--. \\ | ||  _  ||    /|  _|   | |  `--. \\  _  |");
    System.out.println("/\\__/ / | || | | || |\\ \\| |    _| |_/\\__/ / | | |");
    System.out.println("\\____/  \\_/\\_| |_/\\_| \\_\\_|    \\___/\\____/\\_| |_/");
    System.out.println();
    System.out.println("Version 0.3, written by Ori Yonay\n");
  }
  public static void printHelpMenu() {
    System.out.println("Help: use 'exit' or 'resign' to quit.");
    System.out.println("To import a FEN position, use 'import [FEN STRING]'.");
    System.out.println("To export a FEN string, use 'export', or 'export!' to quit after exporting.");
    System.out.println("To undo a move, type 'undo'.\n");
  }
}
