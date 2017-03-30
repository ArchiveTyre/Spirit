#include "ASTWithArgs.hpp"
#include "../ClassCompile.hpp"
#include "ASTBase.hpp"

using std::string;

ASTWithArgs::ASTWithArgs(ASTBase *parent) : ASTBase(parent) 
{
	
}

ASTWithArgs::~ASTWithArgs()
{
	for(auto arg : arg_nodes) {
		delete arg;
	}
}

void ASTWithArgs::debugSelf()
{
	std::cout << '(';	
	for (auto arg : this->arg_nodes) {
		arg->debugSelf();
		if (arg != arg_nodes.back()) {
			std::cout << ", ";
		}
	}
	std::cout << ')';
}

void ASTWithArgs::exportSymToStream(std::ostream& output)
{
	output << "";
	return;
}

bool ASTWithArgs::compileToBackend(ClassCompile *compile_dest)
{

	compile_dest->output_stream << '(';	
	for (ASTBase* arg : arg_nodes) {
		if (!arg->compileToBackend(compile_dest))
			return false;
		if (arg != arg_nodes.back()) {
			compile_dest->output_stream << ", ";
		}
	}
	compile_dest->output_stream << ')';
	return true;
}


ASTBase * ASTWithArgs::findSymbolScan(string name)
{
	for (auto sub : arg_nodes) {
		if (auto result = sub->findSymbol(name)) {
			return result;
		}
	}
	return ASTBase::findSymbol(name);
}
