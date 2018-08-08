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
    val stroll = Stroll(entityStore)

    "support new with no properties" {
        val command = "new User {}"
        val result = stroll.runNew(command)!!
        result.localId shouldBe 0L
        result.typeId shouldBe 0
    }

    "support new with single property" {
        val command = "new User { age: 54 }"
        val result = stroll.runNew(command)!!
        result.localId shouldBe 1L
        result.typeId shouldBe 0
    }

    "support new with multiple properties" {
        val command = "new User { name: \"Bob\", username: \"bob\", age: 54 }"
        val result = stroll.runNew(command)!!
        result.localId shouldBe 2L
        result.typeId shouldBe 0
    }

    "support single link in new command" {
        val command = "new User { name: \"Margret\", contact -> 0-2 }"
        val result = stroll.runNew(command)!!
        result.localId shouldBe 3L
        result.typeId shouldBe 0
    }

    "support multiple links in new command" {
        val command = "new User { name: \"Bill\", supervises => [ 0-3, 0-2 ] }"
        val result = stroll.runNew(command)!!
        result.localId shouldBe 4L
        result.typeId shouldBe 0
    }

    "support five major types" {
        val command = "new TypeTest { int: 3, long: 1009L, double: 3.14, string: \"Test\", boolean: true }"
        val result = stroll.runNew(command)!!
        result.localId shouldBe 0L
        result.typeId shouldBe 1
        val txnResult = entityStore.computeInReadonlyTransaction { txn ->
            txn.find("TypeTest", "char", 'a').size() +
            txn.find("TypeTest", "int", 3).size() +
            txn.find("TypeTest", "long", 1009L).size() +
            txn.find("TypeTest", "double", 3.14).size() +
            txn.find("TypeTest", "string", "Test").size() +
            txn.find("TypeTest", "boolean", true).size() +
            //below should return 0
            txn.find("TypeTest", "boolean", "true").size() +
            txn.find("TypeTest", "double", 3.14f).size()
        }
        txnResult shouldBe 5
    }

    "add age and extra user link" {
        val command = "update 0-4 { age: 45, supervises => [ 0-1 ], supervises => 0-0 }"
        stroll.runUpdate(command)
    }

    "replace data for 0-1" {
        val command = "update 0-1 { age:24, name: \"Lil\" }"
        stroll.runSet(command)
    }

    "delete single entity" {
        stroll.runNew("new DeleteTest { }")
        val command = "delete 2-0"
        stroll.runDelete(command)
    }
    "delete list of entities" {
        stroll.runNew("new DeleteTest { test: 345}")
        stroll.runNew("new DeleteTest { blah: \"Stuff\"}")
        val command = "delete [2-1, 2-2]"
        stroll.runDelete(command)
    }

    "test that all DeleteTest entites have been deleted" {
        val command = "find DeleteTest {}"
        val result = stroll.runFind(command)!!
        result.size shouldBe 0
    }
    "test finding all Users" {
        val command = "find User {}"
        val result = stroll.runFind(command)!!
        result.size shouldBe 5
    }
    "test finding User based on properties" {
        val command = "find User { age: 24 }"
        val result = stroll.runFind(command)!!
        result.size shouldBe 1

        val command2 = "find User { name: \"Bill\", age: 45 }"
        val result2 = stroll.runFind(command2)!!
        result2.size shouldBe 1
    }

    "test finding User based on links" {
        val command = "find User { supervises => [ 0-1, 0-3 ] }"
        val result = stroll.runFind(command)!!
        result.size shouldBe 1

        val command2 = "find User { contact -> 0-2, name: \"Margret\" }"
        val result2 = stroll.runFind(command2)!!
        result2.size shouldBe 1
    }

    "test finding Users with property and link exists queries" {
        val command = "find User { supervises => _ }"
        val result = stroll.runFind(command)!!
        result.size shouldBe 1

        val command1 = "find User { supervises -> _ }"
        val result1 = stroll.runFind(command1)!!
        result1.size shouldBe 1

        val command2 = "find User { username: _ }"
        val result2 = stroll.runFind(command2)!!
        result2.size shouldBe 1
    }

    "test searching ranges with to" {
        val command = "find User { age: 40 to 49 }"
        val result = stroll.runFind(command)!!
        result.size shouldBe 1
    }

    "test using startsWith" {
        val command = "find User { name: startsWith \"Ma\" }"
        val result = stroll.runFind(command)!!
        result.size shouldBe 1

        val command1 = "find User { name: startsWith \"B\" }"
        val result1 = stroll.runFind(command1)!!
        result1.size shouldBe 2
    }
})
