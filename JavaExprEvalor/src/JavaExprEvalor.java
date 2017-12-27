import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by arthurchan35 on 2/25/2016.
 *
 * Recursive Descent parser is difficult to handle Left recursion
 * when it comes to subtraction and division, thus we use EBNF
 * 
 * <expr> : <term> <expr>'
 * 
 * <expr>' : + <term>
 *         : - <term>
 *         :
 * 
 * <term> : <pow> <term>'
 * 
 * <term>' : * <pow>
 *         : / <pow>
 *         :
 * 
 * <pow> : <factor> ^ <pow>
 * 
 * <factor> : ID <factor>'
 *          : INT_CONST
 *          : (<expr>)
 * 
 * <factor>' : (<args>)  <-- function call, using <factor>' to reduce ambiguity
 *           :
 * 
 * <args> : <expr>, <args>
 *        : <expr>
 */
public class JavaExprEvalor {
	String y_expr;
	Double x_val;

	int is;
	int ie;

	String currToken;

	Pattern floatNumber = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");

	private JavaExprEvalor() {}

	public double calculate(String y_expr, String x_val) {

		is = 0;
		ie = 0;
		this.y_expr = y_expr;
		if (x_val == null) {
			x_val = null;
		}
		else {
			this.x_val = Double.parseDouble(x_val);
		}
		return expr();
	}

	private String peekToken() {
		if (is != ie) {
			return currToken;
		}

		wsSkip();
		if (y_expr.charAt(ie) == '(' || y_expr.charAt(ie) == ')') {
			currToken = Character.toString(y_expr.charAt(ie++));
		}
	}

	private void wsSkip() {
		while (	ie < y_expr.length() &&
				y_expr.charAt(ie) == ' ' ||
				y_expr.charAt(ie) == '\t' ||
				y_expr.charAt(ie) == '\n') {
			ie++;
		}
		is = ie;
	}

	private String consumeToken() {
		
	}

	private double expr() {
		double term = term();
		while (peekToken().equals("+") || peekToken().equals("-")) {
			String currToken = consumeToken();
			if (currToken.equals("+")) {
				term += term();
			}
			else {
				term -= term();
			}
		}
		return term;
	}

	private double term() {
		double pow = pow();
		while (peekToken().equals("*") || peekToken().equals("/")) {
			String currToken = consumeToken();
			if (currToken.equals("*")) {
				pow *= pow();
			}
			else {
				pow /= pow();
			}
		}
		return pow;
	}

	//right associativity, right recursive
	private double pow() {
		double factor = factor();
		while (consumeToken().equals("^")) {
			factor *= pow();
		}
		return factor;
	}

	private double factor() {
		String currToken = consumeToken();
		if (currToken.equals("(")) {
			
		}
		if (floatNumber.matcher(currToken).matches()) {
			return Double.parseDouble(currToken);
		}
		if (currToken.equals("x")) {
			if (this.x_val == null) {
				throw new IllegalArgumentException("variable " + currToken + " was NOT set to a value before");
			}
			return this.x_val;
		}

		throw new IllegalArgumentException("un-recogonized token: " + currToken);

	}

	public static void main (String[] args) {
	}
}