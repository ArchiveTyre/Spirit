#pragma once
#ifndef AST_TYPE_HPP
#define AST_TYPE_HPP

#include <string>
#include "ASTNamed.hpp"


class ASTType : public ASTNamed {
public:
	
	ASTType(ASTBase *parent, std::string type_name);
	
	bool compileToBackend(ClassCompile *compile_dest) override;
	bool compileToBackendHeader(ClassCompile *compile_dest) override;
	void debugSelf() override;
	void exportSymToStream(std::ostream& output) override;
	
};

#endif
