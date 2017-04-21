package compiler.tests;

import compiler.Lexer;
import compiler.Parser;
import compiler.ast.ASTClass;
import compiler.builtins.Builtins;
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

	private void testClassCompile(boolean incompleteClass, String name, String testString)
	{
		InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);

		Lexer lexer = new Lexer(pushbackInputStream, name + ".cherry");

		Parser parser = new Parser(lexer);
		parser.fileTypeDeclared = incompleteClass;

		ASTClass astClass = new ASTClass(name, null);

		parser.parseFile(astClass);

		DebugPrinter printer = new DebugPrinter(System.out);

		astClass.debugSelf(printer);
		printer.println();
	}

	private void testClassCompile(String name, String testString)
	{
		testClassCompile(true, name, testString);
	}


	@Test
	void firstTest()
	{

		System.out.println(Builtins.getBuiltin("void").getName());

		// Test basic assignment. //
		testClassCompile("AssignTest1", "a := 5\nb : int = a");
		testClassCompile("AssignTest2", "b := 128");

		// Test assignments with an expression with operators. //
		testClassCompile("ExpressionTest1", "b := 3 + 4");
		testClassCompile("ExpressionTest2", "b := 3 + 4 * 2");

		// Test multi-line assignments. //
		testClassCompile("MultiLine1", "a := 3\nb : int = 1");
		testClassCompile("MultiLine2", "a := 8\n\nvar b = 9\n\n");

		// Just expressions. //
		testClassCompile("ExpressionTest3", "a := 3\na=4");

		testClassCompile("ParTest1", "a := (1 * 2)");

		// Tabbing. //
		testClassCompile("Tabing1", "\ta:=3");
		testClassCompile("Tabing2", "\ta:=3\n\ta = 23");

		// Test multi-line parent search. //
		testClassCompile("MultiLineParentSearch1", "\ta:= 10\n\ta = 25");

		/*// Test functions. //
		testClassCompile("FunTest1", "fun Add(a : int, b : int) -> int:");
		testClassCompile("FunTest2", "fun Add(a : int, b : int) -> int:\n\ta+b");
		testClassCompile("FunTest3", "fun Add(a : int, b : int) -> int:\n\tfun DoAdd(a : int, b : int)->int:");*/
		/*testClassCompile("FnTest1", "fn add(a : int, b : int) : int = a + b");
		testClassCompile("FnTest2", "fn foo\n\ta := 2");
		testClassCompile("FnTest3", "fn foo(a : int, b : int)\n\tc : int = a + b");

		// Test function calls. //
		testClassCompile("FunCall1", "fn Add(a : int, b : int ) : int = \nAdd(3, 2)");
		testClassCompile("FunCall2", "fn Neg(b : int) : int =\n0 - 2");*/

		testClassCompile("FunTest1", "a : (x : int, y : int) int = x + y");
		testClassCompile("FunTest2", "a : ()\n\tprint 23");

		// If statements. //
		testClassCompile("IfStatement1", "A := 3\nif A:\n\tprint A\n\tprint A + 5\nb := 10");
		testClassCompile("IfStatement2", "A := 3\nif A:\n\tprint A\nelse:\n\tprint 32");

		// Loops. //

		testClassCompile("Loops1", "loop A := 3, A < 10, i = i + 1:");
		testClassCompile("Loops2", "loop 10:\n\tprint 2");
		testClassCompile("Loops3", "loop 6 + 2:\n\tprint 2");
		//testClassCompile("Loops4", "loop 10 as i:\n\tprint 2");


		// File type declarations. //
		testClassCompile(false, "FileType1", "type enum");
		testClassCompile(false, "FileType2", "type object");

		testClassCompile(false, "Subclass1", "type enum\nextends MyObject");
	}
}
