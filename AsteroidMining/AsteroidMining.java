import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * The Main class implements an application that reads lines from the standard input
 * and prints them to the standard output.
 */
public class AsteroidMining {
  /**
   * Iterate through each line of input.
   */

  public static void main(String[] args) throws IOException {
    InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
    BufferedReader in = new BufferedReader(reader);
    String line;
    while ((line = in.readLine()) != null) {
      System.out.println(findMinDays(Integer.parseInt(line)));
    }
  }

  //FUNCTION: Calculates minimum number of days to mine a certain amount of grams
  //Because the robots have two options per days, the problem divides itself into two subproblems
  //Therefore the number of total subproblems can be calculated with log base 2 of the total needed grams
  //Each problem division could be seen as a day passing by
  public static int findMinDays(int totalGrams) {
      return (int) Math.ceil(Math.log(totalGrams) / Math.log(2)) + 1;
  }
}
