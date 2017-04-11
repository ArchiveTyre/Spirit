#include "Tests.hpp"
#include <sstream>
#include "Builtins.hpp"
#include "ClassCompile.hpp"

Tests::Tests(std::string new_test_name) : test_name(new_test_name)
{
	
}

bool Tests::testParser(std::string in, std::string source_out, std::string out_header)
{
	std::istringstream stream_in(in);	
	Lexer lexer(&stream_in, test_name);	
	Parser parser(lexer);	
	ClassCompile compile_dest(test_name.append(".ch"));
	compile_dest.output_stream = std::stringstream();
	
	bool step1 = parser.parseInput(compile_dest.class_ast);
	bool step2 = compile_dest.class_ast->compileToBackend(&compile_dest);
	bool step3 = source_out.compare(compile_dest.output_stream.str()) == 0;
	bool step4 = out_header.compare(compile_dest.output_header_stream.str()) == 0;
	
	if (!step3)
		std::cout << "Compiled out: " << compile_dest.output_stream.str() << std::endl;
	if (!step4)
		std::cout << "Compiled header out: " << compile_dest.output_header_stream.str() << std::endl;
	
	bool result = step1 && step2 && step3 && step4;
	
	if (!result)
		std::cout << "Test: " << test_name << " failed." << std::endl;
	
	return result;
	
}

#ifdef TEST
int main() {
	Tests::testAll();
}
#endif

void Tests::testAll()
{
	/* First init everything that is necessary to compile stuff. */
	Builtins::install_types();
	
	ASTBase::unitTest();
	Lexer::unitTest();
	Parser::unitTest();
	
	std::cout << "=== UNIT TESTING GENERICS ===" << std::endl;
	Tests test1("Test 1");
	bool t1 = test1.testParser("var A = 32", "{\nA=32}\n", "");
	
	Tests test2("Test 2");
	bool t2 = test2.testParser("var A = 32\nA= A+ A", "{\nA=32+A}\n", "");
	
	exit(t1 && t2 != 1);
		
}


