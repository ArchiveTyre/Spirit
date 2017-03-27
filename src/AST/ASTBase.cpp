#include "ASTBase.hpp"
#include <iostream>

using std::string;
using std::cout;
using std::endl;

bool ASTBase::compileToBackendHeader(ClassCompile *compile_dest)
{
	/* Not all AST nodes wish to be compiled into a header. Therefore it's okay for them to skip that. */
	return true;
}

void ASTBase::importSymFromStream(ASTBase *dest, std::istream& input)
{
	string ast_type;

	while (input >> ast_type) {
		cout << "Read from file: " << ast_type << endl;
	}
}

void ASTBase::exportSymToStream(std::ostream& output)
{
	/* Do nothing. */
	return;
}

void ASTBase::debugSelf()
{
	printf("A compiler error");
}

ASTBase::ASTBase()
{

	this->line_no = -1;
	this->indentation_level = -1;
	this->parent_node = nullptr;
	
	std::cout << "Calling BASE constructor" << std::endl;
}
