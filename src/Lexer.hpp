/**
 * @class Lexer
 * 
 * A lexer, also known as a tokenizer takes in input and gives out tokens.
 * 
 * @author Tyrerexus
 * @date 26 March 2017
 */

#pragma once
#ifndef LEXER_HPP
#define LEXER_HPP

#include <string>
#include <istream>

/**
 * @class Token
 * The result of lexToken()
 */
class Token {
public:
	typedef enum {
		TOKEN_INTEGER,
		TOKEN_STRING,
		TOKEN_SYMBOL,
		TOKEN_OPERATOR,
		TOKEN_LPAREN,
		TOKEN_RPAREN,
	} TOKEN_TYPE; 
	
	/** The type of this token. */
	TOKEN_TYPE token_type;
	
	/** What was interpreted as a token. */
	std::string value;
	
	/** The column that this token was defined on. */
	int column_no;
	
	/** The line number that this token was define on. */
	int line_no;
	
	/** The only constructor for Token
	 * @param token_type The type of the token that should be constructed.
	 * @param value The string represntation of this token.
	 * @param column_no The column that this token was defined on.
	 * @param line_no The line number that this token was define on.
	 */
	Token(Token::TOKEN_TYPE token_type, std::string value, int column_no, int line_no);
};

class Lexer {
public:
	
		/*** METHODS ***/
		
		/** Automatically test this Lexer to assure that it is sane.
		 */
		static void unitTest();
		
		/** Creates basic lexer.
		 * @param input_stream Where the lexer should read from.
		 */
		Lexer(std::istream& input_stream);
		
		/** Get one token from the input stream.
		 * @return The token that was parsed. Is null on failure.
		 */
		Token * lexToken();
private:
	
		/*** MEMBER VARIABLES ***/
		
		/** Where the lexer is reading from. */
		std::istream& input;
		
		/*** METHODS ***/
		
		/** Safely reads one char.
		 */
		char readChar();
		
		/** Ungets a previouslt read char that was read using readChar().
		 */
		void ungetChar(char c);
		
		int line_no;
		int column_no;
		
		/** Store the old column_no in case we undo a newline. */
		int old_column_no;
};

#endif
