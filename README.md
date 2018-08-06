# Stroll
This project is a simple DSL for working with Xodus EntityStores' api.
It does not attempt to replace what can be done by directly working with the provided api.
Instead it focuses on allowing a simple command structure for adding and searching for entities.
**This project is still very early in its development.**
See `org.almibe.stroll.StrollSpec` in `src/test/kotlin` and issues for current status.

Example commands:
`new User { name: "Alex", hp: 42, employer => Employer#3 }`
`new Character { name: "Billy Lee", games => [ Game#34, Game#344, Game#324,Game#134 ], rightsHolder -> Company#404 }`
`delete Character#55`
`delete [Character#55, Character#556]`
`find Character { games => Game#45 }`
`update Character#44 { name: "Chuck Rock" }`
`set Character#45 { name: "Bobert" }`

Notes:
`:` sets a property
`->` calls `link` methods in Xodus api
`=>` calls `links` methods in Xodus api
`update` replaces or adds properties & links
`set` removes all existing properties and links and adds the newly provided ones
