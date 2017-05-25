package compiler.backends;

import compiler.*;
import compiler.ast.*;
import compiler.lib.IndentPrinter;
import compiler.lib.PathFind;

import java.io.*;

/**
 * Compiles an AST into C++ code.
 *
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
	private ASTMemberAccess astMemberAccess;

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
		return ast instanceof ASTIf
				|| ast instanceof ASTLoop
				|| ast instanceof ASTElse
				|| ast instanceof  ASTInline
				|| (ast instanceof ASTVariableDeclaration && ((ASTVariableDeclaration) ast).childAsts.get(0) instanceof ASTFunctionGroup);
	}

	private String getRawName(CherryType targetType)
	{
		return "___Raw" + targetType.getTypeName();
	}

	@Override
	public void compileClass(ASTClass astClass)
	{

		/// Include guard. ///
		hppOutput.println("#pragma once");
		cppOutput.println("#include \"" + hppLocation + "\"");
		hppOutput.println("#include <string>");
		hppOutput.println("using std::string;");

		/// Compile the imports. ///
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

		/// Set up the class declaration. ///
		hppOutput.println("#define " + astClass.getName() + ' ' + getRawName(astClass) + '*');
		hppOutput.print("class " + getRawName(astClass));

		if (astClass.extendsClassAST != null)
			hppOutput.print(" : public " + getRawName(astClass.extendsClassAST));
		hppOutput.println();
		hppOutput.println("{");
		hppOutput.println("public:");
		hppOutput.indentation++;


		/// Compile members of the class. ///

		for (ASTBase child : astClass.childAsts)
		{
			child.compileSelf(this);

			if (child != astClass.lastChild())
				cppOutput.println(isSemicolonless(child) ? ' ' : ';');
			else
				cppOutput.print(isSemicolonless(child) ? ' ' : ';');
		}

		for (ASTBase child : astClass.childAsts)
		{
			if (child instanceof ASTVariableDeclaration)
			{
				ASTVariableDeclaration varChild = (ASTVariableDeclaration)child;
				if (!(varChild.getValue() instanceof ASTFunctionGroup))
				{
					hppOutput.print(varChild.getExpressionType().getTypeName());
					hppOutput.print(" ");
					hppOutput.print(varChild.getName());
					hppOutput.println(";");
				}
			}
		}
		hppOutput.indentation--;
		hppOutput.println("};");

		// If this is the main class. //
		if (astClass.getName().equals("Main"))
		{
			cppOutput.println();
			cppOutput.println("int main () ");
			cppOutput.println("{");
			cppOutput.indentation++;
			cppOutput.println("Main main = new ___RawMain();");
			cppOutput.println("return 0;");
			cppOutput.indentation--;
			cppOutput.println("}");
		}
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
		if (astFunctionCall.isConstructorCall())
		{
			cppOutput.print("new ");
			((ASTMemberAccess)astFunctionCall.getDeclarationPath()).ofObject.compileSelf(this);
		}
		else
		{
			astFunctionCall.getDeclarationPath().compileSelf(this);
		}
		cppOutput.print("(");

		for (ASTBase child : astFunctionCall.childAsts)
		{
			if (astFunctionCall.compileChild(child))
			{
				child.compileSelf(this);
				if (child != astFunctionCall.lastChild())
					cppOutput.print(", ");
			}
		}


		cppOutput.print(")");
	}

	@Override
	public void compileFunctionGroup(ASTFunctionGroup astFunctionGroup)
	{
		for (ASTBase node : astFunctionGroup.childAsts)
		{
			node.compileSelf(this);
			if (node != astFunctionGroup.lastChild())
			{
				cppOutput.println();
				hppOutput.println();
			}
		}
	}

	@Override
	public void compileVariableUsage(ASTVariableUsage astVariableUsage)
	{
		switch (astVariableUsage.getName())
		{
			case "super":
				// In this language there can only be one super class.          //
				// Thus there is no need for using the real name of that class. //

				cppOutput.print(getRawName(astVariableUsage.getContainingClass().extendsClassAST));
				break;
			case Syntax.ReservedFunctions.CONSTRUCTOR:
				// If we're calling the constructor, then replace that with the name //
				// of the class in question.                                         //

				cppOutput.print(getRawName(astVariableUsage.getContainingClass()));
				break;
			default:

				// Check if we're referring to a type. //
				if (astVariableUsage.getDeclaration() instanceof CherryType)
					// If so, print out the raw name. //
					cppOutput.print(getRawName((CherryType)astVariableUsage.getDeclaration()));
				else
					// Use normal name.
					cppOutput.print(astVariableUsage.getName());
				break;
		}
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

	private String createFunctionDeclaration(ASTFunctionDeclaration astFunctionDeclaration, boolean includeNameSpace)
	{
		ASTVariableDeclaration variableDeclaration = (ASTVariableDeclaration)astFunctionDeclaration.getParent().getParent();

		// Get name and namespace. //
		boolean topLevel = variableDeclaration.getParent() instanceof ASTClass;
		boolean isConstructor = variableDeclaration.getName().equals(Syntax.ReservedFunctions.CONSTRUCTOR);

		String name = topLevel && isConstructor
				? getRawName((ASTClass)variableDeclaration.getParent())
				: variableDeclaration.getName();
		String funNamespace = topLevel && includeNameSpace
				? getRawName((ASTClass)variableDeclaration.getParent()) + "::"
				: "";

		/// Create the declaration. ///
		StringBuilder declaration = new StringBuilder();

		// The return type
		if (!isConstructor)
		{
			declaration.append(astFunctionDeclaration.returnType.getTypeName());
			declaration.append(' ');
		}

		// The name and namespace. //
		declaration.append(funNamespace);
		declaration.append(name);

		// The arguments. //
		declaration.append("(");
		for (ASTVariableDeclaration child : astFunctionDeclaration.args)
		{
			declaration.append(child.getExpressionType().getTypeName());
			declaration.append(' ');
			declaration.append(child.getName());
			if (child != astFunctionDeclaration.args.get(astFunctionDeclaration.args.size() - 1))
				declaration.append(", ");
		}
		declaration.append(")");

		return declaration.toString();
	}

	@Override
	public void compileFunctionDeclaration(ASTFunctionDeclaration astFunctionDeclaration)
	{
		ASTFunctionGroup group = (ASTFunctionGroup) astFunctionDeclaration.getParent();
		String cppDeclaration = createFunctionDeclaration(astFunctionDeclaration, true);
		String hppDeclaration = createFunctionDeclaration(astFunctionDeclaration, false);

		hppOutput.print(hppDeclaration);
		hppOutput.println(";");

		cppOutput.println(cppDeclaration);

		ASTFunctionCall listInitSuperConstructorCall = null;
		if (group.isConstructor())
		{
			listInitSuperConstructorCall = (ASTFunctionCall) astFunctionDeclaration.childAsts.get(0);

			// Check if listInitSuperCall is actually valid... //
			if (listInitSuperConstructorCall.getDeclarationPath() instanceof ASTMemberAccess
					&& listInitSuperConstructorCall.getDeclarationPath().getEnd().equals("new"))
			{
				cppOutput.print(":");
				cppOutput.print(getRawName(astFunctionDeclaration.getContainingClass().extendsClassAST));
				cppOutput.print("(");

				for (ASTBase child : listInitSuperConstructorCall.childAsts)
				{
					if (listInitSuperConstructorCall.compileChild(child))
					{
						child.compileSelf(this);
						if (child != listInitSuperConstructorCall.lastChild())
							cppOutput.print(", ");
					}
				}
				cppOutput.println(")");
			}
			else
			{
				System.err.println("ERROR: Constructor doesn't start with call to super constructor!");
			}
		}

		cppOutput.println("{");
		cppOutput.indentation++;
		for (ASTBase child : astFunctionDeclaration.childAsts)
		{
			if (child != listInitSuperConstructorCall && astFunctionDeclaration.compileChild(child))
			{
				child.compileSelf(this);
				cppOutput.println(isSemicolonless(child) ? ' ' : ';');
			}
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
		cppOutput.print('"' + astString.value + '"');
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

		if (astMemberAccess.ofObject instanceof ASTVariableUsage
		&& ((ASTVariableUsage)astMemberAccess.ofObject).getDeclaration() instanceof CherryType)
			cppOutput.print("::");
		else
			cppOutput.print("->");
		ASTBase member = astMemberAccess.getMember();
		String memberName;
		if (member instanceof CherryType)
			memberName = getRawName((CherryType) member);
		else if (member.getName().equals(Syntax.ReservedFunctions.CONSTRUCTOR))
			memberName = getRawName(astMemberAccess.ofObject.getExpressionType());
		else
			memberName = member.getName();

		cppOutput.print(memberName);
	}

	@Override
	public void compileInline(ASTInline astInline)
	{
		for (String line : astInline.code.split("\n"))
			cppOutput.println(line);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
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
		cppStream.close();

		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		hppStream.flush();
		hppStream.close();
	}
}
