#include "ASTBlock.hpp"

void ASTBlock::insertChild(ASTBase *node)
{
	child_nodes.push(node);
	node->parent_node = this;
}
