#include "ASTBase.hpp"
#include <iostream>

/** For searching up symbols. **/

#include "ASTBlock.hpp"
#include "ASTWithArgs.hpp"
#include "ASTNamed.hpp"

using std::string;
using std::cout;
using std::endl;

ASTType *ASTBase::getExpressionType()
{
	return nullptr;
}

ASTBase * ASTBase::findSymbol(string name)
{
	if (auto named = dynamic_cast<ASTNamed*>(this)) {
		if (named->ast_name.compare(name) == 0) {
			return named;
		}
	}
	return findSymbolScan(name);
}

ASTBase * ASTBase::findSymbolScan(string name)
{
		/*
		 * The findSymbol in ASTBase simply searches it's parents. 
		 */
		
		if (auto parent = dynamic_cast<ASTBlock*>(parent_node)) {
			if (auto result = parent->findSymbol(name)) {
				return result;
			}
		}
		if (auto parent = dynamic_cast<ASTWithArgs*>(parent_node)) {
			if (auto result = parent->findSymbol(name)) {
				return result;
			}
		}
		return nullptr;
}



bool ASTBase::compileToBackendHeader(ClassCompile *compile_dest)
{
	/* Not all AST nodes wish to be compiled into a header. Therefore it's okay for them to skip that. */
	return true;
}

void ASTBase::importSymFromStream(ASTBase *dest, std::istream& input)
{
	string ast_type;

	while (input >> ast_type) {
		cout << "Read from file: " << ast_type << endl;
	}
}

void ASTBase::exportSymToStream(std::ostream& output)
{
	/* Do nothing. */
	return;
}

void ASTBase::debugSelf()
{
	printf("A compiler error");
}

void ASTBase::confirmParent()
{
	parent_node = unconfirmed_parent_node;
	
	if (auto parent = dynamic_cast<ASTBlock*>(parent_node)) {
		parent->child_nodes.push_back(this);
	}
	else if (auto parent = dynamic_cast<ASTWithArgs*>(parent_node)) {
		parent->arg_nodes.push_back(this);
	}
	else if (parent != nullptr) {
		std::cerr << "ERROR: Invalid parent!" << std::endl;
	}
}

ASTBase::ASTBase(ASTBase *new_parent)
: unconfirmed_parent_node(new_parent)
{
	
	this->line_no = -1;
	this->indentation_level = -1;
	parent_node = nullptr;
}

ASTBase::~ASTBase()
{
	
}

