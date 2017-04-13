


# Raven



## Requirements

* Suitable for:
  *  Game Dev 
  *  AI
  *  **Heavyweight projects**
  * Web stuff
* Compact
* Readable
* Not much syntax
* Type inference
* Type safe
* Statically typed
* Compact
* Thread safe
* Serializable
* Object Oriented



## Main Features
* Indent based
* Serializable
* Every Raven is a class.
* Object Oriented
* Garbage Collection
* Optional Manual Memory Management
* The All Mighty Loop keyword



## Syntax

### Main Function

The main function, also known as the entry point of the code is specified using the `start` keyword.  
It looks like this:

```
start:
	# Code
```

### Variables

#### Declaration

To declare a new variable, there are two keywords. Either `var`or `con`.
`var`is a mutable variable, and `con` is a constant.


**Example.raven**

```Raven
var a = 2
a = 123 # Ok.

con b = 5
b = 123 # ERROR!
```

To declare a variable with no value you simply specify the type using the `:` operator.

**Example2.raven**

```
var a : int

# You can also specify type with a value like this:
var b : int = 5	
```

### Functions

#### Declarations
To declare a function you use the `fun` keyword.
Then you declare the name of the function, specify the parameters (if any), and declare the return type.  
The syntax for declaring a function is the following:

```
fun function_name(param1 : param_type, param2 : param_type) -> return_type:
```

Example:
**Example.raven**

```
fun add(a : int, b : int) -> int:
	return a + b
	
start:
	print add(1, 5) # Will output 6
```


### Control Flow (if, match, loop, try, break, continue...

#### IF statement
The syntax for an if statement is as follows:


```
if CONDITION:
	STATEMENTS
elseif CONDITION:
	STATEMENTS
else
	STATEMENTS
```

#### MATCH statement

The syntax for a match statement is as follows:

```
match VARIABLE:
	case 1, 2:
		SOMETHING # Run if 1 or 2
	case 2:
		SOMETHING else
	otherwise:
		DEFAULT STATEMENT
```
break statement is not needed.

#### LOOP statement

There are many ways to create a loop in Raven.  
The general syntax for a loop is as follows:

```
loop DECLARATIONS if CONDITION then EXPRESSION:
	# What happens in the loop
```

For example:


**Example.raven**

```
loop var i = 0 if i < 10 then i++:
	print i
	
# Will output: 0123456789.
```

Using this syntax, you can omit any of the three. All of the below are valid loops:

```
loop var i = 2 then i++:
	# will run forever unless break statement is found

loop if a == 2
	# will loop as long as a is 2
	
loop:
	# will run forever


# NOT RECOMMENDED
var i = 2
loop then i++:
	# Will run forever and always increment i

```

You can also use this alternative loop syntax if you are going to loop with a counter.

```
loop AMOUNT_OF_TIMES as VARIABLE
```

Example:

**Example.raven**

```
loop 10 as i:
	print i
# Will output 0123456789.

# You can also omit the variable like so:
loop 10
	# This will run 10 times
```

You can also iterate over an array like this:

```
loop array as item:
	print item	
```

#### TRY/RESCUE/ALWAYS statement

A try statement is a way to do error handling. If everything runs as it should,
the body of the try statement will be run, but if an error is thrown, then the appropriate rescue statment is run.
The always block is always run.
The syntax looks as follows:

```
try:
	# Risky stuff that could throw an exception.	
rescue Exception as e:
	# Error handling.
always:
	# Cleanup, and other stuff that should always run.
```

#### 





for my_players -> player_name:
	print player_nane
	
for (my_players, player_name):
	print player_name




public
hidden
family

var a = 3
var b = 7
con c = 123
