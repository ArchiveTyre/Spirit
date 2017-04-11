#include "ASTFunctionCall.hpp"
#include "../Builtins.hpp"

ASTFunctionCall::ASTFunctionCall(ASTBase *parent, std::string function_name)
: ASTBase(parent)
, ASTNamed(parent, function_name)
, ASTWithArgs(parent)
{
	std::cout << "Creating funciton call" << std::endl;
}



void ASTFunctionCall::debugSelf()
{
	ASTNamed::debugSelf();
	ASTWithArgs::debugSelf();
}

bool ASTFunctionCall::compileToBackend(ClassCompile *compile_dest)
{
	if (is_infix) {
		/* arg1 our_name arg2. */
		this->arg_nodes.front()->compileToBackend(compile_dest);
		ASTNamed::compileToBackend(compile_dest);
		this->arg_nodes.back()->compileToBackend(compile_dest);
	}
	else {
		/* our_name argN... */
		ASTNamed::compileToBackend(compile_dest);
		ASTWithArgs::compileToBackend(compile_dest);
	}
	return true;
}

bool ASTFunctionCall::compileToBackendHeader(ClassCompile *compile_dest)
{
	if (is_infix) {
		/* arg1 our_name arg2. */
		this->arg_nodes.front()->compileToBackendHeader(compile_dest);
		ASTNamed::compileToBackendHeader(compile_dest);
		this->arg_nodes.back()->compileToBackendHeader(compile_dest);
	}
	else {
		/* our_name argN... */
		ASTNamed::compileToBackendHeader(compile_dest);
		ASTWithArgs::compileToBackendHeader(compile_dest);
	}
	return true;
}

void ASTFunctionCall::exportSymToStream(std::ostream& output)
{
	return;
}

ASTType * ASTFunctionCall::getExpressionType()
{
		if (is_infix) {
			if (arg_nodes.front()->getExpressionType() == Builtins::type_integer
				&& arg_nodes.back()->getExpressionType() == Builtins::type_integer
			) {
				return Builtins::type_integer;
			}
		}
		return nullptr;
}

ASTFunctionCall::~ASTFunctionCall()
{

}
