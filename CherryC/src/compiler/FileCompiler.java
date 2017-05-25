package compiler;

import compiler.ast.ASTBase;
import compiler.ast.ASTClass;
import compiler.backends.CompilerCPP;
import compiler.backends.CompilerSYM;
import compiler.lib.IndentPrinter;

import java.io.*;
import java.util.ArrayList;

/**
 * This class compiles files written in our language.
 * It loads the file into AST and then creates a LangCompiler to compile it.
 * It also creates .sym files to cache the result.
 *
 * @author david, Tyrereuxs
 * @date 4/12/17.
 */
@SuppressWarnings("UnusedReturnValue")
public class FileCompiler
{
	private static String getSymFileName(String fileName)
	{
		return "out/" + fileName + ".sym";
	}

	/**
	 * Imports/compiles/loads the file into parent.
	 * @param fileName The file to import/compile/load
	 * @return The loaded class.
	 */
	public static ASTClass importFile(String fileName, ASTClass parent)
	{
		IndentPrinter printer = new IndentPrinter(System.out);

		try
		{
			ASTClass loadedClass = loadClassAST(fileName, parent);
			if (loadedClass == null)
			{
				System.err.println("ERROR: Could no load AST of " + fileName);
				return null;
			}

			new Polisher(loadedClass).polishClass();

			//loadedClass.debugSelf(printer);
			//printer.println();

			if (shouldCompile(fileName))
			{
				LangCompiler compiler = chooseCompiler();
				compiler.createFileStreams(fileName);
				compiler.compileClass(loadedClass);
				compiler.closeStreams();

				LangCompiler symbolCompiler = new CompilerSYM();
				symbolCompiler.createFileStreams(fileName);
				symbolCompiler.compileClass(loadedClass);
				symbolCompiler.closeStreams();
			}

			// Remove duplicates. //
			for (ASTBase b : new ArrayList<>(parent.childAsts))
			{
				if (b instanceof ASTClass && b.getName().equals(loadedClass.getName()) && b != loadedClass)
				{
					// Found a duplicate. Remove it! //
					b.setParent(null);
				}
			}

			return loadedClass;
		}
		catch (FileNotFoundException e)
		{
			System.err.println("ERROR: Unknown file");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates the requested compiler based on the chosen backend.
	 * @return The created compiler.
	 */
	private static LangCompiler chooseCompiler()
	{
		return new CompilerCPP();
	}

	/**
	 * Tries to load a class'es AST. If the class is already compiled then load the cached form of it.
	 * The cache is stored as a symbol file (.raven.sym).
	 * @param fileName The file path to the class to load.
	 * @param root Where to put the class once it's loaded.
	 * @return The loaded class.
	 * @throws FileNotFoundException If the file is not found.
	 */
	private static ASTClass loadClassAST(String fileName, ASTClass root) throws FileNotFoundException
	{
		if (!new File(fileName).exists())
		{
			throw new FileNotFoundException(fileName);
		}

		String[] splitFileName = fileName.split("\\.");
		String className = splitFileName[0];
		ASTClass dest = new ASTClass(className, root);

		if (shouldCompile(fileName))
		{
			Lexer lexer = new Lexer(new PushbackInputStream(new FileInputStream(fileName), 3), fileName);
			Parser parser = new Parser(lexer);
			parser.parseFile(dest);
		}
		else
		{
			Lexer lexer = new Lexer(new PushbackInputStream(new FileInputStream(getSymFileName(fileName))), fileName);
			ParserSYM parserSYM = new ParserSYM(lexer);
			if (!parserSYM.parseFile(dest))
				return null;

		}

		return dest;
	}

	/**
	 * Checks if we need to compile a file by comparing the output file to the source files last modified date.
	 * @param fileName The name of the file
	 * @return If we should compile the file or not.
	 */
	private static boolean shouldCompile(String fileName)
	{
		// Create a reference to the two files, source and sym output. //
		File sourceFile = new File(fileName);
		File symFile = new File(getSymFileName(fileName));

		// If the source file does not exist then we have a problem. //
		if (!sourceFile.exists())
		{
			return false;
		}

		// If the sym file does not exist then we need to compile. //
		if (!symFile.exists())
		{
			return true;
		}

		// If we changed the source file since the last compile, then we need to recompile. //
		return (sourceFile.lastModified() >= symFile.lastModified());
	}
}