package compiler;

import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * Defines a simple type that can be used in this language.
 *
 * @author Tyrerexus
 * @date 4/12/17
 */
public interface CherryType
{
	String getTypeName();
	ArrayList<ASTBase> getChildNodes();
}
