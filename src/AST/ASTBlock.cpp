#include "../ClassCompile.hpp"
#include "ASTClass.hpp"

void ASTBlock::insertChild(ASTBase *node)
{
	child_nodes.push_back(node);
	node->parent_node = this;
}

bool ASTBlock::compileToBackend(ClassCompile* compile_dest)
{
	compile_dest->output_stream << "{" << std::endl;
	for (auto child : child_nodes) {
		if (!child->compileToBackend(compile_dest))
			return false;
	}
	compile_dest->output_stream << "}" << std::endl;
	return true;
}

bool ASTBlock::compileToBackendHeader(ClassCompile* compile_dest)
{
	compile_dest->output_header_stream << "{" << std::endl;
	for (auto child : child_nodes) {
		if (!child->compileToBackendHeader(compile_dest))
			return false;
	}
	compile_dest->output_header_stream << "}" << std::endl;
	return true;
}

void ASTBlock::debugSelf()
{
	for (auto child : child_nodes) {
		for (int i = -1; i < child->indentation_level; i++)
			std::cout << "    ";
		child->debugSelf();
		std::cout << std::endl;
	}
}

