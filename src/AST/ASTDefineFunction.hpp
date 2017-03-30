#pragma once
#ifndef AST_DEFINE_FUNCTION_HPP
#define AST_DEFINE_FUNCTION_HPP

#include <tuple>
#include <vector>
#include <string>
#include "ASTDefineVariable.hpp"
#include "ASTType.hpp"
#include "ASTSymbol.hpp"

class ASTDefineFunction : public ASTDefineVariable {
public:
	
	ASTType *return_type;
	
	std::vector<std::pair<ASTType, ASTSymbol>> call_args;
	
	void insertArg(std::string type_name, std::string arg_name);
	
};

#endif
