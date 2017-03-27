#include "Tests.hpp"
#include <sstream>
#include "ClassCompile.hpp"

Tests::Tests(std::string new_test_name) : test_name(new_test_name)
{
	
}

bool Tests::testParser(std::string in, std::string out)
{
	std::istringstream stream_in(in);	
	Lexer lexer(stream_in, test_name);	
	Parser parser(lexer);	
	ClassCompile compile_dest(test_name.append(".ch"));
	compile_dest.output_stream = std::ostringstream();
	
	bool step1 = parser.parseInput(&compile_dest.class_ast);
	bool step2 = compile_dest.class_ast.compileToBackend(&compile_dest);
	bool step3 = out.compare(compile_dest.output_stream.str()) == 0;
	
	if (!step3)
		std::cout << "Compiled out: " << compile_dest.output_stream.str() << std::endl;
	
	bool result = step1 && step2 && step3;
	
	if (!result)
		std::cout << "Test: " << test_name << " failed." << std::endl;
	
	return result;
	
}

