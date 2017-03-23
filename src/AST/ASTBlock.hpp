#pragma once
#include <list>
#include "ASTBase.hpp"

class ASTBlock : public ASTBase {
	public:
		/*** METHODS ***/
		void insertChild(ASTBase *node);
		virtual bool compileToBackend(ClassCompile *compile_dest);
		virtual bool compileToBackendHeader(ClassCompile *compile_dest);

		/*** MEMBER VARIABLES ***/
		std::list<ASTBase *> child_nodes;
};
