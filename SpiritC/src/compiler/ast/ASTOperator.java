package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;
import compiler.ast.ASTChildList.ListKey;

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
	 * Getter for rightExpression.
	 * @return Returns the right hand side of the operator.
	 */
	public ASTBase getRightExpression()
	{
		return children.getList(ListKey.OPERATOR_CALL).get(1);
	}

	/**
	 * Getter for leftExpression.
	 * @return Returns the left hand side of the operator.
	 */
	public ASTBase getLeftExpression()
	{
		return children.getList(ListKey.OPERATOR_CALL).get(0);
	}

	public ASTOperator(ASTChildList.ListKey key, ASTParent parent, String operatorName, ASTBase leftExpression, ASTBase rightExpression)
	{
		super(key, parent, operatorName);

		children.addList(ListKey.OPERATOR_CALL, 2);

		if (leftExpression != null)
		{
			leftExpression.setParent(ListKey.OPERATOR_CALL, this);
		}
		if (rightExpression != null)
		{
			rightExpression.setParent(ListKey.OPERATOR_CALL, this);
		}
	}

	@Override
	public SpiritType getExpressionType()
	{
		SpiritType operatorType = getRightExpression().getExpressionType();
		if (operatorType == getLeftExpression().getExpressionType())
			return operatorType;

		System.err.println("Unhandled error for non-matching call types. ");
		return null;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("(");
		if (getLeftExpression() != null)
			getLeftExpression().debugSelf(destination);
		destination.print(' ' + name + ' ');
		if (getRightExpression() != null)
			getRightExpression().debugSelf(destination);
		destination.print(")");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileOperator(this);
	}
}
