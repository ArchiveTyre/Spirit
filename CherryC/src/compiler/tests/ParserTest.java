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

	private void testClassCompile(String testString)
	{
		InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);

		Lexer lexer = new Lexer(pushbackInputStream, "Test.cherry");

		Parser parser = new Parser(lexer);

		ASTClass astClass = new ASTClass("Test", null);

		parser.parseFile(astClass);

		DebugPrinter printer = new DebugPrinter(System.out);

		astClass.debugSelf(printer);
		printer.println();
	}


	@Test
	void firstTest()
	{

		// Test basic assignment. //
		testClassCompile("var a = 5");
		testClassCompile("var b = 128");

		// Test assignments with an expression with operators. //
		testClassCompile("var b = 3 + 4");
		testClassCompile("var b = 3 + 4 * 2");

		// Test multi-line assignments. //
		testClassCompile("var a = 3\nvar b = 1");
		testClassCompile("var a = 8\n\nvar b = 9\n\n");

		// Just expressions. //
		testClassCompile("var a = 3\na=4");
	}
}