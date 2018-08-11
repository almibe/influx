/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package org.almibe.stroll.loader

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.almibe.stroll.CommandType

class StrollCommandListenerSpec : StringSpec({
    "new with single link" {
        val command = "new User { name: \"Margret\", contact -> 0-2, supervises => [ 0-3, 0-2] }"
        val result = readCommand(command)

        val commandArguments = CommandArguments(commandType = CommandType.NEW,
                entityType = "User")
        commandArguments.properties.put("name", Property("String", "Margret"))
        commandArguments.link.put("contact", "0-2")
        commandArguments.links.add(Pair("supervises", "0-3"))
        commandArguments.links.add(Pair("supervises", "0-2"))

        result shouldBe commandArguments
    }

    "five major types" {
        val command = "new TypeTest { int: 3, long: 1009L, double: 3.14, string: \"Test\", boolean: true }"
        val result = readCommand(command)

        val commandArguments = CommandArguments(commandType = CommandType.NEW,
                entityType = "TypeTest")
        commandArguments.properties["int"] = Property("Int", "3")
        commandArguments.properties["long"] = Property("Long", "1009L")
        commandArguments.properties["double"] = Property("Double", "3.14")
        commandArguments.properties["string"] = Property("String", "Test")
        commandArguments.properties["boolean"] = Property("Boolean", "true")

        result shouldBe commandArguments
    }

    "update by adding age and extra user link" {
        val command = "update 0-4 { age: 45, supervises => [ 0-1 ], supervises => 0-0 }"
        val result = readCommand(command)

        val commandArguments = CommandArguments(commandType = CommandType.UPDATE)
        commandArguments.entityId = "0-4"
        commandArguments.properties["age"] = Property("Int", "45")
        commandArguments.links.add(Pair("supervises", "0-1"))
        commandArguments.links.add(Pair("supervises", "0-0"))

        result shouldBe commandArguments
    }

    "set data for 0-1" {
        val command = "set 0-1 { age:24, name: \"Lil\" }"
        val result = readCommand(command)

        val commandArguments = CommandArguments(commandType = CommandType.SET)
        commandArguments.entityId = "0-1"
        commandArguments.properties["age"] = Property("Int", "24")
        commandArguments.properties["name"] = Property("String", "Lil")

        result shouldBe commandArguments
    }

    "delete single entity" {
        val command = "delete 2-0"
        val result = readCommand(command)

        val commandArguments = CommandArguments(commandType = CommandType.DELETE)
        commandArguments.entityIds.add("2-0")

        result shouldBe commandArguments
    }

    "delete list of entities" {
        val command = "delete [2-1, 2-2]"
        val result = readCommand(command)

        val commandArguments = CommandArguments(commandType = CommandType.DELETE)
        commandArguments.entityIds.add("2-1")
        commandArguments.entityIds.add("2-2")

        result shouldBe commandArguments
    }

    "find User based on links" {
        val command = "find User { supervises => [ 0-1, 0-3 ] }"
        val result = readCommand(command)

        val commandArguments = CommandArguments(commandType = CommandType.FIND)
        commandArguments.links.add(Pair("supervises", "0-1"))
        commandArguments.links.add(Pair("supervises", "0-3"))

        result shouldBe commandArguments
    }

    "find Users with property and link exists queries" {
        val command = "find User { supervises => _ }"
        val result = readCommand(command)

        val commandArguments = CommandArguments(commandType = CommandType.FIND)
        commandArguments.linkExistsCheck.add("supervises")
        result shouldBe commandArguments

        val command1 = "find User { supervises -> _ }"
        val result1 = readCommand(command1)
        val commandArguments1 = CommandArguments(commandType = CommandType.FIND)
        commandArguments1.linkExistsCheck.add("supervises")
        result1 shouldBe commandArguments1


        val command2 = "find User { username: _ }"
        val result2 = readCommand(command2)
        val commandArguments2 = CommandArguments(commandType = CommandType.FIND)
        commandArguments2.propertyExistsCheck.add("username")
        result2 shouldBe commandArguments2
    }

    "new and find with namespaced id" {
        val command = "new test.space.User { username:\"Juniper\" }"
        val result = readCommand(command)
        val commandArguments = CommandArguments(commandType = CommandType.NEW, entityType = "test.space.User")
        commandArguments.properties["username"] = Property("String", "Juniper")
        result shouldBe commandArguments
    }

    "simple command test" {
        val command = "list"
        val result = readCommand(command)
        val commandArguments = CommandArguments(commandType = CommandType.SIMPLE, commandName = "list")
        result shouldBe commandArguments
    }
})
