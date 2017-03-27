#include "Parser.hpp"
#include <vector>
#include <string>
#include <sstream>
#include "AST/ASTNumber.hpp"
#include "AST/ASTFunctionCall.hpp"
#include "AST/ASTSymbol.hpp"

using std::vector;
using std::string;

const string Parser::FUNCTION_KEYWORD = "fun";

Parser::Parser(Lexer input_lexer) : lexer(input_lexer)
{
	
}

ASTBase * Parser::parseExpression()
{
	
	if (match(Token::TOKEN_LPAREN) || match(Token::TOKEN_INTEGER) || match(Token::TOKEN_SYMBOL)) {
		
		/** Left hand side of operator. */
		ASTBase *left = nullptr;
		
		/** If number, create ASTNumber. */
		if (previous->token_type == Token::TOKEN_INTEGER) {
			left = new ASTNumber(stoi(previous->value));
			left->line_no = previous->line_no;
		}
		
		/** If symbol, create ASTSymbol. */
		else if (previous->token_type == Token::TOKEN_SYMBOL) {
			left = new ASTSymbol(previous->value);
			left->line_no = previous->line_no;
		}
		
		/** If parenthesis, parse parenthesis. */
		else if (previous->token_type == Token::TOKEN_LPAREN) {
			left = parseExpression();
			if (!match(Token::TOKEN_RPAREN)) {
				syntaxError("closing parenthesis");
				return nullptr;
			}
		}
			
		/** If next is a operator we need to parse next expression as an operator. */
		if (match(Token::TOKEN_OPERATOR)) {
			string operator_name = previous->value;
			if (auto right = parseExpression()) {
				auto operator_call = new ASTFunctionCall(operator_name);
				operator_call->line_no = previous->line_no;
				operator_call->insertArg(left);
				operator_call->insertArg(right);
				operator_call->is_infix = true;
				return operator_call;
			}
			else {
				return nullptr;
			}
		}
		
		/** Check if this is the last token in this expression. */
		else if (look_ahead->token_type == Token::TOKEN_TYPE::TOKEN_NEW_LINE || 
				 look_ahead->token_type == Token::TOKEN_TYPE::TOKEN_RPAREN ||
				 look_ahead->token_type == Token::TOKEN_TYPE::TOKEN_EOF ) {
			return left;
		}
		else {
			syntaxError("(, int, symbol");
			return nullptr;
		}
	}
	
	/* ??? */
	else {
		return nullptr;
	}
}

ASTBase * Parser::parseLine()
{	
	int indentation_level = 0;
	
	while (match(Token::TOKEN_TYPE::TOKEN_INDENT)) {
		indentation_level += 4;
	}
	
	ASTBase *expr = parseExpression();		
	
	if (expr && (match(Token::TOKEN_TYPE::TOKEN_NEW_LINE) || match(Token::TOKEN_TYPE::TOKEN_EOF))) {
		expr->indentation_level = indentation_level;
		return expr;
	}
	else
		return nullptr;
}

bool Parser::parseInput(ASTClass * class_dest)
{
	/* Look ahead. */
	look_ahead = lexer.lexToken();
	
	/* Parse as many lines as possible. */
	while (auto result = parseLine()) {
		class_dest->insertNewCode(result);
	}
	
	/* Then check for trash. */
	if (!match(Token::TOKEN_TYPE::TOKEN_EOF)) {
		syntaxError("end of file or operator");
		return false;
	}
	else {
		return true;
	}
}

bool Parser::match(std::string value)
{
	
	/* Assume that look_ahead EOF warning is already done by previous match(). */
	if (look_ahead != nullptr && look_ahead->value.compare(value) == 0) {
		look_ahead = lexer.lexToken();
		if (look_ahead != nullptr)
			return true;
		else {
			std::cout << "EOF!" << std::endl;
			return false;
		}
	}
	else {
		return false;
	}
}

bool Parser::match(Token::TOKEN_TYPE type)
{
	
	/* Assume that look_ahead EOF warning is already done by previous match(). */
	if (look_ahead != nullptr && look_ahead->token_type == type) {
		previous = look_ahead;
		look_ahead = lexer.lexToken();
		if (look_ahead != nullptr)
			return true;
		else {
			std::cout << "EOF!" << std::endl;
			return false;
		}
	}
	else {
		return false;
	}
}

void Parser::syntaxError(std::string expected, Token* token)
{
	std::cout << "Syntax error in file: " << lexer.file_name_on_error << ':';
	std::cout << token->line_no + 1 << std::endl;
	std::cout << "    Expected: \"" << expected << "\" But got: \"" << token->value << "\"" << std::endl; 
}

void Parser::syntaxError(std::string expected)
{
	syntaxError(expected, look_ahead);
}

void Parser::unitTest()
{
	/* Test 1: Basic assignment. */
	{
		std::istringstream input("A = 32\n");
		
		Lexer lexer(input, "Test.1ch");
		
		Parser parser(lexer);
		
		ASTClass dest("KawaiiClass1");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
	}
	
	/* Test 2: Int addition. */
	{
		std::istringstream input("A = 1+2");
		
		Lexer lexer(input, "Test2.ch");
		
		Parser parser(lexer);
		
		ASTClass dest("KawaiiClass2");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
	}

	/* Test 3: Multiline. */
	{
		std::istringstream input("A = 1+2\nB= A+4\nC=3");
		
		Lexer lexer(input, "Test3.ch");
		
		Parser parser(lexer);
		
		ASTClass dest("KawaiiClass3");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
	}
	
	/* Test 4: Tabs. */
	{
		std::istringstream input("\
A = 32\n\
if A == 32:\n\
\tB= 43\n\
		");
		
		Lexer lexer(input, "Test4.ch");
		
		Parser parser(lexer);
		
		ASTClass dest("KawaiiClass4");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
	}

	/* Test 5: Parenthesis. */
	{
		std::istringstream input("A = 3(3 + 4) * 2");
		
		Lexer lexer(input, "Test5.ch");
		
		Parser parser(lexer);
		
		ASTClass dest("KawaiiClass5");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
		
		std::cout << std::endl;
	}
}


