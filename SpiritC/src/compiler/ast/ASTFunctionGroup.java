package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.Syntax;
import compiler.lib.IndentPrinter;

import java.util.Iterator;
import java.util.List;

/**
 * @author Tyrerexus
 * @date 5/9/17.
 */
public class ASTFunctionGroup extends ASTParent
{
	public boolean operatorOverload = false;

	public ASTFunctionGroup(ASTChildList.ListKey key, ASTParent parent, String name)
	{
		super(key, parent, name);

		children.addLists(ASTChildList.ListKey.BODY);
	}




	public boolean isConstructor()
	{
		return name.equals(Syntax.ReservedNames.CONSTRUCTOR);
	}

	/**
	 * Gets the ASTFunctionDeclaration that matches the given arguments.
	 * @param arguments The arguments to check against.
	 * @return The found ASTFunctionDeclaration. Null on failure.
	 */
	public ASTFunctionDeclaration getWithMarchingArguments(List<ASTBase> arguments)
	{
		// Check all declarations in group. //
		for (ASTBase astBase : children.getBody())
		{
			ASTFunctionDeclaration declaration = (ASTFunctionDeclaration) astBase;

			// No point in checking if they have different sizes. //
			if (declaration.children.getArgs().size() == arguments.size())
			{
				boolean match = true;

				// Iterate until the end or argument miss-match. //
				Iterator<ASTBase> iteratorA = declaration.children.getArgs().iterator();
				Iterator<ASTBase> iteratorB = arguments.iterator();
				while(iteratorA.hasNext())
				{
					// Check miss-match by checking if the types differ. //
					if (iteratorA.next().getExpressionType() != iteratorB.next().getExpressionType())
					{
						// An argument miss-match occurred set failure to true and stop.
						match = false;
						break;
					}
				}

				if (match)
				{
					// If arguments match 100% then we return this declaration. //
					return declaration;
				}
			}
		}

		// Failure, no matchable declarations in this group... //
		return null;
	}

	@Override
	public SpiritType getExpressionType()
	{
		// TODO: This is not really right.
		return children.getFirst().getExpressionType();
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.println("FunctionGroup" + (operatorOverload ? " operator overload" : ""));
		destination.println("{");
		destination.indentation++;
		for(ASTBase node : children.getBody())
		{
			node.debugSelf(destination);
			destination.println();
		}
		destination.indentation--;
		destination.println("}");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileFunctionGroup(this);
	}
}
