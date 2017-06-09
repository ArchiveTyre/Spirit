# Spirit
The language of discipline.
```
add : (x, y) int = x + y

new : ()
  sum := add 1 2
  print sum
```

## What is Spirit?
A little compiler.

## What is implemented?
* If
* Else
* Match
* Variables
* Operators
* Classes.

## Features
* Compiles to C++ for compatiblity with existing code bases.
* Each file is a class.
* Indent-based syntax.
* Static-typing
* Clean & multiplatform standard library.
* Garbage collection (optional)

## Syntax
```
% This is a comment
variable : type = value
function_name : (arg1 : arg1_type, arg2 : arg2_type) return_type
   return_value := (call1 (call2 arg1) arg2)
   = return_value
 %> This is a 
    Multiline comment. <%
```

## Installation (not finished)
1. `git clone -b dev git@github.com:tyrerexus/Spirit.git`
2. Compile it
3. 

## What is planned?
* Generics
* Support for backends other than C++
