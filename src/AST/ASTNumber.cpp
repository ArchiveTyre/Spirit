#include "ASTNumber.hpp"
#include <iostream>
#include "../ClassCompile.hpp"
#include "../Builtins.hpp"

void ASTNumber::debugSelf()
{
	std::cout << this->value;
}

bool ASTNumber::compileToBackend(ClassCompile *compile_dest)
{
	compile_dest->output_stream << this->value;
	return true;
}

bool ASTNumber::compileToBackendHeader(ClassCompile *compile_dest)
{
	compile_dest->output_header_stream << this->value;
	return true;
}

ASTNumber::ASTNumber(ASTBase *parent, int value) : ASTBase(parent)
{
	std::cout << "Creating number of value: " << value << std::endl;
	this->value = value;
}

ASTType * ASTNumber::getExpressionType()
{
	std::cout << "Getting expression type." << std::endl;
	return &Builtins::type_integer;
}
