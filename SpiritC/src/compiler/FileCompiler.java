package compiler;

import compiler.ast.ASTBase;
import compiler.ast.ASTClass;
import compiler.backends.CompilerCPP;
import compiler.backends.CompilerSYM;
import compiler.lib.IndentPrinter;
import compiler.lib.PathFind;

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
	/**
	 * Creates the corresponding symbol file of fileName
	 * @param fileName
	 * @return The file path of the corresponding symbol file.
	 */
	private static String getSymFileName(String fileName)
	{
		return "out/" + fileName + ".sym";
	}

	private static String getClassName(String fileName)
	{
		return new File(fileName).getName().split("\\.")[0];
	}

	private static void useCompiler(LangCompiler compiler, String fileName, ASTClass source)
	{
		compiler.createFileStreams(fileName);
		compiler.compileClass(source);
		compiler.closeStreams();
	}

	/**
	 * Imports/compiles/loads the file into parent.
	 *
	 * Returns null on failure.
	 * @param fileName The file to import/compile/load
	 * @return The loaded class. Null on failure.
	 */
	public static ASTClass importFile(String fileName, ASTClass parent)
	{
		try
		{
			String realFileName = PathFind.findInPath(Main.getPath(), fileName);

			if (realFileName == null)
			{
				System.err.println("ERROR Could not find class: " + fileName + " in path: \"" + Main.getPath() + "\"");
				return null;
			}

			// Try to see if already imported... //
			if (shouldCompile(realFileName) && parent != null)
			{
				ASTBase alreadyImported = parent.findSymbol(getClassName(fileName));
				if (alreadyImported != null && alreadyImported instanceof ASTClass)
				{
					return (ASTClass) alreadyImported;
				}
			}

			ASTClass loadedClass = loadClassAST(realFileName, parent);

			if (loadedClass == null)
			{
				System.err.println("ERROR: Could no load AST of " + fileName);
				return null;
			}

			// Make sure the class is complete with all required features. //
			new Polisher(loadedClass).polishClass();

			// Make sure that nothing illegal happens in the AST. //
			new IntegrityChecker(loadedClass).checkIntegrity();

			if (shouldCompile(realFileName))
			{
				// While compiling we create two outputs:                       //
				// * The backend output file. e.g .cpp                          //
				// * The symbol file used to load the class without re-parsing. //

				LangCompiler compiler = chooseCompiler();
				useCompiler(compiler, fileName, loadedClass);

				LangCompiler symbolCompiler = new CompilerSYM();
				useCompiler(symbolCompiler, fileName, loadedClass);
			}

			return loadedClass;
		}
		catch (FileNotFoundException e)
		{
			System.err.println("ERROR: File not found: " + e.getMessage());
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
	 * The cache is stored as a symbol file (.spirit.sym).
	 * @param fileName The file path to the class to load.
	 * @param root Where to put the class once it's loaded.
	 * @return The loaded class.
	 * @throws FileNotFoundException If the file is not found.
	 */
	private static ASTClass loadClassAST(String fileName, ASTClass root) throws FileNotFoundException
	{
		if (!new File(fileName).exists())
		{
			System.err.println("ERROR: File not found: " + fileName);
			return null;
		}

		ASTClass dest = new ASTClass(getClassName(fileName), root);

		// Checks if file has been changed since last compile. //
		if (shouldCompile(fileName))
		{
			// Start a parse that reads from a lexer that reads from the class source. //
			Lexer lexer = new Lexer(new PushbackInputStream(new FileInputStream(fileName), 3), fileName);
			Parser parser = new Parser(lexer);

			// Parse the represented AST from the file into the the dest node. //
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
	 * @param fileName The name of the file that we're checking.
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