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

import io.kotlintest.specs.StringSpec
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.nio.file.Files

class StrollSpec : StringSpec({
    val entityStore: PersistentEntityStore = PersistentEntityStores.newInstance(Files.createTempDirectory("tmp").toFile())!!
    val stroll = Stroll(entityStore)

//    "new with no properties" {
//        val command = "new User {}"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "new"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "0-0"
//    }
//
//    "new with single property" {
//        val command = "new User { age: 54 }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "new"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "0-1"
//    }
//
//    "new with multiple properties" {
//        val command = "new User { name: \"Bob\", username: \"bob\", age: 54 }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "new"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "0-2"
//    }
//
//    "new with single link" {
//        val command = "new User { name: \"Margret\", contact -> 0-2 }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "new"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "0-3"
//    }
//
//    "new with multiple links" {
//        val command = "new User { name: \"Bill\", supervises => [ 0-3, 0-2 ] }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "new"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "0-4"
//    }
//
//    "five major types" {
//        val command = "new TypeTest { int: 3, long: 1009L, double: 3.14, string: \"Test\", boolean: true }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "new"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "1-0"
//        val txnResult = entityStore.computeInReadonlyTransaction { txn ->
//            txn.find("TypeTest", "char", 'a').size() +
//            txn.find("TypeTest", "int", 3).size() +
//            txn.find("TypeTest", "long", 1009L).size() +
//            txn.find("TypeTest", "double", 3.14).size() +
//            txn.find("TypeTest", "string", "Test").size() +
//            txn.find("TypeTest", "boolean", true).size() +
//            //below should return 0
//            txn.find("TypeTest", "boolean", "true").size() +
//            txn.find("TypeTest", "double", 3.14f).size()
//        }
//        txnResult shouldBe 5
//    }
//
//    "update by adding age and extra user link" {
//        val command = "update 0-4 { age: 45, supervises => [ 0-1 ], supervises => 0-0 }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "update"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "0-4"
//    }
//
//    "set data for 0-1" {
//        val command = "set 0-1 { age:24, name: \"Lil\" }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "set"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "0-1"
//    }
//
//    "delete single entity" {
//        stroll.run("new DeleteTest { }")
//        val command = "delete 2-0"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "delete"
//        result.getAsJsonPrimitive("total").asString shouldBe "1"
//    }
//    "delete list of entities" {
//        stroll.run("new DeleteTest { test: 345}")
//        stroll.run("new DeleteTest { blah: \"Stuff\"}")
//        val command = "delete [2-1, 2-2]"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "delete"
//        result.getAsJsonPrimitive("total").asString shouldBe "2"
//    }
//
//    "find that all DeleteTest entites have been deleted" {
//        val command = "find DeleteTest {}"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result.getAsJsonArray("results").size() shouldBe 0
//    }
//    "find all Users" {
//        val command = "find User {}"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result.getAsJsonArray("results").size() shouldBe 5
//    }
//    "find User based on properties" {
//        val command = "find User { age: 24 }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result.getAsJsonArray("results").size() shouldBe 1
//
//        val command2 = "find User { name: \"Bill\", age: 45 }"
//        val result2 = stroll.run(command2)
//        result2.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result2.getAsJsonArray("results").size() shouldBe 1
//    }
//
//    "find User based on link" {
//        val command = "find User { contact -> 0-2, name: \"Margret\" }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result.getAsJsonArray("results").size() shouldBe 1
//    }
//
//    "find User based on links" {
//        val command = "find User { supervises => [ 0-1, 0-3 ] }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result.getAsJsonArray("results").size() shouldBe 1
//    }
//
//    "find Users with property and link exists queries" {
//        val command = "find User { supervises => _ }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result.getAsJsonArray("results").size() shouldBe 1
//
//        val command1 = "find User { supervises -> _ }"
//        val result1 = stroll.run(command1)
//        result1.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result1.getAsJsonArray("results").size() shouldBe 1
//
//
//        val command2 = "find User { username: _ }"
//        val result2 = stroll.run(command2)
//        result2.getAsJsonPrimitive("operation").asString shouldBe "find"
//        result2.getAsJsonArray("results").size() shouldBe 1
//    }
//
//    "new and find with namespaced id" {
//        val command = "new test.space.User { username:\"Juniper\" }"
//        val result = stroll.run(command)
//        result.getAsJsonPrimitive("operation").asString shouldBe "new"
//        result.getAsJsonObject("result").getAsJsonPrimitive("entityId").asString shouldBe "3-0"
//
//        val findCommand = ""
//        val findResult = stroll.run(command)
//    }



//    "find within a range using to" {
//        val command = "find User { age: 40 to 49 }"
//        val result = stroll.run(command)
//        result.size shouldBe 1
//    }
//
//    "find using startsWith" {
//        val command = "find User { name: startsWith \"Ma\" }"
//        val result = stroll.run(command)
//        result.size shouldBe 1
//
//        val command1 = "find User { name: startsWith \"B\" }"
//        val result1 = stroll.run(command1)
//        result1.size shouldBe 2
//    }
})
