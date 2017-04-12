package compiler;

import compiler.ast.ASTClass;
import compiler.lib.FileHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * This class parses arguments and calls the compiler.
 *
 * @author david, Tyrerexus
 * @date 4/11/17.
 */
public class Main
{
	public static final String VERSION = "0.0.1 ALPHA";
	public static final String COMPILER_NAME = "Cherry";
	public static final String COPYRIGHT = "© 2017 TYREREXUS AND DAVID ALL RIGHTS RESERVED";

	public static void main(String[] args)
	{

		ArrayList<String> fileNames = new ArrayList<>();

		String outputFileName = "";

		for (int i = 0; i < args.length; i++)
		{
			String arg = args[i];

			switch (arg)
			{
				case "-o":
				case "--output":
					i++;
					outputFileName = args[i];
					break;
				case "-h":
				case "--help":
					printHelp();
					break;
				case "-v":
				case "--version":
					printVersion();
					break;
				default:
					fileNames.add(arg);
			}
		}


		for (String file : fileNames)
		{
			String content = FileHandler.getFileContents(file);

			InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));



			PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);

			Lexer lexer = new Lexer(pushbackInputStream, file);

			Parser parser = new Parser(lexer);

			parser.parseFile(new ASTClass(file, null));
		}


	}

	public static void printVersion()
	{
		System.out.println(COMPILER_NAME + " V" + VERSION + "\n" + COPYRIGHT);
	}

	public static void printHelp()
	{
		System.out.println("Options:");
		System.out.println("\t-o / --output:\t\tSpecify the output file");
		System.out.println("\t-h / --help\t\tCall help command");
		System.out.println("\t-v / --version\t\tCurrent version of the compiler.");
	}
}