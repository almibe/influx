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

package org.almibe.stroll

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.nio.file.Files

class StrollSpec : StringSpec({
    val entityStore: PersistentEntityStore = PersistentEntityStores.newInstance(Files.createTempDirectory("tmp").toFile())!!
    val influx = Stroll(entityStore)

    "support new with no properties" {
        val command = "new User {}"
        val result = influx.runNew(command)!!
        result.localId shouldBe  0L
        result.typeId shouldBe 0
    }

    "support new with single property" {
        val command = "new User { age: 54 }"
        val result = influx.runNew(command)!!
        result.localId shouldBe  1L
        result.typeId shouldBe 0
    }

    "support new with multiple properties" {
        val command = "new User { name: \"Bob\", username: \"bob\", age: 54 }"
        val result = influx.runNew(command)!!
        result.localId shouldBe  2L
        result.typeId shouldBe 0
    }

    "support single link in new command" {
        val command = "new User { name: \"Margret\", contact -> User#2 }"
        val result = influx.runNew(command)!!
        result.localId shouldBe  3L
        result.typeId shouldBe 0
    }

    "support multiple links in new command" {
        val command = "new User { name: \"Bill\", supervises => [ User#3, User#2 ] }"
        val result = influx.runNew(command)!!
        result.localId shouldBe  4L
        result.typeId shouldBe 0
    }

    "add age and extra user link" {
        val command = "update User#4 { age: 45, supervises => [ User#1 ], supervises => User#0 }"
        influx.runUpdate(command)
    }

    "replace data for User#1" {
        val command = "update User#1 { age:24, name: \"Lil\" }"
        influx.runSet(command)
    }

    "test finding all Users" {
        val command = "find User {}"
        val result = influx.runFind(command)!!
        result.size shouldBe 5
    }
    "test finding User based on properties" {
        val command = "find User { age: 24 }"
        val result = influx.runFind(command)!!
        result.size shouldBe 1

        val command2 = "find User { name: \"Bill\", age: 45 }"
        val result2 = influx.runFind(command2)!!
        result2.size shouldBe 1
    }

    "test finding User based on links" {
        val command = "find User { supervises => [ User#1, User#3 ] }"
        val result = influx.runFind(command)!!
        result.size shouldBe 1

        val command2 = "find User { contact -> User#2, name: \"Margret\" }"
        val result2 = influx.runFind(command2)!!
        result2.size shouldBe 1
    }

    "delete single entity" {
        val command = "delete User#1"
        influx.runDelete(command)
    }
    "delete list of entities" {
        val command = "delete [User#2, User#3]"
        influx.runDelete(command)
    }
    "check number of Users after delete calls" {
        val command = "find User {}"
        val result = influx.runFind(command)!!
        result.size shouldBe 2
    }
})
