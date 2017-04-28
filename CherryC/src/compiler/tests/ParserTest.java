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
 *
 * FIXME: Right now test cases only check that we don't crash the compiler while performing tests. We need to check the output too.
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

		String tmpPrint = "print : (something : int)\n";
		String tmpVoid = "void : ()\n";
		String tmpInt = "tmp : () int = 5\n";
		String tmpParam = "tmp2 : (x) int = x * 2";


		// Test basic assignment. //
		testClassCompile("AssignTest1", "a := 5\nb : int = a");
		testClassCompile("AssignTest2", "b := 128");

		// Test assignments with an expression with operators. //
		testClassCompile("ExpressionTest1", "b := 3 + 4");
		testClassCompile("ExpressionTest2", "b := 3 + 4 * 2");

		// Test multi-line assignments. //
		testClassCompile("MultiLine1", "a := 3\nb : int = 1");
		testClassCompile("MultiLine2", "a := 8\n\nb := 9\n\n");

		// Just expressions. //
		testClassCompile("ExpressionTest3", "a := 3\na=4");

		testClassCompile("ParTest1", "a := (1 * 2)");

		// Tabbing. //
		testClassCompile("Tabing1", "\ta:=3");
		testClassCompile("Tabing2", "\ta:=3\n\ta = 23");

		// Test multi-line parent search. //
		testClassCompile("MultiLineParentSearch1", "\ta:= 10\n\ta = 25");

		// Test functions. //
		testClassCompile("FunTest1", "a : (x : int, y : int) int = x + y");
		testClassCompile("FunTest2", tmpPrint + "a : ()\n\tprint 23");

		testClassCompile("FunTest3", tmpPrint + "a : ()\n\tb : ()\n\t\tprint 2\n\tb");

		testClassCompile("FunTest4", "func : (x, y : int, b : bool, c, k : string)\n\ta := 5");
		testClassCompile("FunTest4", "func : (x, y, z) int = x * y + z");

		// Test function calls. //
		testClassCompile("FunCall1", "a : (x : int, y : int) int = x + y\na 1 2");
		testClassCompile("FunCall2", "a : (x : int, y : int) int = x + y\na 1 2 (a 3 4)");
		testClassCompile("FunCall3", "a : ()");
		testClassCompile("FunCall4", "a : (x, y : int) int = x + y\na 1 2 (a 3 4)");
		testClassCompile("FunCall5", tmpInt + "a : int = (tmp)");
		testClassCompile("FunCall6", tmpPrint + tmpParam + "print (tmp2 5)");

		// If statements. //
		testClassCompile("IfStatement1", tmpPrint + "A := 3\nif A\n\tprint A\n\tprint A + 5\nb := 10");
		testClassCompile("IfStatement2", tmpPrint + "A := 3\nif A\n\tprint A\nelse\n\tprint 32");

		// Loops. //

		testClassCompile("Loops1", "loop A := 3, A < 10, i = i + 1");
		testClassCompile("Loops2", tmpPrint + "loop 10\n\tprint 2");
		testClassCompile("Loops3", tmpPrint + "loop 6 + 2\n\tprint 2");
		//testClassCompile("Loops4", "loop 10 as i:\n\tprint! 2");


		// File type declarations. //
		testClassCompile(false, "FileType1", "type enum");
		testClassCompile(false, "FileType2", "type object");

		testClassCompile(false, "Subclass1", "type enum\nextends MyObject");

		// Crazy. //

		testClassCompile("CrazyIf1", tmpPrint + "if (\n5 \n==\n 5\n)\n\tprint 5");
		testClassCompile("CrazyElse1", tmpPrint + "if (\n5 \n==\n 5\n)\n\tprint 5\nelse\n\tprint 10");
		testClassCompile("CrazyCall1", tmpPrint +"a : (b, c) int = 3\na (\na 2\n(3)) 0");
		testClassCompile("CrazyCall2", tmpPrint +"a : (b, c) int = 3\na (a ((a 5) + (a 7)))");

		// Precedence. //
		testClassCompile("Precedence1", "2 + 2 * 2 + 2");
		testClassCompile("Precedence2", "2 + 2 == 2 + 2");
		testClassCompile("Precedence3", "2 > 2 == 2 < 2");
		testClassCompile("Precedence2", "2 + 2 + 4 * 4 + 2 + 2");
		testClassCompile("Precedence2", "2 * 2 + 2 * 2");


		// Imports. //
		testClassCompile("Import1", "import a");
		testClassCompile("Import2", "from b import a");
		testClassCompile("Import2", "from b import a, b, c");

	}
}
