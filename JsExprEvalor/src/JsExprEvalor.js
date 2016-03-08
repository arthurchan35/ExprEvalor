function JsExprEvalor(y_expr, x_val) {
	this.y_expr = y_expr;
	this.x_val = x_val;
	this.index = 0;
	this.length = y_expr.length;
	this.parenthesis = new Array();	
	
	this.expr = function expr() {
		var result = this.term();

		while (this.index < this.length && (this.y_expr.charAt(this.index) == "+" || this.y_expr.charAt(this.index) == "-") ) {
			if (y_expr.charAt(this.index++) == '+') {
				result += this.term();
			}
			else {
				result -= this.term();
			}
		}
		return result;		
	}

	this.term = function term() {
		var result = this.pow();

		while (this.index < this.length && (this.y_expr.charAt(this.index) == "*" || this.y_expr.charAt(this.index) == "/") ) {
			if (y_expr.charAt(this.index++) == '*') {
				result *= this.pow();
			}
			else {
				var Divisor = this.pow();
				if (Divisor == 0) {
					console.log("Divisor cannot be zero");
					exit();
				}
				result /= Divisor;
				
			}
		}
		return result;
	}

	this.pow = function pow() {
		var result = this.factor();

		while (this.index < this.length && (this.y_expr.charAt(this.index) == "^")) {
			this.index++;
			result = Math.pow(result, this.factor());
		}
		return result;
	}

	this.wsSkip = function wsSkip() {
		while (this.index < this.length && (this.y_expr.charAt(this.index) == ' ' || this.y_expr.charAt(this.index) == '\t'))
			this.index++;

	}

	this.parenthOp = function parenthOp() {
		this.index++;
		this.parenthesis.push("(");
		var result = this.expr();
		this.wsSkip();

		if (this.index == this.length || this.y_expr.charAt(this.index) != ")" || this.parenthesis.length == 0 || this.parenthesis.pop() != "(") {
			console.log("Incorrect closing parenthesis at index " + this.index);
			exit();
		}

		this.index++;
		this.wsSkip();
		return result;

	}

	this.parenthOp2 = function parenthOp2() {
		var result = new Array();
		this.index++;
		this.parenthesis.push("(");
		result[0] = this.expr();
		this.wsSkip();

		if (this.index == this.length || this.y_expr.charAt(this.index) != ",") {
			console.log("Missing comma at index " + this.index);
			exit();
		}

		this.index++;
		this.wsSkip();
		result[1] = this.expr();
		this.wsSkip();

		if (this.index == this.length || this.y_expr.charAt(this.index) != ")" || this.parenthesis.length == 0 || this.parenthesis.pop() != "(") {
			console.log("Incorrect closing at index " + this.index);
			exit();
		}

		this.index++;
		this.wsSkip();
		return result;
	}

	this.factor = function factor() {
		this.wsSkip();

		if (this.y_expr.charAt(this.index) == 'x') {
			this.index++;
			return this.x_val;
		}

		if (this.y_expr.charAt(this.index) == 's' && this.y_expr.charAt(this.index + 1) == 'i' && this.y_expr.charAt(this.index + 2) == 'n' && this.y_expr.charAt(this.index + 3) == '(') {
			this.index += 3;
			var result = this.parenthOp();
			return Math.sin(result);
		}

		if (this.y_expr.charAt(this.index) == 'c' && this.y_expr.charAt(this.index + 1) == 'o' && this.y_expr.charAt(this.index + 2) == 's' && this.y_expr.charAt(this.index + 3) == '(') {
			this.index += 3;
			var result = this.parenthOp();
			return Math.cos(result);
		}

		if (this.y_expr.charAt(this.index) == 't' && this.y_expr.charAt(this.index + 1) == 'a' && this.y_expr.charAt(this.index + 2) == 'n' && this.y_expr.charAt(this.index + 3) == '(') {
			this.index += 3;
			var result = this.parenthOp();
			return Math.tan(result);
		}

		if (this.y_expr.charAt(this.index) == 'l' && this.y_expr.charAt(this.index + 1) == 'n' && this.y_expr.charAt(this.index + 2) == '(') {
			this.index += 2;
			var result = this.parenthOp();
			return Math.log(result);
		}

		if (this.y_expr.charAt(this.index) == 'a' && this.y_expr.charAt(this.index + 1) == 't' && this.y_expr.charAt(this.index + 2) == 'a' && this.y_expr.charAt(this.index + 3) == 'n' && this.y_expr.charAt(this.index + 4) == '2' && this.y_expr.charAt(this.index + 5) == '(') {
			this.index += 5;
			var result = this.parenthOp2();
			return Math.atan2(result[0], result[1]);
		}

		if (this.y_expr.charAt(this.index) == 'a' && this.y_expr.charAt(this.index + 1) == 't' && this.y_expr.charAt(this.index + 2) == 'a' && this.y_expr.charAt(this.index + 3) == 'n' && this.y_expr.charAt(this.index + 4) == '(') {
			this.index += 4;
			var result = this.parenthOp();
			return Math.atan(result);
		}


		if (this.y_expr.charAt(this.index) == '(')
			return this.parenthOp();

		var num = "";
		var result = 0.0;

		while (this.index < this.length && ((this.y_expr.charAt(this.index) <= '9' && this.y_expr.charAt(this.index) >= '0') || this.y_expr.charAt(this.index) == '.'))
			num += this.y_expr.charAt(this.index++);

		result = parseFloat(num);

		this.wsSkip();

		return result;
	}



}

var y_expr;
var x_val;

function onClick() {
	y_expr = document.getElementById("y_expr").value;
	x_val = document.getElementById("x_val").value;
	var myEvalor = new JsExprEvalor(y_expr, parseFloat(x_val));
	document.write(myEvalor.expr());
}

//while (true) {
	document.write("y = ?");
	document.write("x ?")
	onClick();

//}