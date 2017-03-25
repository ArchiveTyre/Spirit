#include "ASTNumber.hpp"
#include "../ClassCompile.hpp"
#include <iostream>

void ASTNumber::debugSelf()
{
	std::cout << this->value;
}

bool ASTNumber::compileToBackend(ClassCompile *compile_dest)
{
	compile_dest->output_stream << this->value;
	return true;
}

ASTNumber::ASTNumber(int value) : ASTBase()
{
	std::cout << "Creating number of value: " << value << std::endl;
	this->value = value;
}
