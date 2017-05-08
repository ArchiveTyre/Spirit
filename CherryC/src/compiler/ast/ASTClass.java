package compiler.ast;

import compiler.CherryType;
import compiler.FileCompiler;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates a basic class.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTClass extends ASTParent implements CherryType
{

	public class ImportDeclaration
	{
		public String importPackage;
		public String[] importSymbols;

		public ImportDeclaration(String importPackage, String[] importSymbols)
		{
			this.importPackage = importPackage;
			this.importSymbols = importSymbols;
		}
	}

	public ArrayList<ImportDeclaration> classImports = new ArrayList<>();

	public String extendsClass = null;

	public void importClass(String importPackage, String[] importSymbols)
	{
		classImports.add(new ImportDeclaration(importPackage, importSymbols));
		FileCompiler.importFile(importPackage + ".raven", (ASTClass)getParent());
	}

	public ASTClass(String name, ASTParent parent)
	{
		super(parent, name);
	}

	@Override
	public CherryType getExpressionType()
	{
		return this;
	}

	@Override
	public void debugSelf(IndentPrinter to)
	{
		for (ImportDeclaration declaration : classImports)
		{
			to.println("from " + declaration.importPackage + " import " + Arrays.toString(declaration.importSymbols));
		}
		to.println(name);
		to.println("{");
		to.indentation++;
		for (ASTNode child : childAsts)
		{
			child.debugSelf(to);
			to.println("");
		}
		to.indentation--;
		to.print("}");
	}

	/**
	 * This method is an important part of the parser. Perhaps it should be moved there.
	 * However, this method finds the correct parent for a new AST node inside of this class.
	 * @param line_indent The scanned indent for the new AST.
	 * @return The found parent.
	 */
	public ASTParent getParentForNewCode(int line_indent)
	{
		if (childAsts.size() == 0)
			return this;

		// Get last child node. //
		ASTNode newly_inserted_code = childAsts.get(childAsts.size() - 1);

		ASTNode parent;

		// Find parent. //
		if (line_indent > newly_inserted_code.columnNumber)
		{
			parent = newly_inserted_code;
		}
		else if (line_indent == newly_inserted_code.columnNumber)
		{
			parent = newly_inserted_code.getParent();
		}
		else
		{
			parent = newly_inserted_code.getParent();
			while(parent.columnNumber >= line_indent && parent.getParent() != null)
				parent = parent.getParent();
		}

		// Validate parent. //
		if (parent instanceof ASTVariableDeclaration)
		{
			ASTVariableDeclaration parentAsVar = (ASTVariableDeclaration)parent;
			if (parentAsVar.getValue() instanceof ASTFunctionDeclaration)
				return (ASTFunctionDeclaration)parentAsVar.getValue();
		}
		else if (parent instanceof ASTParent)
		{
			return (ASTParent) parent;
		}

		// Validation failed. There is no parent. //
		return null;
	}


	@Override
	public String getTypeName()
	{
		return name;
	}

	@Override
	public ArrayList<ASTNode> getChildNodes()
	{
		return childAsts;
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileClass(this);
	}
}
