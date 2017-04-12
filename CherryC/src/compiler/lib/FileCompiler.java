package compiler.lib;

import compiler.Lexer;
import compiler.Parser;
import compiler.ast.ASTClass;

import java.io.ByteArrayInputStream;
import java.io.File;
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
		if (shouldCompile(fileName))
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

	public static void createClassFiles(String fileName)
	{
		// TODO: Implement this using FileHandler.createFile();
	}

	/**
	 * Checks if we need to compile a file by comparing the output file to the source files last modified date.
	 * @param fileName The name of the file
	 * @return If we should compile the file or not.
	 */
	public static boolean shouldCompile(String fileName)
	{
		// Create a reference to the two files, source and cpp output. //
		File sourceFile = new File(fileName);
		File cppFile = new File("out/" + fileName + ".cpp");

		// If the source file does not exist then we have a problem. //
		if (!sourceFile.exists())
		{
			return false;
		}


		// If the cpp file does not exist then we need to compile. //
		if (!cppFile.exists())
		{
			return true;
		}

		// If we changed the source file since the last compile, then we need to recompile. //
		if (sourceFile.lastModified() > cppFile.lastModified())
		{
			return true;
		}

		// Otherwise we do not need to compile this. //
		return false;
	}
}
