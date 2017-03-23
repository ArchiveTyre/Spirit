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

ASTNumber::ASTNumber(int value)
{
	this->value = value;
}
