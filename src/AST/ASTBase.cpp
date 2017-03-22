#include "ASTBase.hpp"
#include <iostream>

using std::string;
using std::cout;
using std::endl;

//

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
	extern int yylineno;
	extern int line_indent;

	this->line_no = yylineno;
	this->indentation_level = line_indent;
}
