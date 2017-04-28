package compiler;

import compiler.ast.*;

/**
 * @auhtor Tyrerexus
 * @date 28/04/17.
 */
public abstract class LangCompiler
{
	public abstract void compileClass(ASTClass astClass);
	public abstract void compileIf(ASTIf astIf);
	public abstract void compileLoop(ASTLoop astLoop);
	public abstract void compileFunctionCall(ASTFunctionCall astFunctionCall);
	public abstract void compileVariableUsage(ASTVariableUsage astVariableUsage);
	public abstract void compileVariableDeclaration(ASTVariableDeclaration astVariableDeclaration);
	public abstract void compileOperator(ASTOperator astOperator);
	public abstract void compileFunctionDeclaration(ASTVariableDeclaration declaration);
	public abstract void compileNumber(ASTNumber astNumber);
	public abstract void compileString(ASTString astString);
	public abstract void compileReturnExpression(ASTReturnExpression astReturnExpression);

	public LangCompiler()
	{

	}
}
