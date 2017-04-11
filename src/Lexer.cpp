#include "Lexer.hpp"
#include <sstream>
#include <iostream>
#include <string>
#include <assert.h>

using std::string;
using std::stringstream;

Token::Token(Token::TOKEN_TYPE token_type, string value, int column_no, int line_no)
{
	this->token_type = token_type;
	this->value = value;
	this->column_no = column_no;
	this->line_no = line_no;
}

char Lexer::readChar()
{
	
	++column_no;
	char c = input->get();
	
	if (c == '\n') {
		
		/* Save old column_no in case we want to undo this. */
		old_column_no = column_no;
		column_no = 0;
		++line_no;
	}
	
	return c;
}

void Lexer::ungetChar(char c)
{
	--column_no;
	if (c == '\n') {
		--line_no;
		column_no = old_column_no;
	}
	input->unget();
}

Lexer::Lexer(std::istream* input_stream, string file_name) 
: file_name_on_error(file_name)
, input(input_stream)
{
	
}

Token * Lexer::lexToken()
{
	
	/** The character we'll be starting the tokenization from. */
	char c = readChar();
	
	if (c == -1)
		return new Token(Token::TOKEN_TYPE::TOKEN_EOF, "", column_no, line_no);
	
	/* If character was space char then skip until it's not. */
	while (c == ' ')
		c = readChar();
	
	if (c == '\n')
		return new Token(Token::TOKEN_TYPE::TOKEN_NEW_LINE, "\n", column_no, line_no);
	
	else if (c == '\t')
		return new Token(Token::TOKEN_TYPE::TOKEN_INDENT, "\t", column_no, line_no);

	else if (c == '(')
		return new Token(Token::TOKEN_TYPE::TOKEN_LPAREN, "(", column_no, line_no);
	
	else if (c == ')')
		return new Token(Token::TOKEN_TYPE::TOKEN_RPAREN, ")", column_no, line_no);
	
	/* Check if first char is digit. */
	else if (isdigit(c)) {
		stringstream string_builder;
		
		/* If so then the rest of the digit make an integer. */
		while (isdigit(c) && c != -1) {
			string_builder << c;
			c = readChar();
			if (!input)
				break;
		}
		ungetChar(c);
		
		return new Token(Token::TOKEN_TYPE::TOKEN_INTEGER, string_builder.str(), column_no, line_no);
	}
	
	/* Check if first char is an alphabetical character. */
	else if (isalpha(c)) {
		stringstream string_builder;
		
		/* If so then the rest of the alpha+numeric characters make a symbol. */
		while (isalnum(c) && c != -1 && c != '_') {
			string_builder << c;
			c = readChar();
			if (!input)
				break;
		}
		ungetChar(c);
		
		return new Token(Token::TOKEN_TYPE::TOKEN_SYMBOL, string_builder.str(), column_no, line_no);
	}
	
	/* Otherwise it's an operator. */
	else {
		stringstream string_builder;
		
		/* Anything non alpha numeric is an operator. Excluding spaces and EOF. */
		while (!isalnum(c) && c != ' ' && c != '\t' && c != -1) {
			string_builder << c;
			c = readChar();
			if (!input)
				break;
		}
		ungetChar(c);
		
		return new Token(Token::TOKEN_TYPE::TOKEN_OPERATOR, string_builder.str(), column_no, line_no);
	}
}

static bool matches(Lexer *lexer, string name, Token::TOKEN_TYPE type)
{
	Token *token = lexer->lexToken();
	bool m = token->value.compare(name) == 0 && type == token->token_type;
	delete token;
	return m;
}

void Lexer::unitTest()
{
	std::cout << "=== UNIT TESTING LEXER ===" << std::endl;
	
	{
		std::istringstream input("A = 12 + f1");
		Lexer lex(&input, "Test.ch");
		
		assert(matches(&lex, "A", Token::TOKEN_SYMBOL));
		assert(matches(&lex, "=", Token::TOKEN_OPERATOR));
		assert(matches(&lex, "12", Token::TOKEN_INTEGER));
		assert(matches(&lex, "+", Token::TOKEN_OPERATOR));
		assert(matches(&lex, "f1", Token::TOKEN_SYMBOL));
		assert(matches(&lex, "", Token::TOKEN_EOF));
		
	}
}


