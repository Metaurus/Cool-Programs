import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Stack;

/**
 * The Main class implements an application that reads lines from the standard input
 * and prints them to the standard output.
 */
public class test {
  /**
   * Iterate through each line of input.
   */
  static DecimalFormat df = new DecimalFormat("0.00000");
  static String[] arithmeticSymbols = {"+", "-", "*", "/", "^", "mod", "lg", "ln", "cos", "sin", "tan", "sqrt"};
  final static double PI = Double.parseDouble(df.format(Math.PI));
  final static double E = Double.parseDouble(df.format(Math.E));
  public static void main(String[] args) throws IOException {
    Stack<Integer> PStack = new Stack<Integer>();
    Stack<Integer> AbsStack = new Stack<Integer>();
    InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
    BufferedReader in = new BufferedReader(reader);
    String line;
    while ((line = in.readLine()) != null) {
        //A loop to ensure all arithmetic symbols have spaces on either side
        for(String op : arithmeticSymbols) {
            //Don't put spaces on either side of minus signs, could signify it being negative
            if(op == "-") continue;
            if(line.contains(op)) {
                line.replaceAll(op, " " +op+ " ");
            }
        }

        //Iterate throug the string and check for absolute value and parenthesis, those must be calculated first
        for(char c : line.toCharArray()) {
            //Use a stack to handle parenthesis (stack stores their index)
            if(c == '(') {
               PStack.push(line.indexOf(c)); 
            }
            else if(c == ')') {
                int lastPIndex = PStack.pop();
                String[] expressions = line.split("\\"+line.substring(lastPIndex, line.indexOf(c)));
                for(String s : expressions) System.out.println(s);
                line.replaceAll(line.substring(lastPIndex, line.indexOf(c)), calculateExpression(expressions)+ " ");
            }

            //Use another stack to handle absolute value (stack stores their index)
            if(c == '|') {
                AbsStack.push(line.indexOf(c));
            }
            else if(c == '|' && !AbsStack.empty()) {
                int lastAbsIndex = AbsStack.pop();
                String[] expressions = line.split(line.substring(lastAbsIndex, line.indexOf(c)+1));
                line.replaceAll(line.substring(lastAbsIndex, line.indexOf(c)), Math.abs(calculateExpression(expressions))+ " ");
            }
            
        }
        System.out.println(calculateExpression(line.split(" ")));
    }
  }

  public static int factorial(int n) {
    int f = 1;
    for(int i = 1; i <= n; i++) f *= i;
    return f;
  }

  public static double calculateExpression(String[] expressions) {
    for(int i = 0; i < expressions.length; i++) {
        switch(expressions[i]) {
            case "Pi":
                expressions[i] = ""+PI;
                break;
            case "e":
                expressions[i] = ""+E;
                break;
            case "*":
                expressions[i] = df.format(Double.parseDouble(expressions[i]) * Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromStringArray(expressions, new int[]{i-1, i+1});
                break;
            case "/":
                expressions[i] = df.format(Double.parseDouble(expressions[i]) / Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromStringArray(expressions, new int[]{i-1, i+1});
                break;
            case "mod":
                expressions[i] = df.format(Double.parseDouble(expressions[i]) % Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromStringArray(expressions, new int[]{i-1, i+1});
                break;
            case "^":
                expressions[i] = df.format(Math.pow(Double.parseDouble(expressions[i-1]), Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromStringArray(expressions, new int[]{i-1, i+1});
                break;
            case "lg":
                expressions[i] = df.format(Math.log10(Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromStringArray(expressions, new int[]{i+1});
                break;
            case "ln":
                expressions[i] = df.format(Math.log(Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromStringArray(expressions, new int[]{i+1});
                break;
            default:
                break;
        }
    }
    for(int i = 0; i < expressions.length; i++) {
        switch(expressions[i]) {
            case "+":
                expressions[i] = df.format(Double.parseDouble(expressions[i]) + Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromStringArray(expressions, new int[]{i-1, i+1});
                break;
            case "-":
                expressions[i] = df.format(Double.parseDouble(expressions[i]) - Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromStringArray(expressions, new int[]{i-1, i+1});
                break;
            default:
                break;
        }
    }
    return Double.parseDouble(expressions[0]);
  }

  public static String[] removeIndexFromStringArray(String[] array, int[] indices) {
    String[] newArray = new String[array.length - indices.length];
    int offset = 0;
    for(int i = 0; i < newArray.length; i++) {
        for(int j : indices) {
            if(i == j) {
                offset++;
                continue;
            }
        } 
        newArray[i] = array[i+offset];
    }
    return newArray;
  }

}