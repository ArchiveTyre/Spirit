

Variables kind of:
LANG_NAME	- Name of language
LANG_EXT  - File extension


# LANG_NAME



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
* Every LANG_NAME is a class.
* Object Oriented
* Garbage Collection
* Optional Manual Memory Management
* The All Mighty Loop keyword



## Syntax


### Variables

#### Declaration

To declare a new variable, there are two keywords. Either `var`or `con`.
`var`is a mutable variable, and `con` is a constant.


**Example.LANG_EXT**

```LANG_NAME
var a = 2
a = 123 # Ok.

con b = 5
b = 123 # ERROR!
```

To declare a variable with no value you simply specify the type using the `:` operator.

**Example2.LANG_EXT**

```
var a : int

# You can also specify type with a value like this:
var b : int = 5	
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

There are many ways to create a loop in LANG_NAME.  
The general syntax for a loop is as follows:

```
loop DECLARATIONS if CONDITION then EXPRESSION:
	# What happens in the loop
```

For example:


**Example.LANG_EXT**

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

**Example.LANG_EXT**

```
loop 10 as i:
	print i
# Will output 0123456789.

# You can also omit the variable like so:
loop 10
	# This will run 10 times
```








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
