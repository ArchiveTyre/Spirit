#include "Parser.hpp"
#include <vector>
#include <string>
#include "AST/ASTNumber.hpp"
#include "AST/ASTFunctionCall.hpp"

using std::vector;
using std::string;

const string Parser::FUNCTION_KEYWORD = "fun";

Parser::Parser(Lexer input_lexer) : lexer(input_lexer)
{
	
}

ASTBase * Parser::parseExpression()
{
	
	if (match(Token::TOKEN_INTEGER) || match(Token::TOKEN_SYMBOL) {
		
		/** Left hand side of operator. */
		ASTBase *left;
		
		/** If number, create ASTNumber. */
		if (previous->token_type == Token::TOKEN_NUMBER) {
			left = new ASTNumber(stoi(previous->value));
		}
		
		/** If symbol, create ASTSymbol. */
		else if (previous->token_type == Token::TOKEN_SYMBOL) {
			left = new ASTSymbol(previous->value);
		}
		
		if (match(Token::TOKEN_OPERATOR)) {
			string operator_name = previous->value;
			if (auto right = parseExpression()) {
				auto operator_call = new ASTFunctionCall(operator_name);
				operator_call->insertArg(left);
				operator_call->insertArg(right);
				operator_call->is_infix = true;
				return operator_call;
			}
		}
		else {
			cout << "ERROR DESU!";
			return nullptr;
		}
	}
	else {
		cout << "ERROR DESU 2!";
		return nullptr;
	}
	
}

ASTBase * Parser::parseLine()
{
	return parseExpression();
}

bool Parser::parseInput(ASTClass * class_dest)
{
	look_ahead = lexer.lexToken();
	class_dest->insertNewCode(parseLine());
	
	/* FIXME */
	return true;
}

bool Parser::match(std::string value)
{
	if (look_ahead->value.compare(value)) {
		look_ahead = lexer.lexToken();
		return true;
	}
	else {
		return false;
	}
}

bool Parser::match(Token::TOKEN_TYPE type)
{
	if (look_ahead->token_type == type) {
		previous = look_ahead;
		look_ahead = lexer.lexToken();
		return true;
	}
	else {
		return false;
	}
}

bool Parser::expect(std::string value)
{
	if (match(value)) {
		return true;
	}
	else {
		std::cout << "ERROR ON LINE: " << look_ahead->line_no << " Expected: " << value;
		return false;
	}
}

