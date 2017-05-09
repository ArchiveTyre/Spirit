package compiler.backends;

import compiler.LangCompiler;
import compiler.Main;
import compiler.ast.*;
import compiler.lib.IndentPrinter;
import compiler.lib.PathFind;

import java.io.*;

/**
 * @author Tyrerexus
 * @date 28/04/17.
 */
public class CompilerCPP extends LangCompiler
{
	private String hppLocation = null;

	private PrintStream cppStream = null;
	private PrintStream hppStream = null;

	private IndentPrinter cppOutput;
	private IndentPrinter hppOutput;

	public CompilerCPP()
	{

	}

	public CompilerCPP(IndentPrinter cppOutput, IndentPrinter hppOutput)
	{
		super();
		this.cppOutput = cppOutput;
		this.hppOutput = hppOutput;
	}

	private boolean isSemicolonless(ASTBase ast)
	{
		return ast instanceof ASTIf || ast instanceof ASTLoop || ast instanceof ASTElse
				|| (ast instanceof ASTVariableDeclaration && ((ASTVariableDeclaration) ast).childAsts.get(0) instanceof ASTFunctionDeclaration);
	}

	@Override
	public void compileClass(ASTClass astClass)
	{
		hppOutput.println("#pragma once");
		cppOutput.println("#include \"" + hppLocation + "\"");

		// Compile the imports. //
		{
			String path = System.getenv(Main.RAVEN_PKG_PATH);
			if (path == null)
				path = System.getProperty("user.home") + "/RavenPackages:./";

			for (ASTClass.ImportDeclaration declaration : astClass.classImports)
			{
				String pkgPath = PathFind.findInPath(path, "out/" +
						declaration.importPackage.replace('.', '/') + ".raven.hpp");
				if (pkgPath == null)
				{
					System.err.println("ERROR: Could not find package: " + declaration.importPackage);
					System.err.println("     : Search path: " + path);
				}
				else
				{
					hppOutput.println("#include \"" + pkgPath + "\"");
				}
			}
		}

		for (ASTBase child : astClass.childAsts)
		{
			child.compileSelf(this);

			if (child != astClass.childAsts.get(astClass.childAsts.size() - 1))
				cppOutput.println(isSemicolonless(child) ? ' ' : ';');
			else
				cppOutput.print(isSemicolonless(child) ? ' ' : ';');
		}

		hppOutput.println("class " + astClass.getName());
		if (astClass.extendsClass != null)
			hppOutput.print(" : public " + astClass.extendsClass);
		hppOutput.println("{");
		hppOutput.println("public:");
		hppOutput.indentation++;
		for (ASTBase child : astClass.childAsts)
		{
			if (child instanceof ASTVariableDeclaration)
			{
				ASTVariableDeclaration varChild = (ASTVariableDeclaration)child;
				if (varChild.getValue() instanceof ASTFunctionDeclaration)
				{
					ASTFunctionDeclaration declaration = (ASTFunctionDeclaration) varChild.getValue();
					hppOutput.print(declaration.returnType.getTypeName() + " " + varChild.getName() + "(");
					String args = "";
					boolean shouldSubstr = false;
					for (ASTVariableDeclaration childArg : declaration.args)
					{
						args += childArg.getExpressionType().getTypeName() + " " + childArg.getName() + ", ";
						shouldSubstr = true;
					}
					if (shouldSubstr)
						args = args.substring(0, args.length() - 2);

					hppOutput.println(args + ");");
				}
				else
				{
					hppOutput.print(varChild.getExpressionType().getTypeName());
					hppOutput.print(" ");
					hppOutput.print(varChild.getName());
					hppOutput.println(";");
				}
			}
		}
		hppOutput.indentation--;
		hppOutput.println("}");
	}

	@Override
	public void compileIf(ASTIf astIf)
	{
		cppOutput.print("if (");
		astIf.getCondition().compileSelf(this);
		cppOutput.println(")");
		cppOutput.println("{");
		cppOutput.indentation++;
		for (ASTBase child : astIf.childAsts)
		{
			child.compileSelf(this);
			cppOutput.println(isSemicolonless(child) ? ' ' : ';');
		}
		cppOutput.indentation--;
		cppOutput.print("}");

		if (astIf.elseStatement != null)
		{
			cppOutput.println();
			cppOutput.println("else");
			cppOutput.println("{");
			cppOutput.indentation++;
			for (ASTBase child : astIf.elseStatement.childAsts)
			{
				child.compileSelf(this);
				cppOutput.println(isSemicolonless(child) ? ' ' : ';');
			}
			cppOutput.indentation--;
			cppOutput.print("}");
		}
	}

