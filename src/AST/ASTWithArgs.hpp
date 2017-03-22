#pragma once
#include <list>
#include "ASTBase.hpp"

class ASTWithArgs : virtual public ASTBase {
	public:
		/*** METHODS ***/
		void insertArg(ASTBase *arg);
		virtual void exportSymToStream(std::ostream& output) override;
		virtual bool compileToBackend(ClassCompile *compile_dest) override;
		virtual void debugSelf() override;

		/*** MEMBERS VARIABLES ***/
		std::list<ASTBase *> arg_nodes;
};
