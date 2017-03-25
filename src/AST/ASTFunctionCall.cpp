#include "ASTFunctionCall.hpp"

ASTFunctionCall::ASTFunctionCall(std::string function_name) : ASTNamed(function_name)
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


void ASTFunctionCall::exportSymToStream(std::ostream& output)
{
	return;
}
