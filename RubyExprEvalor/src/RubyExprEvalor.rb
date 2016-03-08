
# Created by arthurchan35 on 2/25/2016.

# <expr> : <term> {(+ | -)<term>}
# <term> : <factor> {(* | /)<term>}
# <factor> : ID | INT_CONST | ( <expr> )


class RubyExprEvalor

	def initialize(y_expr, x_val)
		@y_expr = y_expr
		@x_val = x_val
		@index = 0
		@length = y_expr.length
		@parenthesis = Array.new
	end

	def expr
		result = term()
		while (@index < @length and (@y_expr[@index] == '+' or @y_expr[@index] == '-')) do 
			if (@y_expr[@index] == '+')
				@index += 1	
				result += term()
			else
				@index += 1		
				result -= term()
			end		
		end
		return result
	end

	def term
		result = pow()
		while (@index < @length and (@y_expr[@index] == '*' or @y_expr[@index] == '/')) do
			if (@y_expr[@index] == '*')
				@index += 1				
				result *= pow()
			else
				@index += 1				
				dividor = pow()
				if (dividor == 0)
					abort('dividor cannot be zero')
				end
				result /= dividor
			end
		end
		return result;
	end

	def pow
		result = factor()
		while (@index < @length and (@y_expr[@index] == '^')) do
			@index += 1
			result = result ** factor()
		end
		return result
	end

	def wsSkip
		while (@index < @length and (@y_expr[@index] == ' ' or @y_expr[@index] == "\t")) do
			@index += 1
		end
	end

	def parenthOp
		@index += 1
		@parenthesis.push('(')
		result = expr()
		wsSkip()

		if (@index == @length or @y_expr[@index] != ')' or @parenthesis.empty? or @parenthesis.pop() != '(')
			abort('Incorrect closing parenthesis')
		end
		@index += 1
		wsSkip()
		return result
	end

	def parenthOp2
		result = Array.new
		@index += 1
		@parenthesis.push('(')
		result.push(expr())
		wsSkip()
		if (@index == @length or @y_expr[@index] != ',')
			abort('Missing comma')
		end
		@index += 1
		wsSkip()
		result.push(expr())
		wsSkip()
		if (@index == @length or @y_expr[@index] != ')' or @parenthesis.empty? or @parenthesis.pop() != '(')
			abort('Incorrect closing parenthesis')
		end
		@index += 1
		wsSkip()
		return result
	end

	def factor
		wsSkip()
		if (@y_expr[@index] == 'x')
			@index += 1
			return @x_val
		end

		if (@y_expr[@index] == 's' and @y_expr[@index + 1] == 'i' and @y_expr[@index + 2] == 'n' and @y_expr[@index + 3] == '(')
			@index += 3
			result = parenthOp()
			return Math.sin(result)
		end
		if (@y_expr[@index] == 'c' and @y_expr[@index + 1] == 'o' and @y_expr[@index + 2] == 's' and @y_expr[@index + 3] == '(')
			@index += 3
			result = parenthOp()
			return Math.cos(result)
		end	
		if (@y_expr[@index] == 't' and @y_expr[@index + 1] == 'a' and @y_expr[@index + 2] == 'n' and @y_expr[@index + 3] == '(')
			@index += 3
			result = parenthOp()
			return Math.tan(result)
		end			
		if (@y_expr[@index] == 'l' and @y_expr[@index + 1] == 'n' and @y_expr[@index + 2] == '(')
			@index += 2
			result = parenthOp()
			return Math.log(result)
		end	
		if (@y_expr[@index] == 'a' and @y_expr[@index + 1] == 't' and @y_expr[@index + 2] == 'a' and @y_expr[@index + 3] == 'n' and @y_expr[@index + 4] == '2' and @y_expr[@index + 5] == '(')
			@index += 5
			result = parenthOp2()
			return Math.atan2(result[0], result[1])
		end	
		if (@y_expr[@index] == 'a' and @y_expr[@index + 1] == 't' and @y_expr[@index + 2] == 'a' and @y_expr[@index + 3] == 'n' and @y_expr[@index + 4])
			@index += 4
			result = parenthOp()
			return Math.atan(result)
		end
		if (@y_expr[@index] == '(')
			return parenthOp()
		end

		num = ''
		result = 0.0
		while (@index < @length and ((@y_expr[@index] <= '9' && @y_expr[@index] >= '0') or @y_expr[@index] == '.')) do
			num += @y_expr[@index]
			@index += 1
		end
		result = num.to_f
		wsSkip()
		return result			
	end
end

while (true) do 
	puts('y = ?')
	y_expr = gets
	puts('x ?')
	x_val = gets
	ree = RubyExprEvalor.new(y_expr, x_val.to_f)
	#ree = RubyExprEvalor.new('atan(2^(2+x)*atan2(x/4 , 5 - 2 - 1))', 3.1415926)
	result = ree.expr()
	puts(result.to_s)
	puts('')
end






