# Stroll
This project is a simple DSL for working with Xodus EntityStores' api.
It does not attempt to replace what can be done by directly working with the provided api.
Instead it focuses on allowing a simple command structure for adding and searching for entities
within the context of debugging or management.

**This project is still very early in its development.**

See `org.almibe.stroll.StrollSpec` in `src/test/kotlin` and issues for current status.

Example usage from within Kotlin with explicit return types:

```kotlin
val stroll = Stroll(entityStore)
val alexId: EntityId = stroll.runNew("""new User { name: "Alex", hp: 42, employer => Employer#3 }""")
val billId: EntityId = stroll.runNew("""new Character { name: "Billy Lee", games => [ 0-34, 0-344, 0-324,0-134 ], rightsHolder -> 2-404 }""")
stroll.runDelete("""delete 1-55""") //returns nothing
stroll.runDelete("""delete [1-55, 1-556]""") //returns nothing
val entityIds: List<EntityId> = stroll.runFind("""find Character { games => 0-45 }""")
stroll.runUpdate("""update 1-44 { name: "Chuck Rock" }""") //returns nothing
stroll.runSet("""set 1-45 { name: "Bobert" }""") //returns nothing
```

Notes:

`45-76` is an example of an entity id

`:` sets a property

`->` calls `setLink` method in Xodus api

`=>` calls `addLink` method in Xodus api

`_` means this property or link has been set when used in a find `username: _` `spouse -> _`

`to` is used to search for a range `age: 5 to 50` **NOT SUPPORTED YET SEE ISSUE #9**

`startsWith` is used to search for a string that starts with a value `username: startsWith "bob"` **NOT SUPPORTED YET SEE ISSUE #9**

`update` replaces or adds properties & links

`set` removes all existing properties and links and adds the newly provided ones

All floating point numbers are double

All fixed point numbers all ints unless they have an L after them then they are Longs

Booleans are represented with the keyword true or false

Stroll does not support nulls