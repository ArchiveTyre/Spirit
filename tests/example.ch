/* A very cute Hello World program. */
fun construct:
	printf ("Hello %d", BeKawaii(4, 9))

/* This function makes the class kawaii. */
fun BeKawaii(int a_number, int another_number) -> int:

	var int the_sum = a_number + another_number
	the_sum = the_sum + 12
	printf "Hello World (^.^)\n"
	return the_sum
