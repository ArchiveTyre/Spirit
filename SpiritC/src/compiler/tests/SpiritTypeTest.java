package compiler.tests;

import compiler.SpiritType;
import compiler.builtins.Builtins;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrerexus
 * @date 7/16/17
 */
public class SpiritTypeTest
{
	@Test
	void testIsAssignableFrom()
	{
		Assertions.assertEquals(SpiritType.isAssignableFrom(Builtins.getBuiltin("rational_number"),
		                                                    Builtins.getBuiltin("int")), true);

		Assertions.assertEquals(SpiritType.isAssignableFrom(Builtins.getBuiltin("int"),
		                                                    Builtins.getBuiltin("rational_number")), false);

		Assertions.assertEquals(SpiritType.isAssignableFrom(Builtins.getBuiltin("int"),
				Builtins.getBuiltin("int")), true);
	}
}
