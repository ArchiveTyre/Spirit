package compiler.tests;

import compiler.Lexer;
import compiler.ParserSYM;
import compiler.ast.ASTClass;
import compiler.lib.IndentPrinter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tyrerexus
 * @date 5/4/17.
 */
class ParserSYMTest
{
	void test(String source)
	{
		Lexer lexer = new Lexer(source, "Test");
		ParserSYM parseSYM = new ParserSYM(lexer);
		ASTClass astClass = new ASTClass("Test", null);
		parseSYM.parseFile(astClass);

		astClass.debugSelf(new IndentPrinter(System.out));

	}

	@Test
	void testAll()
	{
		test("ClassName: SymTest\n" +
					"CompilerVersion: 0.0.1 ALPHA\n" +
					"ExtendsClass: null\n" +
					"\n" +
					"Arg: c int\n" +
					"Arg: k void\n" +
					"Fun: a void\n" +
					"Fun: b void\n" +
					"Var: A int");
	}
}