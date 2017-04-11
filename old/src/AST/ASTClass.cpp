#include "ASTClass.hpp"
#include <string>
#include "../ClassCompile.hpp"

using std::string;

void ASTClass::debugSelf()
{
	std::cout << "CLASS: ";
	ASTNamed::debugSelf();
	std::cout << std::endl;
	ASTBlock::debugSelf();
}

ASTClass::ASTClass(ASTBase *parent, std::string class_name) 
: ASTBase (parent)
, ASTNamed(parent, class_name)
, ASTBlock(parent)
{
	ASTBlock::indentation_level = -1;
	ASTNamed::indentation_level = -1;
}

ASTBase * ASTClass::getParentForNewCode(int line_indent)
{
	
	ASTBase *newly_inserted_node = child_nodes.back();
	if (newly_inserted_node == nullptr)
		newly_inserted_node = static_cast<ASTBlock*>(this);
	
#	ifdef DEBUG
		std::cout << "Inserting at indent:" << line_indent << std::endl;
#	endif
	
	/* Descend. */
	if (line_indent > newly_inserted_node->indentation_level) {
		if (auto new_parent = dynamic_cast<ASTBlock*>(newly_inserted_node)) {
			return new_parent;
		}
		else {
			std::cerr << "COMPILER ERROR: You can not insert child nodes into anything other than a block." << std::endl;
			abort();
			return nullptr;
		}
	}
	
	/* Stay. Previous node and new node share the same parent. */
	else if (line_indent == newly_inserted_node->indentation_level) {
		if (auto new_parent = dynamic_cast<ASTBlock*>(newly_inserted_node->parent_node)) {
			return new_parent;
		}
		else {
			std::cerr << "COMPILER ERROR: You can not insert child nodes into anything other than a block." << std::endl;
			abort();
			return nullptr;
		}
		
	}
	
	/* Ascend. */
	else {
		
		/* Find the correct depth. */
		ASTBase *parent = newly_inserted_node->parent_node;
		
		/* While the parent his deeper indented than the child node, set parent to grandparent. */
		while (parent != nullptr && parent->indentation_level >= line_indent)
			parent = parent->parent_node;
		
		/* Now add the node to the found result. */
		if (auto correct_parent = dynamic_cast<ASTBlock*>(parent)) {
			return correct_parent;
		}
		else {
			std::cerr << "COMPILER ERROR: Could not find a parent for code." << std::endl;
			return nullptr;
		}
	}
}


void ASTClass::exportSymToStream(std::ostream& output)
{
	ASTNamed::exportSymToStream(output);
	ASTBlock::exportSymToStream(output);
}

bool ASTClass::compileToBackend(ClassCompile *compile_dest)
{
	for (auto child : child_nodes) {
		if (!child->compileToBackend(compile_dest))
			return false;
		compile_dest->output_stream << ';' << std::endl;
	}
	return true;
}

bool ASTClass::compileToBackendHeader(ClassCompile *compile_dest)
{
	
	/* Write the name. */
	compile_dest->output_header_stream << "class ";
	ASTNamed::compileToBackendHeader(compile_dest);
	compile_dest->output_header_stream << " ";
	compile_dest->output_header_stream << "{" << std::endl;
	compile_dest->output_header_stream << "public: " << std::endl;
	for (auto child : child_nodes) {
		if (!child->compileToBackendHeader(compile_dest))
			return false;
		compile_dest->output_header_stream << ';' << std::endl;
	}
	compile_dest->output_header_stream << "};" << std::endl;
	return true;
}
