/* This file was made to test the AST in the early days */
/* This is in no way the final syntax of the language */


fun Main:
	DoSomething()
	print (Escape())
	DoSomethingElse()
	print (LOL)
	a = 4 * (4 + 2)
fun DoSomething:
	print ("Something is being done")

fun DoSomethingElse:
	print ("Something else is being done")

fun SomeCals, A, B:
	if A = B:
		print ("A is bigger than B")
		return (true)
	else:
		return (false)

fun Test:
		if A = B:
			return (false)
	if A = B:
		return true
	return true lol
