# Stroll
This project is a simple DSL for working with Xodus EntityStores' api.
It does not attempt to replace what can be done by directly working with the provided api.
Instead it focuses on allowing a simple command structure for adding and searching for entities.

**This project is still very early in its development.**

See `org.almibe.stroll.StrollSpec` in `src/test/kotlin` and issues for current status.

Example commands:

`new User { name: "Alex", hp: 42, employer => Employer#3 }`

`new Character { name: "Billy Lee", games => [ 0-34, 0-344, 0-324,0-134 ], rightsHolder -> 2-404 }`

`delete 1-55`

`delete [1-55, 1-556]`

`find Character { games => 0-45 }`

`update 1-44 { name: "Chuck Rock" }`

`set 1-45 { name: "Bobert" }`

Notes:

`45-76` is an example of an entity id

`:` sets a property

`->` calls `setLink` method in Xodus api

`=>` calls `addLink` method in Xodus api

`update` replaces or adds properties & links

`set` removes all existing properties and links and adds the newly provided ones
