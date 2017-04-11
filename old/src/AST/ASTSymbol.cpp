#include "ASTSymbol.hpp"

ASTSymbol::ASTSymbol(ASTBase *parent, std::string name)
: ASTBase (parent)
, ASTNamed(parent, name)
{
	std::cout << "Creating symbol!" << std::endl;
	definition = dynamic_cast<ASTDefineVariable*>(findSymbol(ast_name));
	if (definition == nullptr) {
		std::cout << "ERROR: Variable " << ast_name << " is undefined!" << std::endl;
	}
}

ASTType * ASTSymbol::getExpressionType()
{
	return this->definition->ast_type;
}
