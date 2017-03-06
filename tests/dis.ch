
/**
 * This defines a Cat class.
 */
class Cat:
	var name: string = "Unamed desu"
	var weight = 43

/**
 * Defines a function.
 */
fun Cat::Meow(self)
	print "Hello my name is: " ~ self.name ~ " and I weight: " ~ $self.weight

static fun Cat::Meow()
	print "Cats are very cute creatures"

/* Declare a nice cat. */
var momo = new Cat

/* Set the cat's name. */
momo.name = "Momo"

/* Meow cat! */
momo.Meow()
