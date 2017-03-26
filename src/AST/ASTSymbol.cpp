#include "ASTSymbol.hpp"

ASTSymbol::ASTSymbol(std::string name) : ASTNamed(name)
{
	std::cout << "Creating symbol!" << std::endl;
}

