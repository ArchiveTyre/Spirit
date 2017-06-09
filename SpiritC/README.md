## SpiritC is the official Spirit compiler.

## Directory tree:
* src/compiler/ast - Package that contains classes for the AST tree.
* src/compiler/backends - All the backends for spiritc. A backend is like an output form.
* src/compiler/builtins - Contiains all builtin types and classes of spirit.
* src/compiler/lib - Mainly contains helper functioins & classes. Ex: IndentPrinter
* src/compiler/tests - Contains all the test cases. 

## Important files and classes:
* Main.java - The main class.
* Lexer.java - The spirit tokenizer.
* Parser.java - Takes in tokens to create an AST tree.
* Syntax.java - Contains all the keywords for the syntax and other important variables.

