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

ASTClass::ASTClass(std::string class_name) : ASTNamed(class_name), ASTBlock()
{
	ASTBlock::indentation_level = -1;
	ASTNamed::indentation_level = -1;
	newly_inserted_node = static_cast<ASTBlock*>(this);
}

void ASTClass::insertNewCode(ASTBase* new_code)
{
	
	/* Made in parse.l. */
	extern int line_indent;
	
	#ifdef DEBUG
		printf("Inserting at indent: %d\n", line_indent);
	#endif
	
	/* Descend. */
	if (new_code->indentation_level > newly_inserted_node->indentation_level) {
		if (auto new_parent = dynamic_cast<ASTBlock*>(newly_inserted_node)) {
			new_parent->insertChild(new_code);
		}
	}
	
	/* Stay. Previous node and new node share the same parent. */
	else if (newly_inserted_node->indentation_level == new_code->indentation_level) {
		if (auto new_parent = dynamic_cast<ASTBlock*>(newly_inserted_node->parent_node)) {
			new_parent->insertChild(new_code);
		}
		else {
			std::cerr << "COMPILER ERROR: You can not insert child nodes into anything other than a block." << std::endl;
		}
		
	}
	
	/* Ascend. */
	else {
		
		/* Find the correct depth. */
		ASTBase *parent = newly_inserted_node->parent_node;
		
		/* While the parent his deeper indented than the child node, set parent to grandparent. */
		while (parent != nullptr && parent->indentation_level >= new_code->indentation_level)
			parent = parent->parent_node;
		
		/* Now add the node to the found result. */
		if (auto correct_parent = dynamic_cast<ASTBlock*>(parent)) {
			correct_parent->insertChild(new_code);
		}
		else {
			std::cerr << "COMPILER ERROR: Could not find a parent for code." << std::endl;
		}
	}
	
	newly_inserted_node = new_code;
	
	line_indent = 0;
}


void ASTClass::exportSymToStream(std::ostream& output)
{
	ASTNamed::exportSymToStream(output);
	ASTBlock::exportSymToStream(output);
}

bool ASTClass::compileToBackend(ClassCompile *compile_dest)
{
	ASTBlock::compileToBackend(compile_dest);
	return true;
}

bool ASTClass::compileToBackendHeader(ClassCompile *compile_dest)
{
	
	/* Write the name. */
	compile_dest->output_header_stream << "class ";
	ASTNamed::compileToBackendHeader(compile_dest);
	compile_dest->output_header_stream << " ";
	ASTBlock::compileToBackendHeader(compile_dest);
	compile_dest->output_header_stream << ";";
	return true;
}
