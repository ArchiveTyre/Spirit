import Io
import Other

/* A very cute Hello World program. */
fun construct:

	/* Do some printing. */
	printf ("Hello %d", beKawaii(4, 9))

	/* Create other class. */
	var Other other  = new Other
	other.hello = "Hi world!"
	other.greet

/* This function makes the class kawaii. */
fun beKawaii(int a_number, int another_number) -> int:

	/* Some addition. */
	var int the_sum = a_number + another_number
	the_sum = the_sum + 12

	/* Print a kawaii message and return. */
	printf "Hello World (^.^)\n"
	return the_sum

#inline int a; #endinline
