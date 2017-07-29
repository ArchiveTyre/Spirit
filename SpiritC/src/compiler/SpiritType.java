package compiler;

import compiler.ast.ASTBase;
import sun.security.provider.ConfigFile;

import java.util.ArrayList;

/**
 * Defines a simple type that can be used in this language.
 *
 * @author Tyrerexus
 * @date 4/12/17
 */
public interface SpiritType
{
	String getTypeName();
	ArrayList<ASTBase> getChildNodes();
	ASTBase getChildByName(String name);
	SpiritType getSuperType();

	/**
	 * Checks if a variable of type <code>type</code> can be assigned from <code>assignTo</code>
	 * @param type The type of the variable.
	 * @param assignTo What we are checking.
	 * @return True if <code>type</code> is assignable from <code>assignTo</code>.
	 */
	static boolean isAssignableFrom(SpiritType type, SpiritType assignTo)
	{

		for (SpiritType currentCheck = assignTo; currentCheck != null; currentCheck = currentCheck.getSuperType())
		{
			if (type == currentCheck)
				return true;
		}

		return false;
	}
}
