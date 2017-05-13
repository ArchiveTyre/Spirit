package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

import java.util.ArrayList;

/**
 * Defines a member access for a variable.<br>
 * Example: "world.hello"
 *
 * @author Tyrerexus
 * @date 4/30/17.
 */
public class ASTMemberAccess extends ASTParent
{
	/**
	 * The object we are accessing.
	 */
	public ASTBase ofObject;

	/**
	 * The found member.
	 */
	private ASTBase member = null;

	/**
	 * The member name.
	 */
	private String memberName;

	/**
	 * The found member based on {@link #getMemberName()}
	 * @return The member that was found.
	 */
	public ASTBase getMember()
	{
		return member;
	}

	/**
	 * Getter for memberName.
	 * @return Returns the name of the member we are trying to access.
	 */
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
