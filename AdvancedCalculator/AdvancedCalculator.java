import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * The Main class implements an application that reads lines from the standard input
 * and prints them to the standard output.
 */
public class AdvancedCalculator {
  /**
   * Iterate through each line of input.
   */


  //Formatter to ensure all values are rounded to the 5th decimal value
  static DecimalFormat df = new DecimalFormat("0.00000");

  //A list of all possible arithmetic symbols in the equation
  static String[] arithmeticSymbols = {"+", "-", "*", "/", "^", "!", "mod", "lg", "ln", "cos", "sin", "tan", "sqrt"};

  //Value for 'pi'
  final static double PI = Double.parseDouble(df.format(Math.PI));

  //Value for 'e'
  final static double E = Double.parseDouble(df.format(Math.E));

  public static void main(String[] args) throws IOException {
    
    //Parenthesis stack: stores index of last opening-parenthesis
    Stack<Integer> PStack = new Stack<Integer>();

    //Absolute Value stack: stores index of last absolute value sign
    Stack<Integer> AbsStack = new Stack<Integer>();

    InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
    BufferedReader in = new BufferedReader(reader);

    //Program input
    String line;
    while ((line = in.readLine()) != null) {

        //A loop to ensure all arithmetic symbols have spaces on either side
        //This is later used to split the equation into individual expressions
        for(String op : arithmeticSymbols) {
            //Don't put spaces on either side of minus signs, could signify it being negative
            if(op == "-") continue;
            if(line.contains(op)) {
                line = line.replaceAll(Pattern.quote(op), " " +op+ " ");
            }
        }

        //Iterate throug the string and check for absolute value and parenthesis, those must be calculated first
        for(char c : line.toCharArray()) {
            //Use a stack to handle parenthesis (stack stores their index)
            if(c == '(') {
               PStack.push(line.indexOf(c)); 
            }
            else if(c == ')') {

                //Get the index of the last opening-parenthesis
                int lastPIndex = PStack.pop();

                //Set the array of expressions to the expressions inside parenthesis (separated by spaces)
                String parenthesisExpression = line.substring(lastPIndex + 1, line.indexOf(c));
                parenthesisExpression = parenthesisExpression.trim().replaceAll(" +", " ");
                String[] expressions = parenthesisExpression.split(" ");

                //Replace the parenthesis expression with the calculated value of the inner-expression
                //with spaces on either side
                line = line.replaceFirst(Pattern.quote(line.substring(lastPIndex, line.indexOf(c) + 1)), " " +calculateExpression(expressions)+ " ");
            }

            //Use another stack to handle absolute value (stack stores their index)
            if(c == '|') {
                AbsStack.push(line.indexOf(c));
            }
            else if(c == '|' && !AbsStack.empty()) {
                //Get the index of the last absolute value symbol
                int lastAbsIndex = AbsStack.pop();

                //Set the array of expressions to the expressions inside the absolute value (separated by spaces)
                String absoluteExpression = line.substring(lastAbsIndex + 1, line.indexOf(c));
                String[] expressions = absoluteExpression.split(" ");

                //Replace the absolute value expressions with the calculated value of the inner-expression
                //with spaces on either side
                line = line.replaceFirst(Pattern.quote(line.substring(lastAbsIndex, line.indexOf(c) + 1)), " " +Math.abs(calculateExpression(expressions))+ " ");
            }
            
        }

        line = line.trim().replaceAll(" +", " ");
        String[] expressions = line.split(" ");

        //Find the final value of the equation
        double finalValue = calculateExpression(expressions);

        //If the final value is whole, convert it to int
        if(finalValue % 1 == 0) System.out.println((int) finalValue);
        else {
            //Otherwise, print it out as it is
            System.out.println(finalValue);
        }
        
    }
  }

  //Returns the factorial of a number
  public static int factorial(int n) {
    int f = 1;
    for(int i = 1; i <= n; i++) f *= i;
    return f;
  }

  //Calculates equation given an array of expressions
  public static double calculateExpression(String[] expressions) {

    //Loop in order-of-operations order (addition and subtraction are last)
    for(int i = 0; i < expressions.length; i++) {
        switch(expressions[i]) {
            case "Pi":
                expressions[i] = ""+PI;
                break;
            case "e":
                expressions[i] = ""+E;
                break;
            default:
                break;
        }
    }

    for(int i = 0; i < expressions.length; i++) {
        switch(expressions[i]) {
            case "!":
                expressions[i] = df.format(factorial(Integer.parseInt(expressions[i-1])));
                expressions = removeIndexFromArray(expressions, new int[]{i-1});
                i-=1;
                break;
            case "lg":
                expressions[i] = df.format(Math.log10(Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromArray(expressions, new int[]{i+1});
                break;
            case "ln":
                expressions[i] = df.format(Math.log(Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromArray(expressions, new int[]{i+1});
                break;
            case "sin":
                expressions[i] = df.format(Math.sin(Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromArray(expressions, new int[]{i+1});
                break;
            case "cos":
                expressions[i] = df.format(Math.cos(Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromArray(expressions, new int[]{i+1});
                break;
            case "tan":
                expressions[i] = df.format(Math.tan(Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromArray(expressions, new int[]{i+1});
                break;
            case "sqrt":
                expressions[i] = df.format(Math.sqrt(Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromArray(expressions, new int[]{i+1});
                break;
            default:
                break;
        }
    }
            
    for(int i = 0; i < expressions.length; i++) {
        switch(expressions[i]) {
            case "*":
                expressions[i] = df.format(Double.parseDouble(expressions[i-1]) * Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromArray(expressions, new int[]{i-1, i+1});
                i-=1;
                break;
            case "/":
                expressions[i] = df.format(Double.parseDouble(expressions[i-1]) / Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromArray(expressions, new int[]{i-1, i+1});
                i-=1;
                break;
            case "mod":
                expressions[i] = df.format(Double.parseDouble(expressions[i-1]) % Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromArray(expressions, new int[]{i-1, i+1});
                i-=1;
                break;
            case "^":
                expressions[i] = df.format(Math.pow(Double.parseDouble(expressions[i-1]), Double.parseDouble(expressions[i+1])));
                expressions = removeIndexFromArray(expressions, new int[]{i-1, i+1});
                i-=1;
                break;
            default:
                break;
        }
    }
    for(int i = 0; i < expressions.length; i++) {
        switch(expressions[i]) {
            case "+":
                expressions[i] = df.format(Double.parseDouble(expressions[i-1]) + Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromArray(expressions, new int[]{i-1, i+1});
                i-=1;
                break;
            case "-":
                expressions[i] = df.format(Double.parseDouble(expressions[i-1]) - Double.parseDouble(expressions[i+1]));
                expressions = removeIndexFromArray(expressions, new int[]{i-1, i+1});
                i-=1;
                break;
            default:
                break;
        }
    }
    return Double.parseDouble(expressions[0]);
  }

  //Removes certain indices from an array
  //Takes original array and array of indices as arguments
  public static String[] removeIndexFromArray(String[] array, int[] indices) {
    
    //Create a new array to copy information into
    //Size is the difference between original array and how many indices need to be removed
    String[] newArray = new String[array.length - indices.length];

    //Initialize the offset
    int offset = 0;

    //For every index of the new array
    for(int i = 0; i < newArray.length; i++) {

        //Check if current index matches one of the indices that needs to be removed
        for(int j : indices) {

            //If so...
            if(i == j - offset) {

                //Increment the offset value
                offset++;

            }
        } 

        //Copy the current index plus the offset from the original array into the new one
        newArray[i] = array[i+offset];
    }

    //Return the new array
    return newArray;
  }

}