package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.builtins.FileType;
import compiler.lib.IndentPrinter;

/**
 * @author david
 * @date 4/19/17.
 */

// FIXME: Just move it to ASTClass...
public class ASTFileTypeDeclaration extends ASTBase
{

	public FileType type;
	public ASTFileTypeDeclaration(ASTParent parent, FileType type)
	{
		super(parent);
		this.type = type;
	}

	@Override
	public SpiritType getExpressionType()
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
