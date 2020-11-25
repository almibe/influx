# Wander

Wander is an experimental scripting language for working with data dynamically.
It is currently being developed as part of the Ligature project, which provides support for running Wander scripts against its quad stores.
Wander tries to combine ideas existing RDF serialization formats (namely Turtle and N3), SPARQL, and modern general purpose languages (mainly Kotlin, Scala, and Rust).

Goals of Wander
 - be a small and easy to learn language for most people with any scripting background and some (knowledge of|interest in) linked data
 - make heavy use of streams, expressions, and pattern matching to solve problems (no manual loops)
 - support all features SPARQL has (and probably eventually be used for Ligature's SPARQL implemenation)
 - support immutability, persistent data structures, and functional concepts
 - provide a variety of options for handling the output of a script (table, triples/quads, json, csv, xml, visualization)
 - run on JVM or JavaScript platform

Relation to Turtle/SPARQL
 - support for @base and @prefix definitions
   - must be at top of file below read/write declaration
   - can't repeat base or override prefixes
 - support for iri literals (including use of prefix and base)
 - the keyword a is a shortcut for type-of predicate iri
 - support for typed literals and lang literals
 - use # for comments
 - allow querying external stores via SPARQL endpoints
 - support for statement literals and graph pattern match literals

Relation to Scala/Kotlin/Rust/Modern Langs In General
 - (Rust not Kotlin) use `let` to define immutable variables (mutable variables are not supported)
 - dynamically typed so no type declarations
 - kotlin style lambdas sans type declarations (no planned support for function declarations just lambdas)
 - when expressions for control flow (there are no plans are in place to support other control flow mechanisms)
 - denote ranges with ..
 - support for 'in' and '!in' for working with ranges and collections
 - support for 'is' and '!is' for checking types
 - no support for kotlin style comments, use # instead

Unique-ish concepts
 - In memory graphs
   - a new data structure that represents a graph/quadstore in memory
   - create with graph() - all graph instances are mutable
 
https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=11&cad=rja&uact=8&ved=2ahUKEwiw6Mj_yMHjAhWLQc0KHTPADts4ChC3AjAAegQICRAB&url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DfabN6HNZ2qY&usg=AOvVaw1h2AovvmN_cBDBTQWnca8Z

example of a problem hard to solve in sparql
https://web.archive.org/web/20150516154515/http://answers.semanticweb.com:80/questions/9842/how-to-limit-sparql-solution-group-size

built in functions
 - collection functions
 - SPARQL's functions

``` 
@prefix : <http://localhost>
@prefix test: <http://localhost/test>
@base <http://localhost/base>

let iri = <http://test>
let iri2 = <#frombase>
let iri3 = :fromPrefix
let iri4 = test:alsoFromPrefix
let graphs = $.graphs
let statementInDefaultGraph = iri a :resource
let statementInNamedGraph = iri a :resource :graph2
let list = list()
let map = map()
let set = set()
let mutList = mutList()
let mutMap = mutMap()
let mutSet = mutSet()
let pair = 5 to "hello"
let statement = $.matchStatements(iri <http://predicate/something> :prefix)
#or
let specificStatements = $.match(iri <http://predicate/something> :prefix) #all statements that match pattern across all graphs
let statements = $.match(iri ? ? ?) #all statements with that subject

let lambda = {x,y -> x+y} #define a lambda

let nine = lambda(4, 5) #call it

let result = when {
  graphs.count > 10 -> :morethanten
  graphs.count > 1 -> :singledigitplural
  else -> :oneorzero
}

let useResult = result a :quantity

let result2 = when(useResult.subject) {
  :morethanten -> 11
  :singledigitplural -> 5
  else -> 0
}

let useResult2 = result2 * 100

let blah = graph()
let blah2 = map("hello" to "world")

blah.addStatements(list(
  useResult,
  :first test:second <#third> :graph2
))

when {
  blah.subjects.count > 90 -> return blah
  else -> return blah2
}
```

### Usage

This is a normal sbt project, you can compile code with `sbt compile` and run it
with `sbt run`, `sbt console` will start a Dotty REPL.

For more information on the sbt-dotty plugin, see the
[dotty-example-project](https://github.com/lampepfl/dotty-example-project/blob/master/README.md).
