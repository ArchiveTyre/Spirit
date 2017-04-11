#include "ASTDefineVariable.hpp"
#include "ASTType.hpp"

ASTDefineVariable::ASTDefineVariable(ASTBase *parent, std::string name, ASTBase* new_initial_value)
: ASTBase(parent)
, ASTNamed(parent, name)
, initial_value(new_initial_value)
{
	std::cout << "Defining new variable: " << name << std::endl;
}

void ASTDefineVariable::confirmParent()
{
	ASTBase::confirmParent();
	ast_type = initial_value->getExpressionType();
}

bool ASTDefineVariable::compileToBackend(ClassCompile* compile_dest)
{
#	if 0
		bool s1 = ast_type->compileToBackend(compile_dest);
		
		/* If class, add the colons like: ClassName::variable_name. */
		if (auto parent = dynamic_cast<ASTClass*>(parent_node)) {
			compile_dest->output_stream << parent->ast_name << "::";
		}
		bool s2 = ASTNamed::compileToBackend(compile_dest);
		compile_dest->output_stream << " = ";
		bool s3 = initial_value->compileToBackend(compile_dest);
		return s1 && s2 && s3;*/
#	endif
	return true;
}

bool ASTDefineVariable::compileToBackendHeader(ClassCompile* compile_dest)
{
	bool s1 = ast_type->compileToBackendHeader(compile_dest);
	bool s2 = ASTNamed::compileToBackendHeader(compile_dest);
	compile_dest->output_header_stream << " = ";
	bool s3 = initial_value->compileToBackendHeader(compile_dest);
	return s1 && s2 && s3;
}


void ASTDefineVariable::debugSelf()
{
	std::cout << "Variable -> type: ";
	ast_type->debugSelf();
	std::cout << " name: ";
	ASTNamed::debugSelf();
	std::cout << " value: ";
	initial_value->debugSelf();
	std::cout << std::endl;
}

void ASTDefineVariable::exportSymToStream(std::ostream& output)
{
	
}
