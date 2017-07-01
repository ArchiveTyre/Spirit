package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * A path is either an ASTMemberAccess or ASTVariableUsage.
 *
 * @author Tyrerexus
 * @date 5/13/17
 */
public interface ASTPath
{
	void debugSelf(IndentPrinter destination);
	void setParent(ASTChildList.ListKey key, ASTParent newParent);
	void compileSelf(LangCompiler compiler);
	String getName();
	String getEnd();
	SpiritType getExpressionType();
}
