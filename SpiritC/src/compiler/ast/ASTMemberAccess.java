package compiler.ast;

import compiler.SpiritType;
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
public class ASTMemberAccess extends ASTParent implements ASTPath
{
	/**
	 * The object we are accessing.
	 */
	public ASTPath ofObject;

	/**
	 * The member name.
	 */
	private String memberName;

	/**
	 * The find member based on {@link #getMemberName()}
	 * @return The member that was found.
	 */
	public ASTBase getMember()
	{
		ASTBase member = null;

		ArrayList<ASTBase> members = ofObject.getExpressionType().getChildNodes();
		if (members != null)
		{
			for (ASTBase possibleMember : members)
			{
				if (possibleMember.getName().equals(memberName))
				{
					member = possibleMember;
					break;
				}
			}
		}

		if (member == null)
		{
			System.err.println("Could not find member: " + memberName + " in " + ofObject.getName());
		}
		return member;
	}

	/**
	 * Getter for memberName.
	 * @return Returns the name of the member we are trying to access.
	 */
	public String getMemberName() { return memberName;}

	public ASTMemberAccess(ASTChildList.ListKey key, ASTParent parent, ASTPath ofObject, String memberName)
	{
		super(key, parent, "");

		children.addLists(ASTChildList.ListKey.VALUE);

		this.ofObject = ofObject;
		ofObject.setParent(ASTChildList.ListKey.VALUE, this);
		this.memberName = memberName;
	}

	@Override
	public SpiritType getExpressionType()
	{
		return getMember().getExpressionType();
	}

	@Override
	public String getEnd()
	{
		return memberName;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		ofObject.debugSelf(destination);
		destination.print(".");
		if (getMember() != null)
			destination.print(getMember().getName());
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
