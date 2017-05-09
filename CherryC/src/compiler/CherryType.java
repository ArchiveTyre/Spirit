package compiler;

import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * @author Tyrerexus
 * @date 4/12/17
 */
public interface CherryType
{
	String getTypeName();
	ArrayList<ASTBase> getChildNodes();
}
