#include "ASTNamed.hpp"
#include "../ClassCompile.hpp"

void ASTNamed::exportSymToStream(std::ostream& output)
{
	output << this->ast_name;
}

bool ASTNamed::compileToBackend(ClassCompile *compile_dest)
{
	compile_dest->output_stream << this->ast_name;
	return true;
}

void ASTNamed::debugSelf()
{
	std::cout << this->ast_name;
}

ASTNamed::ASTNamed(std::string new_name) : ASTBase()
{
	this->ast_name = new_name;
	std::cout << "Creating named AST: " << new_name << std::endl;
}
