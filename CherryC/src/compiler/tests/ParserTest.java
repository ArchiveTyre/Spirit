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
 * Created by david on 4/12/17.
 */
class ParserTest
{

	private void testClassCompile(String testString)
	{
		InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);

		Lexer lexer = new Lexer(pushbackInputStream, "Test.cherry");


		Parser parser = new Parser(lexer);

		ASTClass astClass = new ASTClass("test", null);

		parser.parseFile(astClass);

		DebugPrinter printer = new DebugPrinter(System.out);

		astClass.debugSelf(printer);
		printer.println();
	}


	@Test
	void firstTest()
	{
		testClassCompile("var a = 5");
		testClassCompile("var b = 128");
	}
}