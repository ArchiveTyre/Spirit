#pragma once
#include "ASTBase.hpp"

class ASTBlock : public ASTBase {
	public:
		/*** METHODS ***/
		void insertChild(ASTBase *node);

		/*** MEMBER VARIABLES ***/
		std::stack<ASTBase *> child_nodes;
};
