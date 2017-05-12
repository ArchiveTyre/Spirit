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
	public ASTBase ofObject;
	ASTBase member = null;
	String memberName;

	public ASTBase getMember()
	{
		return member;
	}
	public String getMemberName() { return memberName;}

	public ASTMemberAccess(ASTParent parent, ASTBase ofObject, String memberName)
	{
		super(parent, "");
		this.ofObject = ofObject;
		ofObject.setParent(this);
		this.memberName = memberName;

		ArrayList<ASTBase> members =  ofObject.getExpressionType().getChildNodes();
		if (members != null)
		{
			for (ASTBase member : members)
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

	@Override
	public boolean compileChild(ASTBase child)
	{
		return false;
	}
}
