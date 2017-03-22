#include "ASTClass.hpp"
#include <string>
#include "../ClassCompile.hpp"

using std::string;

ASTClass::ASTClass(std::string class_name)
{
	this->ast_name = class_name;
}

void ASTClass::exportSymToStream(std::ostream& output)
{
	ASTNamed::exportSymToStream(output);
}

bool ASTClass::compileToBackend(ClassCompile *compile_dest)
{

	/* Write the name. */
	compile_dest->output_stream << "class ";
	ASTNamed::compileToBackend(compile_dest);

	return true;
}