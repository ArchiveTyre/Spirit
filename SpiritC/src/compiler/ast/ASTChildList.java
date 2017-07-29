package compiler.ast;

import compiler.lib.Helper;

import java.util.*;

/**
 * Created by david on 7/1/17.
 */


// Unsafe casts can be made with: (Collection<? extends ASTVariableDeclaration>)    /tyrerexus

public class ASTChildList
{
	public enum ListKey
	{
		BODY,
		ARGS,
		PATH,
		CONDITION,
		FOR_INIT,
		FOR_ITERATIONAL,
		FOR_CONDITION,
		VALUE,
		OPERATOR_CALL,
	}

	public Map<ListKey, List<ASTBase>> children = new EnumMap<>(ListKey.class);

	public ASTParent origin;


	public ASTChildList(ASTParent origin)
	{
		this.origin = origin;
	}


	public void addChild(ListKey key, ASTBase child)
	{
		if (children.containsKey(key))
		{
			children.get(key).add(child);
		}
		else
		{
			throw new Error("You stupid baka... (-_-)\nKey: " + key + " doesn't exist!");
		}

	}

	public void removeChild(ASTBase child)
	{
		for (List<ASTBase> lists: children.values())
		{
			lists.remove(child);
		}
	}

	public void addLists(ListKey... listKeys)
	{
		for (ListKey key : listKeys)
		{
			children.put(key, new ArrayList<>());
		}
	}


	public void addList(ListKey key, int size)
	{
		children.put(key, new ArrayList<>(size));
	}

	public ASTBase[] getAll()
	{
		return Helper.combineList(children.values());
	}

	public ASTBase getLast()
	{
		return getLast(ListKey.BODY);
	}

	public ASTBase getLast(ListKey key)
	{
		List<ASTBase> body = children.get(key);
		return body.get(body.size() - 1);
	}

	public ASTBase getFirst()
	{
		return children.get(ListKey.BODY).get(0);
	}







	public List<ASTBase> getList(ListKey key) { return children.get(key); }

	public List<ASTBase> getBody() 			{ return getList(ListKey.BODY); 			}
	public List<ASTBase> getArgs() 			{ return getList(ListKey.ARGS); 			}
	public List<ASTBase> getPath() 			{ return getList(ListKey.PATH); 			}
	public List<ASTBase> getValue() 		{ return getList(ListKey.VALUE); 			}
	public List<ASTBase> getForInit() 		{ return getList(ListKey.FOR_INIT); 		}
	public List<ASTBase> getCondition() 	{ return getList(ListKey.CONDITION); 		}
	public List<ASTBase> getForIncrement() 	{ return getList(ListKey.FOR_ITERATIONAL); 	}
	public List<ASTBase> getForCondition() 	{ return getList(ListKey.FOR_CONDITION); 	}





}
