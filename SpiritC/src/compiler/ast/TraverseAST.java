package compiler.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to traverse an AST
 *
 * @author alex
 * @date 30/06/17.
 */
public class TraverseAST
{
	/**
	 * Returns a list of all recursive child of *astParent* with type of *type*.
	 * @param astParent The parent to traverse.
	 * @param type The type we want.
	 * @return A list of ASTs to traverse.
	 */
	public static List<ASTBase> traverse(ASTParent astParent, Class type)
	{
		List<ASTBase> toTraverse = new ArrayList<>();
		traverseNode(astParent, type, toTraverse);
		return toTraverse;
	}

	private static void traverseNode(ASTParent astParent, Class type, List<ASTBase> toTraverse)
	{
		for (ASTBase child : astParent.childAsts)
		{
			if (type.isInstance(child))
			{
				toTraverse.add(child);
			}
			if (child instanceof ASTParent)
			{
				traverseNode((ASTParent)child, type, toTraverse);
			}
		}
	}
}
