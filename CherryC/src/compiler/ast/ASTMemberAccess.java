package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

import java.util.ArrayList;

/**
 * @author Tyrerexus
 * @date 4/30/17.
 */
public class ASTMemberAccess extends ASTParent
{
	public ASTNode ofObject;
	ASTNode member = null;
	String memberName;

	public ASTNode getMember()
	{
		return member;
	}

	public ASTMemberAccess(ASTParent parent, ASTNode ofObject, String memberName)
	{
		super(parent, "");
		this.ofObject = ofObject;
		ofObject.setParent(this);
		this.memberName = memberName;

		ArrayList<ASTNode> members =  ofObject.getExpressionType().getChildNodes();
		if (members != null)
		{
			for (ASTNode member : members)
			{
				if (member.getName().equals(memberName))
				{
					this.member = member;
					break;
				}
			}
		}

		if (member == null)
		{
			System.err.println("Could not find member: " + memberName + " in " + ofObject.getName());
		}

	}

	@Override
	public CherryType getExpressionType()
	{
		return member.getExpressionType();
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		ofObject.debugSelf(destination);
		destination.print(".");
		if (member != null)
			destination.print(member.getName());
		else
			destination.print("<<NOT FOUND>>");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileMemberAccess(this);
	}
}
