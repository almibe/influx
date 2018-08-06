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
    val influx = Stroll(entityStore)

    "support new with no properties" {
        val command = "new User {}"
        val result = influx.run(command)!!.first!!
        result.type shouldBe "User"
        result.propertyNames.size shouldBe 0
    }

    "support new with single property" {
        val command = "new User { age: 54 }"
        val result = influx.run(command)!!.first!!
        result.type shouldBe "User"
        result.getProperty("age") shouldBe 54
        result.propertyNames.size shouldBe 1
    }

    "support new with multiple properties" {
        val command = "new User { name: \"Bob\", username: \"bob\", age: 54 }"
        val result = influx.run(command)!!.first!!
        result.type shouldBe "User"
        result.getProperty("age") shouldBe 54
        result.getProperty("name") shouldBe "Bob"
        result.getProperty("username") shouldBe "bob"
        result.propertyNames.size shouldBe 3
    }
})
