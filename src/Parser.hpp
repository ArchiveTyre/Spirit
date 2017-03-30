/**
 * @class Parser
 * 
 * A mini-parser for the Cheri language.
 * 
 * @author Tyrerexus
 * @date 26 March 2017
 */

#pragma once
#ifndef PARSER_HPP
#define PARSER_HPP

#include "Lexer.hpp"
#include "AST/ASTClass.hpp"

class Parser {
public:
	
	/*** METHODS ***/
	
	/** Automatically test this Lexer to assure that it is sane.
	 */
	static void unitTest();
	
	/**
	 * Creates a basic parser.
	 * @param input_lexer The input for this parser.
	 */
	Parser(Lexer input_lexer);
	
	/** Parses the input given in the constructer.
	 * @return Returns true on success.
	 */
	bool parseInput(ASTClass *class_dest);
	
	/*** STATICS ***/
	
	/** The keyword for defining functions. */
	static const std::string FUNCTION_KEYWORD;
	
private:
	
	/** The lexer of this parser. */
	Lexer lexer;
	
	/** The token ahead of us. */
	Token *look_ahead = nullptr;
	
	/** The previous token parsed. */
	Token *previous = nullptr;
	
	/*** METHODS ***/
	
	/** Prints one syntax error.
	 * @param token The token that was unexpected.
	 * @param expected What was expected?
	 */
	void syntaxError(std::string expected, Token *token);
	
	/** Same as syntaxError(expected, look_ahead)
	 * @param expected What was expected?
	 */
	void syntaxError(std::string expected);
	
	bool match(std::string value);
	bool match(Token::TOKEN_TYPE type);
	
	ASTBase * parseLine(ASTClass *class_dest);
	ASTBase * parseExpression(ASTBase *parent);
};

#endif
