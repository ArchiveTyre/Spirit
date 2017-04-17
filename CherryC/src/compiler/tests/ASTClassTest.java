package compiler.tests;

import compiler.ast.ASTClass;
import compiler.ast.ASTNumber;
import compiler.ast.ASTParent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by alex on 4/12/17.
 */
class ASTClassTest
{

	private ASTNumber createChild(ASTClass parent, int indentation_level)
	{
		ASTNumber child = new ASTNumber(parent.getParentForNewCode(indentation_level), 42);
		child.columnNumber = indentation_level;
		return child;
	}

	@Test
	void getParentForNewCode()
	{
		{
			ASTClass class1 = new ASTClass("Test", null);

			ASTParent p1 = class1.getParentForNewCode(1);
			ASTClass class2 = new ASTClass("Sub-class", p1);
			class2.columnNumber = 1;

			ASTParent p2 = class1.getParentForNewCode(2);
			ASTClass class3 = new ASTClass("Other sub-class", p2);
			class3.columnNumber = 2;

			Assertions.assertEquals(class3, ((ASTParent) class1.childAsts.get(0)).childAsts.get(0));
		}

		{
			ASTClass p1 = new ASTClass("Test2", null);
			ASTNumber c1 = createChild(p1, 4);
			ASTNumber c2 = createChild(p1, 4);

			Assertions.assertEquals(c1, p1.childAsts.get(0));
			Assertions.assertEquals(c2, p1.childAsts.get(1));
		}
	}

}