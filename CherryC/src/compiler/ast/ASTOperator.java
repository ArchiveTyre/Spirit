package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Created by alex on 4/21/17.
 */
public class ASTOperator extends ASTParent
{


	ASTNode rightExpression;
	ASTNode leftExpression;

	public ASTNode getRightExpression()
	{
		return rightExpression;
	}

	public ASTNode getLeftExpression()
	{
		return leftExpression;
	}

	public ASTOperator(ASTParent parent, String operatorName, ASTNode rightExpression, ASTNode leftExpression)
	{
		super(parent, operatorName);

		if (rightExpression != null)
		{
			rightExpression.setParent(this);
			this.rightExpression = rightExpression;
		}
		if (leftExpression != null)
		{
			leftExpression.setParent(this);
			this.leftExpression = leftExpression;
		}
	}

	@Override
	public CherryType getExpressionType()
	{
		CherryType operatorType = rightExpression.getExpressionType();
		if (operatorType == leftExpression.getExpressionType())
			return operatorType;

		System.err.println("Unhandled error for non-matching call types. ");
		return null;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("(");
		if (leftExpression != null)
			leftExpression.debugSelf(destination);
		destination.print(' ' + name + ' ');
		if (rightExpression != null)
			rightExpression.debugSelf(destination);
		destination.print(")");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileOperator(this);
	}
}
