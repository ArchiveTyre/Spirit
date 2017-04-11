package compiler.ast;

/**
 * Created by david on 4/11/17.
 */
public class ASTBase
{

	/** If the AST has a name (optional). */
	String name;

	/** Line number on which it was defined on. */
	int lineNumber;

	/** Column number on which it was defined on. */
	int columnNumber;

	/** Parent of this node. */
	ASTParent parent;

	ASTBase(ASTParent parent)
	{
		if (parent != null)
		{
			this.parent = parent;
			parent.child_asts.add(this);
		}
	}

	ASTBase(ASTParent parent, String name)
	{
		this(parent);
		this.name = name;
	}

	ASTBase findSymbol(String symbolName)
	{
		if (symbolName == name)
			return this;

		return null;
	}

}
