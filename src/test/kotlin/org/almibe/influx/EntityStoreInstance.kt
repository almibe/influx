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

package org.almibe.influx


import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.nio.file.Files

object EntityStoreInstance {
    val entityStore: PersistentEntityStore = PersistentEntityStores.newInstance(Files.createTempDirectory("tmp").toFile())!!

    init {
        entityStore.executeInExclusiveTransaction {
            val nintendo = it.newEntity("Company")
            nintendo.setProperty("name", "Nintendo")

            val gb = it.newEntity("VideoGameSystem")
            gb.setProperty("name", "Game Boy")
            gb.setProperty("company", "Nintendo")
            gb.setLink("company", nintendo)
        }
    }
}
