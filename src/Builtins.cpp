#include "Builtins.hpp"
#include "ClassCompile.hpp"

ASTType Builtins::type_integer(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "int");
ASTType Builtins::type_string(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "string");
ASTType Builtins::type_function(dynamic_cast<ASTBlock*>(&ClassCompile::root_class), "function");

void Builtins::install_types()
{
	type_integer.confirmParent();
	type_string.confirmParent();
	type_function.confirmParent();
}
