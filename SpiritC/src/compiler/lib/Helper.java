package compiler.lib;

import compiler.ast.ASTBase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class contains some static methods for making your life a lot easier. <3
 *
 * @author david, Tyrerexus
 * @date 6/30/17
 */
public class Helper
{
	//@SafeVarargs public static <T> T[] combineList(List<T>... lists)
	//{
//		return combineList(Arrays.asList(lists));
	//}

	public static ASTBase[] combineList(Collection<List<ASTBase>> lists)
	{
		int size = 0;
		for (List list : lists)
		{
			size += list.size();
		}

		@SuppressWarnings("unchecked")
		//ASTBase[] output = ((ASTBase[]) new ArrayList<ASTBase>().toArray());
		// Thats some cancer code....

		ASTBase[] output = new ASTBase[size];
		int index = 0;
		for (List list : lists)
		{

			for (Object item : list)
			{
				output[index] = (ASTBase) item;
				index++;
			}
		}

		return output;
	}
}
