package compiler.ast;

import compiler.SpiritType;
import compiler.FileCompiler;
import compiler.LangCompiler;
import compiler.Main;
import compiler.lib.IndentPrinter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates a basic class.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTClass extends ASTParent implements SpiritType
{
	/**
	 * A class representing an import.
	 */
	public class ImportDeclaration
	{
		/** The package name. */
		public String importPackage;

		/** The symbols that we wish to import from the package. */
		public String[] importSymbols;

		private ImportDeclaration(String importPackage, String[] importSymbols)
		{
			this.importPackage = importPackage;
			this.importSymbols = importSymbols;
		}
	}

	/**
	 * A list of packages to import.
	 */
	public ArrayList<ImportDeclaration> classImports = new ArrayList<>();

	/**
	 * The name of the class we are extending.
	 */
	public String extendsClass = null;

	/**
	 * The result of <code>this.findSymbol(extendsClass)</code>.
	 */
	public ASTClass extendsClassAST = null;

	/**
	 * When importing should we compile the thing that we are importing?
	 */
	public boolean ignoreImports = false;

	public ASTBase newlyInsertedCode = null;

	public ASTClass(String name, ASTParent parent)
	{
		super(parent, name);
		if (parent == null)
			this.columnNumber = -2;
		else
			this.columnNumber = -1;
	}

	/**
	 * Makes the class extend another class by name.
	 * @param className The name of the class to extend.
	 */
	public void extendClass(String className)
	{
		extendsClass = className;
		ASTBase search = this.findSymbol(className);
		if (search instanceof ASTClass)
			extendsClassAST = (ASTClass)search;
	}

	/**
	 * Checks whether there is a constructor in the class.
	 * @return True if there is a constructor.
	 */
	public boolean getConstructorDeclared()
	{
		for (ASTBase child : childAsts)
		{
			if (child instanceof ASTVariableDeclaration
					&&((ASTVariableDeclaration) child).isFunctionDeclaration())
			{
				ASTFunctionGroup group = (ASTFunctionGroup) ((ASTVariableDeclaration) child).getValue();
				if (group.isConstructor())
					return true;
			}
		}
		return false;
	}

	/**
	 * Adds a package to the import list.
	 * Then it imports it if ignoreImports is set to false.
	 * @param importPackage The package name.
	 * @param importSymbols A list of stuff to  be imported from it.
	 */
	public void importClass(String importPackage, String[] importSymbols)
	{
		classImports.add(new ImportDeclaration(importPackage, importSymbols));
		if (!ignoreImports)
			FileCompiler.importFile(importPackage + Main.FILE_EXTENSION, (ASTClass)getParent());
	}

	/**
	 * This method is an important part of the parser. Perhaps it should be moved there.
	 * However, this method finds the correct parent for a new AST node inside of this class.
	 * @param line_indent The scanned indent for the new AST.
	 * @return The found parent.
	 */
	public ASTParent getParentForNewCode(int line_indent)
	{
		if (newlyInsertedCode == null)
		{
			return this;
		}

		ASTBase parent;

		// Find parent. //
		if (line_indent > newlyInsertedCode.columnNumber)
		{
			parent = newlyInsertedCode;
		}
		else if (line_indent == newlyInsertedCode.columnNumber)
		{
			parent = newlyInsertedCode.getParent();
		}
		else
		{
			parent = newlyInsertedCode.getParent();
			while(parent.columnNumber >= line_indent && parent.getParent() != null)
				parent = parent.getParent();
		}

		// Validate parent. //
		if (parent instanceof ASTVariableDeclaration)
		{
			ASTVariableDeclaration parentAsVar = (ASTVariableDeclaration)parent;
			if (parentAsVar.getValue() instanceof ASTFunctionGroup)
			{
				ASTFunctionGroup group = (ASTFunctionGroup)parentAsVar.getValue();
				return (ASTParent)group.lastChild();
			}

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
	public ArrayList<ASTBase> getChildNodes()
	{
		return childAsts;
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileClass(this);
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return true;
	}

	@Override
	public SpiritType getExpressionType()
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
		for (ASTBase child : childAsts)
		{
			child.debugSelf(to);
			to.println("");
		}
		to.indentation--;
		to.print("}");
	}
}