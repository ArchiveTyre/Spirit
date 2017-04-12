package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * The base class for any AST node.
 *
 * @author Tyrerexus
 * @date 4/11/17.
 */
public abstract class ASTBase
{

	/** If the AST has a name (optional). */
	String name;

	/** Line number on which it was defined on. */
	public int lineNumber;

	/** Column number on which it was defined on. */
	public int columnNumber;

	/** Parent of this node. */
	ASTParent parent;

	public ASTBase(ASTParent parent)
	{
		if (parent != null)
		{
			this.parent = parent;
			parent.child_asts.add(this);
		}
	}

	public ASTBase(ASTParent parent, String name)
	{
		this(parent);
		this.name = name;
	}

	void setParent(ASTParent newParent)
	{
		if (this.parent != null)
			this.parent.child_asts.remove(this);
		if (newParent != null)
			newParent.child_asts.add(this);
		this.parent = newParent;

	}

	public CherryType getExpressionType()
	{
		return null;
	}

	/**
	 * Finds another AST from this AST's perspective.
	 * @param symbolName
	 * @return
	 */
	public ASTBase findSymbol(String symbolName)
	{
		if (symbolName == name)
			return this;

		return null;
	}

	/**
	 * Prints the AST onto a stream.
	 *
	 * When implementing, make sure that the print does not end with a newline.
	 * @param destination The DebugPrinter on which to print to.
	 */
	abstract public void debugSelf(DebugPrinter destination);

}
