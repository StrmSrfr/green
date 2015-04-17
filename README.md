# Green 0.0.1: "Cosmic Dust"

This is a rough-draft sneak-peek developer-preview release of Green.
Everything may change between now and the 1.0.

Green is a lisp that runs in an interpreter on the JVM.  The
fundamental model behind Green is that every expression results in
an implicit future, which may not be evaluated until its results
are needed.  The ordering of side effects must be managed explicitly
by the programmer.  In its current incarnation, Green has no side
effects.

The current interpreter is a very rough draft and will probably
crash in the face of malformed programs.  In fact, there are probably
bugs which will cause it to crash in the face of some well-formed
programs as well.

# Dependencies

Green requires Java 8.

# The type hierarchy

Green programs operate on values which have types.

## T

The root of the type hierarchy is named `t`.  Every value in Green
is a member of this type.  T is short for true, and this type takes
its name from the fact that the predicate that determines if a value
is of type `t` simply returns true all the time.

The source code of a green program is also of type `t`.  They
generally consist of lists and symbols, but other objects may also
appear.

## List

As a lisp, Green has a concept of lists.  The list type is the union
of the cons and nil types.  Lists have a `car` and a `cdr`.

### Nil

Nil is the type of the empty list.  In Green programs it is written
`()`.  Nil's car is nil, as is its cdr.  Nil is `eq` to itself.

Unlike some other lisps, nil is not a symbol, and the symbol `nil`
is not bound to anything by default.

Nil also has the unique property of being false.  Any other value
used for a boolean is true.

### Cons

Cons forms the other half of the list type.  Each cons has a car
and a cdr, whose types are unrestricted.  Creating a cons with the
same car and cdr as another cons produces the same cons.

By placing a list in the cdr position, conses can be chained into
linked lists.

A cons that has something in its cdr position that is not a list
is what is conventionally referred to as a "dotted list".  It is
possible to construct dotted lists in Green, but there is no reader
syntax for them.

A chain of conses that ends in a cons whose cdr is in the chain is
a circular list.  It is not currently possible to construct circular
lists in Green, and it may never be.

## Symbol

A symbol is an object whose name is a string.  Symbols with alphabetic
names are denoted in the reader syntax by those names.

Green does not currently have a string type, so there is no way to
get at a symbol's name in your program.  For similar reasons, there
is currently no way to create a symbol in a Green program short of
including it in the source.

The reader interns symbols, meaning that each copy of the same name
in a Green program results in the same symbol.

## Integer

Green currently has one numeric type, the non-negative integers.
These are represented in the runtime by `java.math.BigInteger`s.
They are represented in source code as `0b` followed by their binary
representation.

## Special Operator

Special operators are types that result in useful special effects
when they are used in the first position of a list in a Green
program.  A number of symbols evaluate to special operators in the
default lexical context.  At the moment it is possible to use these
symbols outside of their useful position, which will get you the
special operator object.  The set of special operators is very
small, and likely to remain so.

## Function

Function objects are similar to special operators in that they
result in a useful effect when used in the first position of a list.
The value of such an expression is the result of applying the
function to the values of the remaining subexpressions.  Note that
due to the implicit futures, the function may return a value before
all of its parameters have been evaluated, if the data flow permits
this.

## Exception

Currently exceptions are just another kind of value in Green.
Exceptions are different in that, in the absence of a special
operator, an expression which has an exception as a subexpression
takes on the value of that exception rather than whatever value it
might otherwise have.  Thus, exceptions propogate rather like NaN.
Each special operator has its own rules as to the value of its
expression in the face of an exceptional subexpression.

# Special Operators

## Quote

Quote is the simplest special operator.  It is used in this way:

    (quote x)

Quote prevents the evaluation of its operand.  So in this case,
`(quote x)` evaluates to the symbol `x`, whereas just `x` would have
caused an error that `x` is not bound to a value.

Quote can also quote more complicated structures.

## If

The if operator takes the following form:

    (if condition then [else])

The square brackets here are metasyntax indicating that the `else`
portion is optional.

If the condition is exceptional, if returns it.  If the condition is
true, if returns the evaluation of the `then` portion.  If the
condition is false, if returns the evaluation of the `else` portion.
If there is no else portion and it would be returned, if returns the
empty list.

## Lambda

Lambda is used to create functions and bindings.  It takes this form:

    (lambda list expression)

Where the `list` is a list of symbols.  Lambda returns a function.
When the function is called, the `expression` is evaluated in a
lexical environment constructed by combining the lexical environment
in which the lambda expression appeared with a lexical enviroment
created by binding each symbol in the lambda list to the corresponding
argument in the function invocation.

For example, this will return the symbol `y`:

    ((lambda (x) x) (quote y))

## Macrolet

Macrolet is used to create macros.  Within the body of a macrolet
defining a macro named `macrox`, expressions of the form `(macrox
...)` will be replaced with the result of calling macrox's expansion
function on the unevaluated arguments, and then the new form is
evaluated.

Macrolet takes the form:

    (macrolet macros body)

Where `macros` is a list of items of this shape:

    (name arguments expression)

Here `name` is the name of the macro and the rest is similar to a
lambda expression.  The `arguments` is a list of symbols which will be
replaced with the unevaluated forms in the expression to determine the
expansion.

Here is an example from the test suite:

    (macrolet ((macrox () (quote x)))
      (macrox))

This defines a macro named `macrox` with an expansion function of no
arguments.  The expansion function evaluates to the symbol x.  Thus,
the form `(macrox)` is replaced with `x` prior to evaluation, which
causes an error as x has not been bound.

## repl

The repl special form is a total hack.  It gives you a new repl in
which the lexical environment is the lexical environment in which it
appears.  When end of file is reached, the repl form returns the last
value that it printed.

# Running
