#include "Parser.hpp"
#include <assert.h>
#include <vector>
#include <string>
#include <sstream>
#include "AST/ASTNumber.hpp"
#include "AST/ASTFunctionCall.hpp"
#include "AST/ASTSymbol.hpp"
#include "AST/ASTDefineVariable.hpp"

using std::vector;
using std::string;

const string Parser::FUNCTION_KEYWORD = "fun";

Parser::Parser(Lexer input_lexer) : lexer(input_lexer)
{
	
}

Parser::~Parser()
{
	if (previous != nullptr) {
		delete previous;
	}
	if (look_ahead != nullptr) {
		delete look_ahead;
	}
}

ASTBase * Parser::parseExpression(ASTBase *parent)
{
	
	if (match(Token::TOKEN_LPAREN) || match(Token::TOKEN_INTEGER) || match(Token::TOKEN_SYMBOL)) {
		
		/** Left hand side of operator. */
		ASTBase *left = nullptr;
		
		/** If number, create ASTNumber. */
		if (previous->token_type == Token::TOKEN_INTEGER) {
			left = new ASTNumber(parent, stoi(previous->value));
			left->line_no = previous->line_no;
		}
		
		/** If symbol, create ASTSymbol. */
		else if (previous->token_type == Token::TOKEN_SYMBOL) {
			
			/** If it is followed by a parenthesis then it's a function call. */
			if (look_ahead->token_type == Token::TOKEN_LPAREN) {
			
				ASTFunctionCall *function_call = new ASTFunctionCall(parent, previous->value);
				
				do {
					/* Parse and insert. */
					auto parsed = parseExpression(function_call);
					parsed->confirmParent();
				}
				while(match(","));
				
				return function_call;
			}
			else {
				left = new ASTSymbol(parent, previous->value);
				left->line_no = previous->line_no;
			}
		}
		
		/** If parenthesis, parse parenthesis. */
		else if (previous->token_type == Token::TOKEN_LPAREN) {
			left = parseExpression(parent);
			if (!match(Token::TOKEN_RPAREN)) {
				syntaxError("closing parenthesis");
				return nullptr;
			}
		}
			
		/** If next is a operator we need to parse next expression as an operator. */
		if (match(Token::TOKEN_OPERATOR)) {
			string operator_name = previous->value;
			auto operator_call = new ASTFunctionCall(parent, operator_name);
			if (auto right = parseExpression(operator_call)) {
				
				operator_call->line_no = previous->line_no;
				left->confirmParent(operator_call);
				right->confirmParent(operator_call);
				operator_call->confirmParent();
				
				operator_call->is_infix = true;
				return operator_call;
			}
			else {
				delete operator_call;
				return nullptr;
			}
		}
		
		/** Check if this is the last token in this expression. */
		else if (look_ahead->token_type == Token::TOKEN_NEW_LINE || 
				 look_ahead->token_type == Token::TOKEN_RPAREN ||
				 look_ahead->token_type == Token::TOKEN_EOF ) {
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

ASTBase * Parser::parseLine(ASTClass *class_dest)
{	
	
	/* Get indentation level & parent. */
	int indentation_level = 0;
	while (match(Token::TOKEN_INDENT)) {
		indentation_level += 4;
	}
	ASTBase *parent = class_dest->getParentForNewCode(indentation_level);
	
	/* Do the parsing. */
	ASTBase *parsed_line;
	
	/* It's a variable definition. */
	if (match("var")) {
		if (match(Token::TOKEN_SYMBOL)) {
			string name = previous->value;
			if (match("=")) {
				// Assign default value.
				ASTBase *value = parseExpression(parent);
				parsed_line = new ASTDefineVariable(parent, name, value);
				value->confirmParent(parsed_line);
			}
			else {
				syntaxError("variable value");
				parsed_line = nullptr;
			}
		}
		else {
			syntaxError("variable name");
			parsed_line = nullptr;
		}
	}
	
	/* It's an expression. */
	else {
		parsed_line = parseExpression(parent);	
	}
	
	/* Finish off. */	
	if (parsed_line && (match(Token::TOKEN_NEW_LINE) || match(Token::TOKEN_EOF))) {
		parsed_line->indentation_level = indentation_level;
		parsed_line->confirmParent();
		return parsed_line;
	}
	else
		return nullptr;
}

bool Parser::parseInput(ASTClass * class_dest)
{
	/* Look ahead. */
	look_ahead = lexer.lexToken();
	
	/* Parse as many lines as possible. */
	while (auto result = parseLine(class_dest)) {
	}
	
	/* Then check for trash. */
	if (!match(Token::TOKEN_EOF)) {
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
		if (previous != nullptr) {
			delete previous;
			previous = nullptr;
		}
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

bool Parser::match(Token::TOKEN_TYPE type)
{
	
	/* Assume that look_ahead EOF warning is already done by previous match(). */
	if (look_ahead != nullptr && look_ahead->token_type == type) {
		if (previous != nullptr) {
			delete previous;
			previous = nullptr;
		}
		
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
		std::istringstream input("var A = 32\nA = 64");
		
		Lexer lexer(&input, "Test1.ch");
		
		Parser parser(lexer);
		
		ASTClass dest(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "KawaiiClass1");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
		ClassCompile::root_class.child_nodes.remove(dynamic_cast<ASTBlock*>(&dest));
	}
	
	/* Test 1.5: */
	{
		std::istringstream input("var a = 32");
		
		Lexer lexer(&input, "Test1_5.ch");
		
		Parser parser(lexer);
		
		ASTClass dest(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "KawaiiClass1.5");
		
		parser.parseInput(&dest);
		
		assert(((ASTBlock)dest).findSymbol("a") != nullptr);
		
		ClassCompile::root_class.child_nodes.remove(dynamic_cast<ASTBlock*>(&dest));
	}
	
	/* Test 2: Int addition. */
	{
		std::istringstream input("var A = 1+2");
		
		Lexer lexer(&input, "Test2.ch");
		
		Parser parser(lexer);
		
		ASTClass dest(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "KawaiiClass2");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
		ClassCompile::root_class.child_nodes.remove(dynamic_cast<ASTBlock*>(&dest));
	}

	/* Test 3: Multiline. */
	{
		std::istringstream input("var A = 1+2\nvar B= A+4\n var C=3");
		
		Lexer lexer(&input, "Test3.ch");
		
		Parser parser(lexer);
		
		ASTClass dest(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "KawaiiClass3");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
		ClassCompile::root_class.child_nodes.remove(dynamic_cast<ASTBlock*>(&dest));
	}
	
	/* Test 4: Tabs. */
#	if 0
	{
		std::istringstream input("\
A = 32\n\
if A == 32:\n\
\tB= 43\n\
		");
		
		Lexer lexer(&input, "Test4.ch");
		
		Parser parser(lexer);
		
		ASTClass dest(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "KawaiiClass4");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
		ClassCompile::root_class.child_nodes.remove(dynamic_cast<ASTBlock*>(&dest));
	}
#	endif
	/* Test 5: Parenthesis. */
	{
		std::istringstream input("A = 3(3 + 4) * 2");
		
		Lexer lexer(&input, "Test5.ch");
		
		Parser parser(lexer);
		
		ASTClass dest(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "KawaiiClass5");
		
		parser.parseInput(&dest);
		
		dest.debugSelf();
		ClassCompile::root_class.child_nodes.remove(dynamic_cast<ASTBlock*>(&dest));
		
		std::cout << std::endl;
	}
}


