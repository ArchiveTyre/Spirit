package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.builtins.FileType;
import compiler.lib.IndentPrinter;

/**
 * @author david
 * @date 4/19/17.
 */

// FIXME: Move to just class...
public class ASTFileTypeDeclaration extends ASTNode
{

	public FileType type;
	public ASTFileTypeDeclaration(ASTParent parent, FileType type)
	{
		super(parent);
		this.type = type;
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.println("File type: " + type.name());
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		// Do nothing ... //
	}
}
