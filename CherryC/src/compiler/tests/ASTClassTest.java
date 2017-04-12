package compiler.tests;

import compiler.ast.ASTClass;
import compiler.ast.ASTParent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by alex on 4/12/17.
 */
class ASTClassTest
{
	@Test
	void getParentForNewCode()
	{
		ASTClass class1 = new ASTClass("Test", null);

		ASTParent p1 = class1.getParentForNewCode(1);
		ASTClass class2 = new ASTClass("Sub-class", p1);
		class2.columnNumber = 1;

		ASTParent p2 = class1.getParentForNewCode(2);
		ASTClass class3 = new ASTClass("Other sub-class", p2);
		class3.columnNumber = 2;

		Assertions.assertEquals(class3, ((ASTParent)class1.child_asts.get(0)).child_asts.get(0));

	}

}