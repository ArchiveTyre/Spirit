# Spirit Programming Language


## Introduction

## 0.0 Basic types in Spirit

Spirit has 11 basic types. They are as follows

| Spirit name | Memory  |      Descriptor name      |                         Description                         |                          Range                          |
| ---------- | ------- | ------------------------- | ----------------------------------------------------------- | ------------------------------------------------------- |
| char       | 8 bits  | Character                 | An 8 bit number representing a literal character.           | - 128 to 127                                            |
| uchar      | 8 bits  | Unsigned Character        | An 8 bit unsigned  number representing a literal character. | 0 to 255                                                |
| int        | 32 bits | Integer                   | A 32 bit whole number.                                      | -2147483648 to 2147483647                               |
| uint       | 32 bits | Unsigned Integer          | A 32 bit Natural number (positive integer).                 | 0 to 4294967295                                         |
| uint16     | 16 bits | Unsigned Short Integer    | A 16 bit Natural number (positive integer).                 | 0 to 65,535                                             |
| int64      | 64 bits | Long Integer              | A 64 bit whole number.                                      | -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807 |
| uint64     | 64 bits | Unsigned Long Integer     | A 64 bit Natural number (positive integer).                 | 0 to 18,446,744,073,709,551,615                         |
| float      | 32 bits | Floating Point Number     | A decimal number.                                           | +/- 3.4e +/- 38 (~7 digits)                             |
| double     | 64 bits | Big Floating Point number | A decimal number.                                           | +/- 1.7e +/- 308 (~15 digits)                           |
| bool       | 1 bit   | Boolean                   | A 1 bit value which is either true, or False                | 0 to 1                                                  |
| string     | --      | string                    | A sequence of characters                                    | --                                                      |


## 1.0 Spirit reserved words

## 2.0 Spirit Variables

## 3.0 Comments

A comment in Spirit is simply telling the compiler to ignore whatever
you put after the comment.
This is very useful, if you for example want to leave
a note of what you are doing, explain further or
temporarily disable pieces of code.

A single line comment in Spirit starts with a `%`.
Everything after this is ignored, and you may put
whatever you want there.

```Spirit
% Check if the user is logged in.
if logged_in
    % Display the admin panel
    display_admin_panel user.name
```

The other type of comment is a multiline comments. These comments
are not restricted to a single line, but starts with a 
`<% `, and ends with a `%>`. Everything between these characters
is ignored and will not be compiled.

**User.Spirit**
```Spirit
<%
 % The purpose of this class is to allow the
 % user to interact with the admin panel.
 %>
```

Note that the percent signs between `<%` and `%>`
are not necessary. They are only there because in Spirit it is
a convention to put an equal sign at the start of each line in a
mulitline comment. This is also valid:
```Spirit
<%This
is a perfectly
		fine and valid
multiline
comment.
			%>
```


## 4.0 Strings

There are three types ways to use text as data in Spirit.

The first one is through a normal string like this:

```spirit
my_string := "This is a string"

```
Strings are enclosed within double quotes: `"`


Multiline: <* String *>

Multiline raw: <** String **>



## 5.0 If statements

## 6.0 Loops

## 7.0 Functions

## 8.0 Match cases

## 9.0 Imports

## 10.0 Type definition

## 11.0 Generics

Generics in Spirit are very easy.

### Functions

**File.spirit**

```
myFunction : [T E] (myVar T, myOtherVar E) T
```



### In Function calls
**File.spirit**

```
myFunction [T E] val1 val2 valN
```

### In classes

*File.spirit*

```
with (T E)
```
































