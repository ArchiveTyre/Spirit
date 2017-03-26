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
	
	/**
	 * Creates a basic parser.
	 */
	Parser(Lexer input_lexer);
	
	bool parseInput(ASTClass *class_dest);
	
	static const std::string FUNCTION_KEYWORD;
	
private:
	
	/** The lexer of this parser. */
	Lexer lexer;
	Token *look_ahead = nullptr;
	Token *previous = nullptr;
	
	bool match(std::string value);
	bool match(Token::TOKEN_TYPE type);
	bool expect(std::string value);
	
	ASTBase * parseLine();
	ASTBase * parseExpression();
};

#endif
