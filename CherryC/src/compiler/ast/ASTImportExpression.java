package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * Created by david on 4/28/17.
 */
public class ASTImportExpression extends ASTBase
{

	private String[] importPackage;
	private String[] importFiles;

	public ASTImportExpression(ASTParent parent, String[] importPackage, String[] importFiles)
	{
		super(parent);
		this.importPackage = importPackage;
		this.importFiles = importFiles;
	}

	public ASTImportExpression(ASTParent parent, String importPackage, String[] importFiles)
	{
		super(parent);
		this.importPackage = new String[] {importPackage};
		this.importFiles = importFiles;
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.print("from " + this.importPackage[0] + " import ");
		for (int i = 0; i < importFiles.length; i++)
		{
			String comma = (i == importFiles.length - 1) ? "" : ", ";
			destination.print(importFiles[i] + comma);
		}
	}
}
