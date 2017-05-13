package compiler;

import compiler.ast.*;

/**
 * The file defines everything a backend has to be able to compile.
 * It also defines functions that are called before and after compilation.
 * Such as: {@link #createFileStreams(String)} and {@link #closeStreams()}
 *
 * @author Tyrerexus
 * @date 28/04/17.
 */
public abstract class LangCompiler
{
	public abstract void compileClass(ASTClass astClass);
	public abstract void compileIf(ASTIf astIf);
	public abstract void compileLoop(ASTLoop astLoop);
	public abstract void compileFunctionCall(ASTFunctionCall astFunctionCall);
	public abstract void compileFunctionGroup(ASTFunctionGroup astFunctionGroup);
	public abstract void compileVariableUsage(ASTVariableUsage astVariableUsage);
	public abstract void compileVariableDeclaration(ASTVariableDeclaration astVariableDeclaration);
	public abstract void compileOperator(ASTOperator astOperator);
	public abstract void compileFunctionDeclaration(ASTFunctionDeclaration astFunctionDeclaration);
	public abstract void compileNumber(ASTNumber astNumber);
	public abstract void compileString(ASTString astString);
	public abstract void compileReturnExpression(ASTReturnExpression astReturnExpression);
	public abstract void compileMemberAccess(ASTMemberAccess astMemberAccess);

	/**
	 * Opens up the appropriate streams for a specified fileName.
	 * @param fileName The filename of the file being compiled.
	 */
	public abstract void createFileStreams(String fileName);

	/**
	 * Flush and close streams.
	 */
	public abstract void closeStreams();

	public LangCompiler()
	{

	}
}
