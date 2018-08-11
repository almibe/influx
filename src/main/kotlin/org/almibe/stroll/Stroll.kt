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

import jetbrains.exodus.entitystore.PersistentEntityStore
import org.almibe.stroll.loader.readCommand
import org.almibe.stroll.runner.StrollCommandRunner

enum class CommandType {
    NEW,
    UPDATE,
    SET,
    DELETE,
    FIND,
    SIMPLE
}

data class ReadProperty(val propertyName: String, val propertyType: String, val stringValue: String)
data class ReadLink(val linkName: String, val entityId: String)
data class ReadEntity(val entityType: String, val entityId: String,
                      val properties: Set<ReadProperty>, val links: Set<ReadLink>)

sealed class StrollResult(val commandType: CommandType)
data class NewResult(val newEntity: ReadEntity): StrollResult(CommandType.NEW)
data class UpdateResult(val updatedEntity: ReadEntity): StrollResult(CommandType.UPDATE)
data class SetResult(val setEntity: ReadEntity): StrollResult(CommandType.SET)
data class DeleteResult(val totalDeleted: Int): StrollResult(CommandType.DELETE)
data class FindResult(val entities: List<ReadEntity>): StrollResult(CommandType.FIND)
data class SimpleResult(val commandName: String, val result: String): StrollResult(CommandType.SIMPLE)

class Stroll(val entityStore: PersistentEntityStore) {
    private val commandRunner = StrollCommandRunner()

    fun run(command: String): StrollResult {
        val commandArguments = readCommand(command)
        return commandRunner.runCommandArguments(entityStore, commandArguments)
    }
}
