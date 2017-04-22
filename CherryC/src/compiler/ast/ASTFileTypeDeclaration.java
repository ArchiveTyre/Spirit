package compiler.ast;

import compiler.CherryType;
import compiler.builtins.FileType;
import compiler.lib.DebugPrinter;

/**
 * @author david
 * @date 4/19/17.
 */
public class ASTFileTypeDeclaration extends ASTBase
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
	public void debugSelf(DebugPrinter destination)
	{
		destination.println("File type: " + type.name());
	}
}
