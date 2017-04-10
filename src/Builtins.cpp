#include "Builtins.hpp"
#include "ClassCompile.hpp"

ASTType *Builtins::type_integer;
ASTType *Builtins::type_string;
ASTType *Builtins::type_function;

void Builtins::install_types()
{
	type_integer = new ASTType(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "int");
	type_string = new ASTType(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "string");
	type_function = new ASTType(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "function");
	
	type_integer->confirmParent();
	type_string->confirmParent();
	type_function->confirmParent();
}
