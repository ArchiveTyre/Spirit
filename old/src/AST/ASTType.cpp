#include "ASTType.hpp"
#include "../ClassCompile.hpp"

ASTType::ASTType(ASTBase *parent, std::string type_name)
: ASTBase(parent)
, ASTNamed(parent, type_name)
{
	
}


bool ASTType::compileToBackend(ClassCompile* compile_dest)
{
	bool s1 = ASTNamed::compileToBackend(compile_dest);
	compile_dest->output_stream << ' ';
	return s1;
}

bool ASTType::compileToBackendHeader(ClassCompile* compile_dest)
{
	 bool s1 = ASTNamed::compileToBackendHeader(compile_dest);
	 compile_dest->output_header_stream << ' ';
	 return s1;
}

void ASTType::debugSelf()
{
	std::cout << "@";
	ASTNamed::debugSelf();
}

void ASTType::exportSymToStream(std::ostream& output)
{
	output << "ERROR: Not implemented yet (^.^)" << std::endl;
}

