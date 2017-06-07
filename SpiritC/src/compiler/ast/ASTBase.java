package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * The base class for any AST node.
 *
 * @author Tyrerexus
 * @date 4/11/17.
 */
public abstract class ASTBase
{

	/**
	 * If the AST has a name (optional).
	 */
	String name = "";

	/**
	 * Line number on which it was defined on.
	 */
	public int lineNumber;

	/**
	 * Column number on which it was defined on.
	 */
	public int columnNumber;

	/**
	 * Parent of this node.
	 */
	private ASTParent parent;

	/**
	 * Getter for name.
	 * @return The current name of this AST node.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Getter for parent.
	 * @return The current parent of this AST node.
	 */
	public ASTParent getParent()
	{
		return parent;
	}

	/**
	 * Setter for parent.
	 * @param newParent The new parent.
	 */
	public void setParent(ASTParent newParent)
	{
		if (this.parent != null)
			this.parent.childAsts.remove(this);
		if (newParent != null)
			newParent.childAsts.add(this);
		this.parent = newParent;

	}

	/**
	 * Returns the first class it finds by traversing through the parents.
	 * @return The found class that contains this AST node.
	 */
	public ASTClass getContainingClass()
	{
		ASTParent parent = getParent();
		while (parent != null && !(parent instanceof ASTClass))
		{
			parent = parent.getParent();
		}
		return (ASTClass)parent;
	}

	public ASTBase(ASTParent parent)
	{
		if (parent != null)
		{
			this.parent = parent;
			parent.childAsts.add(this);
		}
	}

	public ASTBase(ASTParent parent, String name)
	{
		this(parent);
		this.name = name;
	}

	/**
	 * Prints the AST onto a stream.
	 *
	 * When implementing, make sure that the print does not end with a newline.
	 * @param destination The IndentPrinter on which to print to.
	 */
	public abstract void debugSelf(IndentPrinter destination);

	/**
	 * Calls appropriate function in the compiler for compiling this AST node.
	 * @param compiler The compiler to call.
	 */
	public abstract void compileSelf(LangCompiler compiler);

	/**
	 * Returns the expression type of this AST node.
	 * @return The CherrType that represents this AST node.
	 */
	public abstract SpiritType getExpressionType();

}
