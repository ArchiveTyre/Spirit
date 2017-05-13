package compiler.tests;

import compiler.Lexer;
import compiler.Parser;
import compiler.ast.ASTClass;
import compiler.backends.CompilerSYM;
import compiler.lib.IndentPrinter;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrerexus
 * @date 5/4/17.
 */
class CompilerSYMTest
{
	@SuppressWarnings("SameParameterValue")
	private void test(String fileName, String source)
	{
		Lexer lexer = new Lexer(source, fileName);
		Parser parser = new Parser(lexer);
		ASTClass astClass = new ASTClass(fileName, null);
		parser.parseFile(astClass);
		CompilerSYM compiler = new CompilerSYM();
		compiler.symOutput = new IndentPrinter(System.out);
		compiler.compileClass(astClass);
	}

	@Test
	void testAll()
	{
		test("SymTest", "a : (c : int, k : void) = 32\nb:()=1\nA:=55");
	}

}