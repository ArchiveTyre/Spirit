import Std.IO
import Std.OW

/** Include another file. */
import tests.Other

/* A very cute Hello World program. */
fun construct:

	/* Do some printing. */
	printf ("The answer to life, the universe and everthing is %d", kawaiiCalculations(7, 7))

	/* Create other class. */
	var Other other = new Other

	/* Change class variable. */
	other.hello_string = "Hi"

	/* Greet the user (^.^). */
	other.greet()

/* This function makes the class kawaii. */
fun kawaiiCalculations(int a_number, int another_number) -> int:

	/* Some addition. */
	var int the_sum = a_number + another_number

	/* Add 28. */
	the_sum = the_sum + 28

	return the_sum
