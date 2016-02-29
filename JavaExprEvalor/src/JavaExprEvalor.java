import java.util.Scanner;
import java.util.Stack;

/**
 * Created by arthurchan35 on 2/25/2016.
 *
 * <expr> : <term> {(+ | -)<term>}
 * <term> : <factor> {(* | /)<term>}
 * <factor> : ID | INT_CONST | ( <expr> )
 *
 */
public class JavaExprEvalor {
	String y_expr;
	Double x_val;
	int index;
	int length;
	Stack<Character> parenthesis;
	JavaExprEvalor(String y_expr, String x_val) {
		this.y_expr = y_expr;
		try {
			this.x_val = Double.parseDouble(x_val);
		}
		catch (NumberFormatException e){
			System.out.println("Invalid input for variable X, please input a number for X.");
			System.exit(-1);
		}
		index = 0;
		length = y_expr.length();
		parenthesis = new Stack<>();
	}

	private Double expr() {
		Double result = term();

		while (index < length && (y_expr.charAt(index) == '+' || y_expr.charAt(index) == '-')) {
			if (y_expr.charAt(index++) == '+') {
				result += term();
			}
			else {
				result -= term();
			}
		}
		return result;
	}

	private Double term() {
		Double result = pow();

		while (index < length && (y_expr.charAt(index) == '*' || y_expr.charAt(index) == '/')) {
			if (y_expr.charAt(index++) == '*') {
				result *= pow();
			}
			else {
				try {
					result /= pow();
				}
				catch (ArithmeticException e) {
					System.out.println("Divisor is 0");
					System.exit(-1);
				}
			}
		}
		return result;
    }

	private Double pow() {
		Double result = factor();
		while (index < length && (y_expr.charAt(index) == '^')) {
			index++;
			result = Math.pow(result, factor());
		}
		return result;
	}

	private void wsSkip() {
		while (index < length && (y_expr.charAt(index) == ' ' || y_expr.charAt(index) == '\t'))
			index++;
	}

	private Double parenthOp() {
		index++;
		parenthesis.push('(');
		Double result = expr();
		wsSkip();
		if (index == length || y_expr.charAt(index) != ')' || parenthesis.empty() || parenthesis.pop() != '(') {
			System.out.println("Incorrect closing parenthesis at index " + index);
			System.exit(-1);
		}
		index++;
		wsSkip();
		return result;
	}

	private Double[] parenthOp2() {
		Double[] result = new Double[2];
		index++;
		parenthesis.push('(');
		result[0] = expr();
		wsSkip();
		if (index == length || y_expr.charAt(index) != ',') {
			System.out.println("Missing comma at index " + index);
			System.exit(-1);
		}
		index++;
		wsSkip();
		result[1] = expr();
		wsSkip();
		if (index == length || y_expr.charAt(index) != ')' || parenthesis.empty() || parenthesis.pop() != '(') {
			System.out.println("Incorrect closing parenthesis at index " + index);
			System.exit(-1);
		}
		index++;
		wsSkip();
		return result;
	}

	private Double factor() {
		wsSkip();

		if (y_expr.charAt(index) == 'x') {
			index++;
			return x_val;
		}

		if (y_expr.charAt(index) == 's' && y_expr.charAt(index + 1) == 'i' && y_expr.charAt(index + 2) == 'n' && y_expr.charAt(index + 3) == '(') {
			index += 3;
			Double result = parenthOp();
			return Math.sin(result);
		}

		if (y_expr.charAt(index) == 'c' && y_expr.charAt(index + 1) == 'o' && y_expr.charAt(index + 2) == 's' && y_expr.charAt(index + 3) == '(') {
			index += 3;
			Double result = parenthOp();
			return Math.cos(result);
		}

		if (y_expr.charAt(index) == 't' && y_expr.charAt(index + 1) == 'a' && y_expr.charAt(index + 2) == 'n' && y_expr.charAt(index + 3) == '(') {
			index += 3;
			Double result = parenthOp();
			return Math.tan(result);
		}

		if (y_expr.charAt(index) == 'l' && y_expr.charAt(index + 1) == 'n' && y_expr.charAt(index + 2) == '(') {
			index += 2;
			Double result = parenthOp();
			return Math.log(result);
		}

		if (y_expr.charAt(index) == 'a' && y_expr.charAt(index + 1) == 't' && y_expr.charAt(index + 2) == 'a' && y_expr.charAt(index + 3) == 'n' && y_expr.charAt(index + 4) == '2' && y_expr.charAt(index + 5) == '(') {
			index += 5;
			Double result[] = parenthOp2();
			return Math.atan2(result[0], result[1]);
		}

		if (y_expr.charAt(index) == 'a' && y_expr.charAt(index + 1) == 't' && y_expr.charAt(index + 2) == 'a' && y_expr.charAt(index + 3) == 'n' && y_expr.charAt(index + 4) == '(') {
			index += 4;
			Double result = parenthOp();
			return Math.atan(result);
		}


		if (y_expr.charAt(index) == '(')
			return parenthOp();

		String num = "";
		Double result = 0.0;

		while (index < length && (y_expr.charAt(index) <= '9' && y_expr.charAt(index) >= '0') || y_expr.charAt(index) == '.')
			num += y_expr.charAt(index++);
		try {
			result = Double.parseDouble(num);
		}
		catch (NumberFormatException e) {
			System.out.println("Invalid number: \"" + num + "\" in expression :" + y_expr);
			System.exit(-1);
		}

		wsSkip();

		return result;
	}

	public static void main (String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("y = ? ");
			String y_expr = scanner.nextLine();
			System.out.println("x ? ");
			String x_val = scanner.nextLine();

			JavaExprEvalor jee = new JavaExprEvalor(y_expr, x_val);
			Double result = jee.expr();
			if (!jee.parenthesis.empty()) {
				System.out.println("Too few closing parenthesis in expression.");
				System.exit(-1);
			}
			else {
				System.out.println(result);
			}
		}
	}
}