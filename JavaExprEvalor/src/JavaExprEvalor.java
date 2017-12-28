import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
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
	static String y_expr;

	static Map<String, Double> symbolTable;

	static int is;
	static int ie;

	static String currToken;

	final static Pattern floatNumber = Pattern.compile("^([-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)");
	final static Pattern id = Pattern.compile("^([a-z][A-Za-z0-9]*)");

	private JavaExprEvalor() {}

	public static double calculate() {

		if (symbolTable == null) {
			symbolTable = new HashMap<String, Double>();
		}
		is = 0;
		ie = 0;
		return expr();
	}

	public void setExpression(String y_expr) {
		JavaExprEvalor.y_expr = y_expr;
	}

	public void setUserVariable(String x, double v) {
		symbolTable.put(x, v);
	}

	private static String peekToken() {
		if (is != ie) {
			return currToken;
		}

		wsSkip();

		if (y_expr.charAt(ie) == '(' || y_expr.charAt(ie) == ')' ||
			y_expr.charAt(ie) == '+' || y_expr.charAt(ie) == '-' ||
			y_expr.charAt(ie) == '*' || y_expr.charAt(ie) == '/' ||
			y_expr.charAt(ie) == '^' || y_expr.charAt(ie) == ',') {
			ie++;
		}
		else {
			String rest = y_expr.substring(is);
			Matcher fnm = floatNumber.matcher(rest);
			Matcher idm = id.matcher(rest);
			if (fnm.find()) {
				ie = fnm.end(1);
			}
			else if (idm.find()) {
				ie = idm.end(1);
			}
		}
		currToken = y_expr.substring(is, ie);
		return currToken;
	}

	private static void wsSkip() {
		while (	ie < y_expr.length() &&
				y_expr.charAt(ie) == ' ' ||
				y_expr.charAt(ie) == '\t' ||
				y_expr.charAt(ie) == '\n') {
			ie++;
		}
		is = ie;
	}

	private static String consumeToken() {
		String currToken = peekToken();
		is = ie;
		JavaExprEvalor.currToken = null;
		return currToken;
	}

	private static double expr() {
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

	private static double term() {
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
	private static double pow() {
		double factor = factor();
		while (consumeToken().equals("^")) {
			factor *= pow();
		}
		return factor;
	}

	private static double factor() {
		String currToken = consumeToken();
		if (currToken.equals("sin")) {
			String leftPar = consumeToken();
			if (!leftPar.equals("(")) {
				//error
			}
			double e = expr();
			String rightPar = consumeToken();
			if (!rightPar.equals(")")) {
				//error
			}
			return Math.sin(e);
		}
		if (currToken.equals("cos")) {
			String leftPar = consumeToken();
			if (!leftPar.equals("(")) {
				//error
			}
			double e = expr();
			String rightPar = consumeToken();
			if (!rightPar.equals(")")) {
				//error
			}
			return Math.cos(e);
		}
		if (currToken.equals("tan")) {
			String leftPar = consumeToken();
			if (!leftPar.equals("(")) {
				//error
			}
			double e = expr();
			String rightPar = consumeToken();
			if (!rightPar.equals(")")) {
				//error
			}
			return Math.tan(e);
		}
		if (currToken.equals("atan")) {
			String leftPar = consumeToken();
			if (!leftPar.equals("(")) {
				//error
			}
			double e = expr();
			String rightPar = consumeToken();
			if (!rightPar.equals(")")) {
				//error
			}
			return Math.atan(e);
		}
		if (currToken.equals("atan2")) {
			String leftPar = consumeToken();
			if (!leftPar.equals("(")) {
				//error
			}
			double e1 = expr();
			String comma = consumeToken();
			if (!comma.equals(",")) {
				//error
			}
			double e2 = expr();
			String rightPar = consumeToken();
			if (!rightPar.equals(")")) {
				//error
			}
			return Math.atan2(e1, e2);
		}
		if (currToken.equals("(")) {
			double e = expr();
			String rightPar = consumeToken();
			if (!rightPar.equals(")")) {
				//error
			}
			return e;
		}
		if (floatNumber.matcher(currToken).matches()) {
			return Double.parseDouble(currToken);
		}
		if (id.matcher(currToken).matches()) {
			if (!symbolTable.containsKey(currToken)) {
				throw new IllegalArgumentException("variable " + currToken + " was NOT set to a value before");
			}
			return symbolTable.get(currToken);
		}

		throw new IllegalArgumentException("un-recogonized token: " + currToken);

	}

	public static void main (String[] args) {
	}
}