	@Override
	public void compileLoop(ASTLoop astLoop)
	{
		if (astLoop.preparationalStatement != null)
		{
			astLoop.preparationalStatement.compileSelf(this);
			cppOutput.println();
		}
		cppOutput.print("for (");
		if (astLoop.initialStatement != null)
			astLoop.initialStatement.compileSelf(this);
		cppOutput.print("; ");
		if (astLoop.conditionalStatement != null)
			astLoop.conditionalStatement.compileSelf(this);
		cppOutput.print("; ");
		if (astLoop.iterationalStatement != null)
			astLoop.iterationalStatement.compileSelf(this);
		cppOutput.println(")");
		cppOutput.println("{");
		cppOutput.indentation++;
		for (ASTBase child : astLoop.childAsts)
		{
			if (child != astLoop.preparationalStatement
					&& child != astLoop.initialStatement
					&& child != astLoop.conditionalStatement
					&& child != astLoop.iterationalStatement)
			{
				child.compileSelf(this);
				cppOutput.println(isSemicolonless(child) ? ' ' : ';');
			}
		}
		cppOutput.indentation--;
		cppOutput.println("}");

	}

	@Override
	public void compileFunctionCall(ASTFunctionCall astFunctionCall)
	{
		cppOutput.print(astFunctionCall.getName() + "(");

		for (int i = 0; i < astFunctionCall.childAsts.size(); i++)
		{
			ASTBase child = astFunctionCall.childAsts.get(i);
			child.compileSelf(this);

			if (i != astFunctionCall.childAsts.size() - 1)
				cppOutput.print(", ");
		}
		cppOutput.print(")");
	}

	@Override
	public void compileFunctionGroup(ASTFunctionGroup astFunctionGroup)
	{
		for (ASTBase node : astFunctionGroup.childAsts)
		{
			node.compileSelf(this);
			cppOutput.println();
			hppOutput.println();
		}
	}

	@Override
	public void compileVariableUsage(ASTVariableUsage astVariableUsage)
	{
		cppOutput.print(astVariableUsage.getName());
	}

	@Override
	public void compileVariableDeclaration(ASTVariableDeclaration astVariableDeclaration)
	{
		cppOutput.print(astVariableDeclaration.getExpressionType().getTypeName());
		cppOutput.print(" ");
		cppOutput.print(astVariableDeclaration.getName());
		if (astVariableDeclaration.getValue() != null)
		{
			cppOutput.print(" = ");
			astVariableDeclaration.getValue().compileSelf(this);
		}
	}

	@Override
	public void compileOperator(ASTOperator astOperator)
	{
		if (astOperator.getLeftExpression() != null)
			astOperator.getLeftExpression().compileSelf(this);
		cppOutput.print(" " + astOperator.getName() + " ");
		if (astOperator.getRightExpression() != null)
			astOperator.getRightExpression().compileSelf(this);
	}

	@Override
	public void compileFunctionDeclaration(ASTVariableDeclaration variableDeclaration)
	{
		ASTFunctionDeclaration declaration = (ASTFunctionDeclaration) variableDeclaration.childAsts.get(0);
		String funNamespace = variableDeclaration.getParent() instanceof ASTClass ? variableDeclaration.getParent().getName() + "::" : "";
		cppOutput.print(declaration.returnType.getTypeName() + " " + funNamespace + variableDeclaration.getName() + "(");
		String args = "";
		boolean shouldSubstr = false;
		for (ASTVariableDeclaration child : declaration.args)
		{
			args += child.getExpressionType().getTypeName() + " " + child.getName() + ", ";
			shouldSubstr = true;
		}
		if (shouldSubstr)
			args = args.substring(0, args.length() - 2);

		cppOutput.println(args + ")");
		cppOutput.println("{");
		cppOutput.indentation++;
		for (ASTBase child : declaration.childAsts)
		{
			child.compileSelf(this);
			cppOutput.println(isSemicolonless(child) ? ' ' : ';');
		}
		cppOutput.indentation--;
		cppOutput.println("}");
	}

	@Override
	public void compileNumber(ASTNumber astNumber)
	{
		cppOutput.print(astNumber.value);
	}

	@Override
	public void compileString(ASTString astString)
	{
		cppOutput.print(astString.value);
	}

	@Override
	public void compileReturnExpression(ASTReturnExpression astReturnExpression)
	{
		cppOutput.print("return ");
		astReturnExpression.childAsts.get(0).compileSelf(this);
	}

	@Override
	public void compileMemberAccess(ASTMemberAccess astMemberAccess)
	{
		astMemberAccess.ofObject.compileSelf(this);
		cppOutput.print("->");
		cppOutput.print(astMemberAccess.getMember().getName());
	}

	@Override
	public void createFileStreams(String fileName)
	{
		try
		{
			// Assure that the output directory exists. //
			if (!Main.outDir.exists())
				Main.outDir.mkdirs();
			String parent = new File(fileName).getParent();
			if (parent != null)
				new File(Main.outDir.getPath() + '/' + parent).mkdirs();

			// Set location. //
			hppLocation = Main.outDir.getPath() + '/' + fileName + ".hpp";

			// Create the output streams. //
			cppStream = new PrintStream(Main.outDir.getPath() + '/' + fileName + ".cpp");
			hppStream = new PrintStream(hppLocation);

			cppOutput = new IndentPrinter(cppStream);
			hppOutput = new IndentPrinter(hppStream);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void closeStreams()
	{
		cppStream.flush();
		hppStream.flush();
		cppStream.close();
		hppStream.close();
	}
}
