package compiler;

import compiler.ast.ASTClass;
import compiler.lib.IndentPrinter;

import java.io.File;
import java.util.ArrayList;

/**
 * This class parses arguments and calls the compiler.
 *
 * @author david, Tyrerexus
 * @date 4/11/17.
 */
@SuppressWarnings("WeakerAccess")
public class Main
{
	public static final String VERSION        = "0.0.1 ALPHA";
	public static final String COMPILER_NAME  = "Spirit";
	public static final String COPYRIGHT      = "© 2017 TYREREXUS AND DAVID ALL RIGHTS RESERVED";
	public static final String ENV_PKG_PATH   = "SPIRITENV_PKG_PATH";
	public static final String FILE_EXTENSION = ".spirit";
	public static File outDir = new File("out/");
	public static boolean compilerDebug = false;

	public static String getPath()
	{
		String path = System.getenv(Main.ENV_PKG_PATH);

		// If environment variable isn't specified.
		if (path == null)
		{
			String home = System.getProperty("user.home");
			path = home + "/SpiritPackages:" + home + "/SpiritPackages/StdLib:./";
		}
		return path;
	}

	public static void main(String[] args)
	{
		ArrayList<String> fileNames = new ArrayList<>();

		for (int i = 0; i < args.length; i++)
		{
			String arg = args[i];

			switch (arg)
			{
				case "-o":
				case "--output":
					i++;
					outDir = new File(args[i]);
					break;
				case "-h":
				case "--help":
					printHelp();
					break;
				case "-v":
				case "--version":
					printVersion();
					break;
				case "--compiler-debug":
					compilerDebug = true;
					break;
				default:
					fileNames.add(arg);
			}
		}

		ASTClass root = new ASTClass("root", null);
		FileCompiler.importFile(Syntax.ReservedNames.OBJECT_CLASS + FILE_EXTENSION, root);

		for (String file : fileNames)
		{
			FileCompiler.importFile(file, root);
		}

		if (compilerDebug)
		{
			IndentPrinter printer = new IndentPrinter(System.out);
			root.debugSelf(printer);
		}
	}

	private static void printVersion()
	{
		System.out.println(COMPILER_NAME + " V" + VERSION + "\n" + COPYRIGHT);
	}

	private static void printHelp()
	{
		System.out.println("Options:");
		System.out.println("\t-o / --output:\t\tSpecify the output file");
		System.out.println("\t-h / --help\t\tCall help command");
		System.out.println("\t-v / --version\t\tCurrent version of the compiler.");
	}
}