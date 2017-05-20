package compiler.tests;

import compiler.Lexer;
import compiler.Parser;
import compiler.ast.ASTClass;
import compiler.lib.IndentPrinter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * FIXME: Right now test cases only check that we don't crash the compiler while performing tests. We need to check the output too.
 * @author david, Tyrerexus
 * @date 4/12/17.
 */
class ParserTest
{
	private void testClassCompile(boolean incompleteClass, String name, String testString, String importedString)
	{
		System.out.flush();
		System.err.flush();
		System.out.println("=== " + name + " ===");

		IndentPrinter printer = new IndentPrinter(System.out);

		InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream, 3);
		Lexer lexer = new Lexer(pushbackInputStream, name + ".raven");
		ASTClass astClass = new ASTClass(name, null);

		Parser parser = new Parser(lexer);
		parser.fileTypeDeclared = incompleteClass;
		parser.ignoreImport = true;

		// Parse imported-class. //
		{
			InputStream inputStream2 = new ByteArrayInputStream(importedString.getBytes(StandardCharsets.UTF_8));
			PushbackInputStream pushbackInputStream2 = new PushbackInputStream(inputStream2);
			Lexer lexer2 = new Lexer(pushbackInputStream2, name + "Sub.raven");
			ASTClass importedClass = new ASTClass("Other", astClass);

			Parser parser2 = new Parser(lexer2);
			parser2.fileTypeDeclared = true;
			parser2.ignoreImport = true;
			parser2.parseFile(importedClass);
		}

		// Parse the rest. //
		parser.parseFile(astClass);
		astClass.debugSelf(printer);
		printer.println();

		System.out.println();
		System.out.flush();
		System.err.flush();
	}

	private void testClassCompile(String name, String testString)
	{
		testClassCompile(true, name, testString, "");
	}

	@Test
	void singleTest()
	{
		testClassCompile("S", "begin : ()\n\tsomething\nsomething : ()");
	}

	@Test
	void firstTest()
	{
		String tmpPrint = "print : (something : int)\n";
		String tmpStrPrint = "print : (what : string)\n";
		String tmpVoid = "void : ()\n";
		String tmpInt = "tmp : () int = 5\n";
		String tmpParam = "tmp2 : (x) int = x * 2";

		// Empty. //
		testClassCompile("Empty1", "");

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

		// Parenthesis. //
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
		testClassCompile("FunTest5", "func : (x, y, z) int = x * y + z");

		// Test function calls. //
		testClassCompile("FunCall1", "a : (x : int, y : int) int = x + y\na 1 2");
		testClassCompile("FunCall2", "a : (x : int, y : int) int = x + y\na 1 2 (a 3 4)");
		testClassCompile("FunCall3", "a : ()");
		testClassCompile("FunCall4", "a : (x, y : int) int = x + y\na 1 2 (a 3 4)");
		testClassCompile("FunCall5", tmpInt + "a : int = (tmp)");
		testClassCompile("FunCall6", tmpPrint + tmpParam + "print (tmp2 5)");
		testClassCompile("FunCall7", "d : ( a : int, b : int) = a + b\nm : (a : int, b : string, c : int) int = 42\nm (d 1 2) \"Hi\" 12");
		testClassCompile("FunCall8", "begin : ()\n\tsomething\nsomething : ()");

		// If statements. //
		testClassCompile("IfStatement1", tmpPrint + "A := 3\nif A\n\tprint A\n\tprint A + 5\nb := 10");
		testClassCompile("IfStatement2", tmpPrint + "A := 3\nif A\n\tprint A\nelse\n\tprint 32");

		// Loops. //
		testClassCompile("Loops1", "loop A := 3, A < 10, i = i + 1");
		testClassCompile("Loops2", tmpPrint + "loop 10\n\tprint 2");
		testClassCompile("Loops3", tmpPrint + "loop 6 + 2\n\tprint 2");
		//testClassCompile("Loops4", "loop 10 as i:\n\tprint! 2");


		// File type declarations. //
		testClassCompile(false, "FileType1", "type enum", "");
		testClassCompile(false, "FileType2", "type object", "");

		testClassCompile(false, "Subclass1", "type enum\nextends MyObject", "");

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
		testClassCompile("Import3", "from b import a, b, c");
		testClassCompile("Import4", "from hello.world.now import alpha, beta");
		testClassCompile("Import5", "import a.b.c");

		// Member access. //

		testClassCompile("MemberAccess1", "a : int = 0\na.toString");
		testClassCompile(true, "MemberAccess2", "b : Other = Other", "fun : ()");
		testClassCompile(true, "MemberAccess3", "b : Other = Other\nb.fun", "fun : ()");

		testClassCompile(true, "MemberAccess4", "b : Other = Other\nb.fun 12", "fun : (x : int)");

		// String test. //
		testClassCompile("Strings1", "a := \"Hello\"");
		testClassCompile("Strings2", tmpStrPrint + "print \"Hii\"");



		// Comments test. //
		testClassCompile("Comments1", "% This is a test\na := 10");
		testClassCompile("Comments2", "a := 10 % This is a test");
		testClassCompile("Comments3", "b := 10 <% This\nIs\nA\nTest %>\na := 10");
		testClassCompile("Comments3", "b := 10 <% This<%\n%>Is\nA\nTest %>\na := 10");
		testClassCompile("Comments4", "name := \"Godzilla\"\nna<%te\nst%>me = \"Nano\"");

		// Inline. //
		testClassCompile("Inline1", "a := 5\n#inline\na = 15;\ncout << a;\n#end\na = 10");

		testClassCompile("Test", "a := 5\nif a < 10\n\ta = 100");
		testClassCompile("Test2", tmpStrPrint + "print\n\tif 10 == 10\n\t\ta := 20");

	}
}