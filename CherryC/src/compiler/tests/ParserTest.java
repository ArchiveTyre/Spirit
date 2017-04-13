package compiler.tests;

import compiler.Lexer;
import compiler.Parser;
import compiler.ast.ASTClass;
import compiler.lib.DebugPrinter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author david, Tyrerexus
 * @date 4/12/17.
 */
class ParserTest
{

	private void testClassCompile(String name, String testString)
	{
		InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);

		Lexer lexer = new Lexer(pushbackInputStream, name + ".cherry");

		Parser parser = new Parser(lexer);

		ASTClass astClass = new ASTClass(name, null);

		parser.parseFile(astClass);

		DebugPrinter printer = new DebugPrinter(System.out);

		astClass.debugSelf(printer);
		printer.println();
	}


	@Test
	void firstTest()
	{
		// Test basic assignment. //
		testClassCompile("AssignTest1", "var a = 5");
		testClassCompile("AssignTest2", "var b = 128");

		// Test assignments with an expression with operators. //
		testClassCompile("ExpressionTest1", "var b = 3 + 4");
		testClassCompile("ExpressionTest2", "var b = 3 + 4 * 2");

		// Test multi-line assignments. //
		testClassCompile("MultiLine1", "var a = 3\nvar b = 1");
		testClassCompile("MultiLine2", "var a = 8\n\nvar b = 9\n\n");

		// Just expressions. //
		testClassCompile("ExpressionTest3", "var a = 3\na=4");

		testClassCompile("ParTest1", "var a = (1 * 2)");


		// Test multi-line parent search. //
		testClassCompile("MultiLineParentSearch", "\tvar a = 10\n\ta = 25");

		// Test functions. //
		testClassCompile("FunTest1", "fun Add(a : int, b : int) -> int:");
		testClassCompile("FunTest2", "fun Add(a : int, b : int) -> int:\n\ta+b");
		testClassCompile("FunTest3", "fun Add(a : int, b : int) -> int:\n\tfun DoAdd(a : int, b : int)->int:");

		// Test function calls. //
		testClassCompile("FunCall1", "fun Add(a : int, b : int )->int:\nAdd(3, 2)");
		testClassCompile("FunCall2", "fun Neg(b : int) -> int:\nNeg 2");
	}
}