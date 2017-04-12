package compiler.lib;

import compiler.Lexer;
import compiler.Parser;
import compiler.ast.ASTClass;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by david on 4/12/17.
 */
public class FileCompiler
{

	public static void compileFile(String fileName)
	{
		String content = FileHandler.getFileContents(fileName);

		String[] splitFileName = fileName.split("\\.");
		String className = splitFileName[0];


		InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));



		PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);

		Lexer lexer = new Lexer(pushbackInputStream, fileName);

		Parser parser = new Parser(lexer);

		parser.parseFile(new ASTClass(className, null));
	}
}
