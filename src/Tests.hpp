/**
 * @class Tests
 * 
 * A class for testing the compiler at compile-time.
 *
 * @author Tyrerexus
 * @date 27 March 2017
 */

#pragma once
#ifndef TESTS_HPP
#define TESTS_HPP

#include "Parser.hpp"
#include <string>

class Tests {
public:
	
	/*** METHODS ***/
	
	Tests(std::string test_name);
	
	bool testParser(std::string in, std::string source_out, std::string header_out);
	
	/** Tests a lot of small tests.
	 */
	static void testAll();
	
	/*** MEMBER VARIABLES ***/
	
	std::string test_name;
	
};

#endif
