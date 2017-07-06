package compiler.backends;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.Main;
import compiler.ast.*;
import compiler.lib.IndentPrinter;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Compiles an AST into a .sym file.
 *
 * @author Tyrerexus
 * @date 5/4/17.
 */

public class CompilerSYM extends LangCompiler
{
	public IndentPrinter symOutput = null;
	private PrintStream symStream = null;

	@Override
	public void compileClass(ASTClass astClass)
	{
		symOutput.println("ClassName: " + astClass.getName());
		symOutput.println("CompilerVersion: " + Main.VERSION);
		symOutput.println("ExtendsClass: " + astClass.extendsClass);
		symOutput.println();

		if (astClass.classImports.size() > 0)
		{
			for (ASTClass.ImportDeclaration importDeclaration : astClass.classImports)
			{
				symOutput.println("Dependency: " + importDeclaration.importPackage);
			}
			symOutput.println();
		}
		for (ASTBase node : astClass.children.getAll())
		{
			node.compileSelf(this);
			symOutput.println();
		}
	}

	@Override
	public void compileVariableDeclaration(ASTVariableDeclaration astVariableDeclaration)
	{
		symOutput.println("Var: " +
				astVariableDeclaration.getName() + " " +
				astVariableDeclaration.getExpressionType().getTypeName());
	}

	@Override
	public void compileFunctionDeclaration(ASTFunctionDeclaration astFunctionDeclaration)
	{
		for (ASTBase baseArg : astFunctionDeclaration.children.getArgs())
		{
			ASTVariableDeclaration arg = (ASTVariableDeclaration) baseArg;
			symOutput.println("Arg: " +
					arg.getName() + " " +
					arg.getExpressionType().getTypeName());
		}
		SpiritType returnType = astFunctionDeclaration.returnType;
		if (returnType == null)
		{
			System.err.println("COMPILER ERROR: No return type...");
		}
		symOutput.println("Fun: " +
				astFunctionDeclaration.getParent().getName() + " " +
				returnType.getTypeName());
	}

	@Override
	public void compileFunctionGroup(ASTFunctionGroup astFunctionGroup)
	{
		for (ASTBase node : astFunctionGroup.children.getBody())
		{
			node.compileSelf(this);
		}
	}

	@Override
	public void createFileStreams(String fileName)
	{
		try
		{
			symStream = new PrintStream("out/" + fileName + ".sym");
			symOutput = new IndentPrinter(symStream);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void closeStreams()
	{
		symStream.flush();
		symStream.close();
	}

	@Override
	public void compileIf(ASTIf astIf)
	{

	}

	@Override
	public void compileLoop(ASTLoop astLoop)
	{

	}

	@Override
	public void compileFunctionCall(ASTFunctionCall astFunctionCall)
	{

	}

	@Override
	public void compileVariableUsage(ASTVariableUsage astVariableUsage)
	{

	}

	@Override
	public void compileOperator(ASTOperator astOperator)
	{

	}

	@Override
	public void compileNumber(ASTNumber astNumber)
	{

	}

	@Override
	public void compileString(ASTString astString)
	{

	}

	@Override
	public void compileReturnExpression(ASTReturnExpression astReturnExpression)
	{

	}

	@Override
	public void compileMemberAccess(ASTMemberAccess astMemberAccess)
	{

	}

	@Override
	public void compileInline(ASTInline astInline)
	{

	}
}
