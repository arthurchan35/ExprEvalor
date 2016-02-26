import java.util.Scanner;

/**
 * Created by arthurchan35 on 2/25/2016.
 *
 * <expr> : <term> {(+ | -)<term>}
 * <term> : <factor> {(* | /)<term>}
 * <factor> : ID | INT_CONST | ( <expr> )
 *
 */
public class JavaExprEvalor {
	JavaExprEvalor(String y_expr, String x_val) {

	}

	void expr() {

	}

	void term() {

    }

	void factor() {

	}

	public static void main (String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("y = ? ");
		String y_expr = scanner.nextLine();
	}
}