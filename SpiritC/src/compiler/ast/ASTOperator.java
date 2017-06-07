package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Defines a operator usage in the ast. <br>
 * Example: 32 * 42
 *
 * @author Tyrerexus
 * @date 4/21/17.
 */
public class ASTOperator extends ASTParent
{

	/**
	 * Right hand side of the operator
	 */
	private ASTBase rightExpression;

	/**
	 * Left hand side of the operator.
	 */
	private ASTBase leftExpression;

	/**
	 * Getter for rightExpression.
	 * @return Returns the right hand side of the operator.
	 */
	public ASTBase getRightExpression()
	{
		return rightExpression;
	}

	/**
	 * Getter for leftExpression.
	 * @return Returns the left hand side of the operator.
	 */
	public ASTBase getLeftExpression()
	{
		return leftExpression;
	}

	public ASTOperator(ASTParent parent, String operatorName, ASTBase rightExpression, ASTBase leftExpression)
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
	public SpiritType getExpressionType()
	{
		SpiritType operatorType = rightExpression.getExpressionType();
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

	@Override
	public boolean compileChild(ASTBase child)
	{
		return false;
	}
}
