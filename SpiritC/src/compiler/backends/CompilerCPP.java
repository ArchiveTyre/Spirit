package compiler.backends;

import compiler.*;
import compiler.ast.*;
import compiler.ast.ASTChildList.ListKey;
import compiler.lib.IndentPrinter;
import compiler.lib.PathFind;

import java.io.*;
import java.util.HashMap;

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

	private IndentPrinter currentOutput;

	private static final HashMap<String, String> overloadOperatorToOperatorName = new HashMap<String, String>(){{
		// FIXME: Complete the map!

		put("+",  "___add");
		put("-",  "___minus");

		put("*",  "___mul");
		put("/",  "___div");
		put(Syntax.ReservedNames.SELF,"___call");
	}};

	private boolean possibleOverload(ASTBase node)
	{
		return overloadOperatorToOperatorName.containsKey(node.getName());
	}

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
				|| (ast instanceof ASTVariableDeclaration && ((ASTVariableDeclaration) ast).children.getValue().get(0) instanceof ASTFunctionGroup);
	}

	private String getRawName(SpiritType targetType)
	{
		return "___Raw" + targetType.getTypeName();
	}

	@Override
	public void compileClass(ASTClass astClass)
	{

		/// Include guard. ///
		cppOutput.println("#include \"" + hppLocation + "\"");

		currentOutput = cppOutput;

		/// Compile members of the class. ///

		for (ASTBase child : astClass.children.getBody())
		{
			child.compileSelf(this);
			cppOutput.print(isSemicolonless(child) ? ' ' : ';');
		}

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

		/// Compile the imports. ///
		{
			String path = Main.getPath();
			for (ASTClass.ImportDeclaration declaration : astClass.classImports)
			{
				String pkgPath = PathFind.findInPath(path, "out/" +
						declaration.importPackage.replace('.', '/') + Main.FILE_EXTENSION +".hpp");
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

		currentOutput = hppOutput;

		hppOutput.println("#pragma once");
		hppOutput.println("#include <string>");
		hppOutput.println("using string = std::string;");

		/// Set up the class declaration. ///
		hppOutput.println("#define " + astClass.getName() + ' ' + getRawName(astClass) + '*');
		hppOutput.print("class " + getRawName(astClass));

		if (astClass.extendsClassAST != null)
			hppOutput.print(" : public " + getRawName(astClass.extendsClassAST));
		hppOutput.println();
		hppOutput.println("{");
		hppOutput.println("public:");
		hppOutput.indentation++;

		for (ASTBase child : astClass.children.getAll())
		{
			if (child instanceof ASTVariableDeclaration)
			{
				ASTVariableDeclaration varChild = (ASTVariableDeclaration)child;
				if (varChild.getValue() instanceof ASTFunctionGroup)
				{
					child.compileSelf(this);
				}
				else
				{
					hppOutput.print(varChild.getExpressionType().getTypeName());
					hppOutput.print(" ");
					hppOutput.print(varChild.getName());
					if (varChild.getValue() != null)
					{
						hppOutput.print(" = ");
						varChild.getValue().compileSelf(this);
					}
					hppOutput.println(";");
				}
			}
		}
		hppOutput.indentation--;
		hppOutput.println("};");
	}

	@Override
	public void compileIf(ASTIf astIf)
	{
		cppOutput.print("if (");
		astIf.getCondition().compileSelf(this);
		cppOutput.println(")");
		cppOutput.println("{");
		cppOutput.indentation++;
		for (ASTBase child : astIf.children.getBody())
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
			for (ASTBase child : astIf.elseStatement.children.getBody())
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
		for (ASTBase child : astLoop.children.getBody())
		{
			child.compileSelf(this);
			cppOutput.println(isSemicolonless(child) ? ' ' : ';');
		}
		cppOutput.indentation--;
		cppOutput.println("}");

	}

	@Override
	public void compileFunctionCall(ASTFunctionCall astFunctionCall)
	{

		/* Check if call to class constructor. */
		if (astFunctionCall.isConstructorCall())
		{
			/* Add "new " and compile the path without the ".new" part. */

			cppOutput.print("new ");
			((ASTMemberAccess)astFunctionCall.getDeclarationPath()).ofObject.compileSelf(this);
		}

		/* Check if call on class. */
		else if (astFunctionCall.getDeclarationPath().getExpressionType() instanceof ASTClass)
		{
			/* It is, compile as normal function call but add "->___call". */

			astFunctionCall.getDeclarationPath().compileSelf(this);
			cppOutput.print("->___call");
		}
		else
		{
			/* Just a normal function call. */

			astFunctionCall.getDeclarationPath().compileSelf(this);
		}

		cppOutput.print("(");

		for (ASTBase child : astFunctionCall.children.getArgs())
		{
			child.compileSelf(this);
			if (child != astFunctionCall.children.getLast(ASTChildList.ListKey.ARGS))
				cppOutput.print(", ");
		}

		cppOutput.print(")");
	}

	@Override
	public void compileFunctionGroup(ASTFunctionGroup astFunctionGroup)
	{
		for (ASTBase node : astFunctionGroup.children.getBody())
		{
			node.compileSelf(this);
			if (node != astFunctionGroup.children.getBody())
			{
				currentOutput.println();
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

				currentOutput.print(getRawName(astVariableUsage.getContainingClass().extendsClassAST));
				break;
			case Syntax.ReservedNames.CONSTRUCTOR:
				// If we're calling the constructor, then replace that with the name //
				// of the class in question.                                         //

				currentOutput.print(getRawName(astVariableUsage.getContainingClass()));
				break;
			default:

				// Check if we're referring to a type. //
				if (astVariableUsage.getDeclaration() instanceof SpiritType)
					// If so, print out the raw name. //
					currentOutput.print(getRawName((SpiritType)astVariableUsage.getDeclaration()));
				else
					// Use normal name.
					currentOutput.print(astVariableUsage.getName());
				break;
		}
	}

	@Override
	public void compileVariableDeclaration(ASTVariableDeclaration astVariableDeclaration)
	{
		if (!(astVariableDeclaration.getParent() instanceof ASTClass))
		{
			currentOutput.print(astVariableDeclaration.getExpressionType().getTypeName());
			currentOutput.print(" ");
			currentOutput.print(astVariableDeclaration.getName());
			if (astVariableDeclaration.getValue() != null)
			{
				currentOutput.print(" = ");
				astVariableDeclaration.getValue().compileSelf(this);
			}
		}
	}

	@Override
	public void compileOperator(ASTOperator astOperator)
	{

		boolean callsFunction = astOperator.getLeftExpression().getExpressionType() instanceof ASTClass
				&& possibleOverload(astOperator);
		if (callsFunction)
		{
			if (astOperator.getLeftExpression() != null)
				astOperator.getLeftExpression().compileSelf(this);
			currentOutput.print("->");
			currentOutput.print(overloadOperatorToOperatorName.get(astOperator.getName()));
			currentOutput.print("(");
			if (astOperator.getRightExpression() != null)
				astOperator.getRightExpression().compileSelf(this);
			currentOutput.print(")");
		}
		else
		{
			currentOutput.print("(");
			if (astOperator.getLeftExpression() != null)
				astOperator.getLeftExpression().compileSelf(this);
			currentOutput.print(" " + astOperator.getName() + " ");
			if (astOperator.getRightExpression() != null)
				astOperator.getRightExpression().compileSelf(this);
			currentOutput.print(")");
		}
	}

	private String createFunctionDeclaration(ASTFunctionDeclaration astFunctionDeclaration, boolean includeNameSpace)
	{
		ASTFunctionGroup group = (ASTFunctionGroup) astFunctionDeclaration.getParent();
		ASTVariableDeclaration variableDeclaration = (ASTVariableDeclaration)group.getParent();

		// Get name and namespace. //
		boolean topLevel = variableDeclaration.getParent() instanceof ASTClass;
		boolean isConstructor = group.isConstructor();
		boolean isOverload = group.operatorOverload;

		String name;
		if (topLevel && isConstructor)
			name = getRawName((ASTClass)variableDeclaration.getParent());
		else if ((isOverload && possibleOverload(variableDeclaration))
				|| variableDeclaration.getName().equals(Syntax.ReservedNames.SELF))
			name = overloadOperatorToOperatorName.get(variableDeclaration.getName());
		else
			name = variableDeclaration.getName();
		String funNamespace = topLevel && includeNameSpace
				? getRawName((ASTClass)variableDeclaration.getParent()) + "::"
				: "";




		/// Create the declaration. ///
		StringBuilder declaration = new StringBuilder();

		if (astFunctionDeclaration.generics != null)
		{
			declaration.append("template <");
			for (int i = 0; i < astFunctionDeclaration.generics.length; i++)
			{
				String generic = astFunctionDeclaration.generics[i];
				declaration.append("typename ");
				declaration.append(generic);
				if (i != astFunctionDeclaration.generics.length -1)
				{
					declaration.append(", ");
				}
			}
			declaration.append("> ");
		}

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
		for (ASTBase baseChild : astFunctionDeclaration.children.getArgs())
		{
			ASTVariableDeclaration child = (ASTVariableDeclaration)baseChild;
			declaration.append(child.getExpressionType().getTypeName());
			declaration.append(' ');
			declaration.append(child.getName());
			if (child != astFunctionDeclaration.children.getLast(ListKey.ARGS))
				declaration.append(", ");
		}
		declaration.append(")");

		return declaration.toString();
	}

	@Override
	public void compileFunctionDeclaration(ASTFunctionDeclaration astFunctionDeclaration)
	{
		boolean justDeclaration = currentOutput == hppOutput;

		ASTFunctionGroup group = (ASTFunctionGroup) astFunctionDeclaration.getParent();
		String declaration = createFunctionDeclaration(astFunctionDeclaration, !justDeclaration);

		currentOutput.print(declaration);
		if (justDeclaration)
		{
			// If it's just a declaration, end it here. //
			currentOutput.println(";");
		}
		else
		{
			// Otherwise, continue...

			// A special case for constructors... //
			if (group.isConstructor() && astFunctionDeclaration.children.getBody().size() > 0)
			{
				ASTFunctionCall listInitSuperConstructorCall = (ASTFunctionCall) astFunctionDeclaration.children.getFirst();

				// Check if listInitSuperCall is actually valid... //
				if (listInitSuperConstructorCall.getDeclarationPath() instanceof ASTMemberAccess
						&& listInitSuperConstructorCall.getDeclarationPath().getEnd().equals("new"))
				{
					currentOutput.print(":");
					currentOutput.print(getRawName(astFunctionDeclaration.getContainingClass().extendsClassAST));
					currentOutput.print("(");

					for (ASTBase child : listInitSuperConstructorCall.children.getArgs())
					{
						child.compileSelf(this);
						if (child != listInitSuperConstructorCall.children.getLast(ListKey.ARGS))
							currentOutput.print(", ");
					}
					currentOutput.println(")");
				}
				else
				{
					System.err.println("ERROR: Constructor doesn't start with call to super constructor!");
				}
			}

			currentOutput.println("\n{");
			currentOutput.indentation++;
			for (ASTBase child : astFunctionDeclaration.children.getBody())
			{
				child.compileSelf(this);
				currentOutput.println(isSemicolonless(child) ? ' ' : ';');
			}
			currentOutput.indentation--;
			currentOutput.println("}");
		}
	}

	@Override
	public void compileNumber(ASTNumber astNumber)
	{
		currentOutput.print(astNumber.value);
	}

	@Override
	public void compileString(ASTString astString)
	{
		currentOutput.print('"' + astString.value + '"');
	}

	@Override
	public void compileReturnExpression(ASTReturnExpression astReturnExpression)
	{
		currentOutput.print("return ");
		astReturnExpression.children.getValue().get(0).compileSelf(this);
	}

	@Override
	public void compileMemberAccess(ASTMemberAccess astMemberAccess)
	{
		astMemberAccess.ofObject.compileSelf(this);

		if (astMemberAccess.ofObject instanceof ASTVariableUsage
		&& ((ASTVariableUsage)astMemberAccess.ofObject).getDeclaration() instanceof SpiritType)
			currentOutput.print("::");
		else
			currentOutput.print("->");
		ASTBase member = astMemberAccess.getMember();
		String memberName;
		if (member instanceof SpiritType)
			memberName = getRawName((SpiritType) member);
		else if (member.getName().equals(Syntax.ReservedNames.CONSTRUCTOR))
			memberName = getRawName(astMemberAccess.ofObject.getExpressionType());
		else
			memberName = member.getName();

		currentOutput.print(memberName);
	}

	@Override
	public void compileInline(ASTInline astInline)
	{
		for (String line : astInline.code.split("\n"))
			currentOutput.println(line);
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
