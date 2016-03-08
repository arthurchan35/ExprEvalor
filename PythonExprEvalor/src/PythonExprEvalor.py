
#Created by arthurchan35 on 2/25/2016.

#<expr> : <term> {(+ | -)<term>}
#<term> : <factor> {(* | /)<term>}
#<factor> : ID | INT_CONST | ( <expr> )
import sys
import math
class PythonExprEvalor:
	#y_expr
	#x_val
	#index
	#length
	#parenthesis

	def __init__(self, y_expr, x_val):
		self.y_expr = str(y_expr)
		self.x_val = float(x_val)
		self.index = 0
		self.length = len(str(y_expr))
		self.parenthesis = []

	def factor(self):
		self.wsSkip()

		if (self.y_expr[self.index] == 'x') :
			self.index += 1
			return self.x_val

		if (self.y_expr[self.index] == 's' and self.y_expr[self.index + 1] == 'i' and
			self.y_expr[self.index + 2] == 'n' and self.y_expr[self.index + 3] == '('):
			self.index += 3
			result = self.parenthOp()
			return math.sin(result)

		if (self.y_expr[self.index] == 'c' and self.y_expr[self.index + 1] == 'o' and
			self.y_expr[self.index + 2] == 's' and self.y_expr[self.index + 3] == '('):
			self.index += 3
			result = self.parenthOp()
			return math.cos(result)		

		if (self.y_expr[self.index] == 't' and self.y_expr[self.index + 1] == 'a' and
			self.y_expr[self.index + 2] == 'n' and self.y_expr[self.index + 3] == '('):
			self.index += 3
			result = self.parenthOp()
			return math.tan(result)

		if (self.y_expr[self.index] == 'l' and self.y_expr[self.index + 1] == 'n' and
			self.y_expr[self.index + 2] == '('):
			self.index += 2
			result = self.parenthOp()
			return math.log(result)

		if (self.y_expr[self.index] == 'a' and self.y_expr[self.index + 1] == 't' and
			self.y_expr[self.index + 2] == 'a' and self.y_expr[self.index + 3] == 'n' and
			self.y_expr[self.index + 4] == '2' and self.y_expr[self.index + 5] == '('):
			self.index += 5
			result = self.parenthOp2()
			return math.atan2(result[0], result[1])

		if (self.y_expr[self.index] == 'a' and self.y_expr[self.index + 1] == 't' and
			self.y_expr[self.index + 2] == 'a' and self.y_expr[self.index + 3] == 'n' and
			self.y_expr[self.index + 4] == '('):
			self.index += 4
			result = self.parenthOp()
			return math.atan(result)

		if (self.y_expr[self.index] == '('):
			return self.parenthOp()

		num = ""
		result = 0.0

		while (self.index < self.length and ((self.y_expr[self.index] <= '9' and 
			self.y_expr[self.index] >= '0') or self.y_expr[self.index] == '.')):
			num += self.y_expr[self.index]
			self.index += 1

		try:	
			result = float(num)
		except ValueError:
			print "at index of " + str(self.index)
			print "not a value" + num
			sys.exit(-1)

		self.wsSkip()
		return result

	def parenthOp2(self):
		result = []
		self.index += 1
		self.parenthesis.append('(')
		result.append(self.expr())
		self.wsSkip()
		if (self.index == self.length or self.y_expr[self.index] != ','):
			sys.exit(-1)

		self.index += 1
		self.wsSkip()
		result.append(self.expr())
		self.wsSkip()
		if (self.index == self.length or self.y_expr[self.index] != ')' or
			not self.parenthesis or self.parenthesis.pop() != '(') :
			sys.exit(-1)

		self.index += 1
		self.wsSkip()
		return result

	def parenthOp(self):
		self.index += 1
		self.parenthesis.append('(')
		result = self.expr()
		self.wsSkip()
		if (self.index == self.length or self.y_expr[self.index] != ')' or
			not self.parenthesis or self.parenthesis.pop() != '(') : 
			sys.exit(-1)

		self.index += 1
		self.wsSkip()
		return result

	def wsSkip(self):
		while (self.index < self.length and (self.y_expr[self.index] == ' ' or 
			self.y_expr[self.index] == '\t')):
			self.index += 1

	def pow(self):
		result = self.factor()
		while (self.index < self.length and self.y_expr[self.index] == '^'):
			self.index += 1
			result = math.pow(result, self.factor())
		return result

	def term(self): 
		result = self.pow()
		while (self.index < self.length and (self.y_expr[self.index] == '*' or self.y_expr[self.index] == '/')):
			if (self.y_expr[self.index] == '*'):
				self.index += 1
				result *= self.pow()
			else:
				self.index += 1
				try:
					result /= self.pow()
				except ValueError:
					print "divided by zero"
					sys.exit(-1)
		return result			

	def expr(self):
		result = self.term()
		while (self.index < self.length and (self.y_expr[self.index] == '+' or self.y_expr[self.index] == '-')):
			if (self.y_expr[self.index] == '+'):
				self.index += 1
				result += self.term()
			else:
				self.index += 1
				result -= self.term()
		return result

while (True):	
	y_expr = input("Y = ? ")
	x_val = input("X ? ")

	pee = PythonExprEvalor(y_expr, x_val)
	result = pee.expr()
	if (not pee.parenthesis):
		print result
	else:
		print "Too few closing parenthesis in expression."
		print len(pee.parenthesis)
		print pee.parenthesis
		sys.exit(-1)

