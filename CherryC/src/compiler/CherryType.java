package compiler;

import compiler.ast.ASTNode;

import java.util.ArrayList;

/**
 * @author Tyrerexus
 * @date 4/12/17
 */
public interface CherryType
{
	String getTypeName();
	ArrayList<ASTNode> getChildNodes();
}
