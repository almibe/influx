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

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.EntityId
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.StoreTransaction
import org.almibe.stroll.tokenizer.StrollToken
import org.almibe.stroll.tokenizer.TokenType
import org.almibe.stroll.tokenizer.Tokenizer

data class CommandArguments (
        //three collections used by all commands
        val properties: MutableMap<String, StrollToken> = mutableMapOf(),
        val link: MutableMap<String, StrollToken> = mutableMapOf(),
        val links: MutableMap<String, StrollToken> = mutableMapOf(),
        //collections only used by find
        val propertyExistsCheck: MutableList<String> = mutableListOf(),
        val linkExistsCheck: MutableList<String> = mutableListOf()
        //TODO eventually this will contain range and startsWith info
)

class Stroll(private val entityStore: PersistentEntityStore) {
    private val tokenizer = Tokenizer()

    private fun tokenize(command: String): Iterator<StrollToken> = tokenizer.tokenize(command).iterator()

    //TODO new,set,update,find will all use this
    private fun readCommandArguments(itr: Iterator<StrollToken>): CommandArguments? {
        val commandArguments = CommandArguments()
        val brace = itr.next()
        assert(brace.tokenType == TokenType.START_BRACE)
        while (itr.hasNext()) {
            val next = itr.next()
            when (next.tokenType) {
                TokenType.END_BRACE -> {
                    if (itr.hasNext()) {
                        return null //error
                    } else {
                        //ready to run command
                    }
                }
                TokenType.KEYWORD -> {
                    val propertyName = next.tokenContent
                    val colonOrLink = itr.next()
                    val value = itr.next()

                    if (colonOrLink.tokenType == TokenType.COLON) {
                        when (value.tokenType) {
                            TokenType.INT -> commandArguments.properties[propertyName] = value
                            TokenType.DOUBLE -> commandArguments.properties[propertyName] = value
                            TokenType.LONG -> commandArguments.properties[propertyName] = value
                            TokenType.STRING -> commandArguments.properties[propertyName] = value
                            TokenType.UNDERSCORE -> commandArguments.propertyExistsCheck.add(propertyName)
                            TokenType.KEYWORD -> {
                                assert(value.tokenContent == "true" || value.tokenContent == "false")
                                commandArguments.properties[propertyName] = value
                            }
                            else -> throw RuntimeException("Unexpected value after colon $value")
                        }
                    } else if (colonOrLink.tokenType == TokenType.ARROW && value.tokenType == TokenType.IDENTITY) {
                        commandArguments.link[propertyName] = value
                    } else if (colonOrLink.tokenType == TokenType.ARROW && value.tokenType == TokenType.UNDERSCORE) {
                        commandArguments.linkExistsCheck.add(propertyName)
                    } else if (colonOrLink.tokenType == TokenType.FAT_ARROW && value.tokenType == TokenType.IDENTITY) {
                        commandArguments.links[propertyName] = value
                    } else if (colonOrLink.tokenType == TokenType.FAT_ARROW && value.tokenType == TokenType.UNDERSCORE) {
                        commandArguments.linkExistsCheck.add(propertyName)
                    } else if (colonOrLink.tokenType == TokenType.FAT_ARROW && value.tokenType == TokenType.START_BRACKET) {
                        while (true) {
                            val identity = itr.next()
                            val commaOrEndBracket = itr.next()
                            assert(identity.tokenType == TokenType.IDENTITY)
                            assert(commaOrEndBracket.tokenType == TokenType.COMMA ||
                                    commaOrEndBracket.tokenType == TokenType.END_BRACKET)
                            commandArguments.links[propertyName] = identity
                            if (commaOrEndBracket.tokenType == TokenType.COMMA) {
                                continue
                            } else {
                                break
                            }
                        }
                    } else {
                        throw RuntimeException("Unexpected value types $colonOrLink & $value")
                    }
                    val braceOrComma = itr.next()
                    assert(braceOrComma.tokenType == TokenType.COMMA ||
                            braceOrComma.tokenType == TokenType.END_BRACE)
                }
                else -> {
                    return null
                }
            }
        }
        return commandArguments
    }

    private fun clearPropertiesAndLinks(entity: Entity) {
        entity.propertyNames.forEach {
            entity.deleteProperty(it)
        }
        entity.linkNames.forEach {
            entity.deleteLinks(it)
        }
    }

    private fun setPropertiesAndLinks(transaction: StoreTransaction, entity: Entity, commandArguments: CommandArguments) {
        commandArguments.properties.forEach { property ->
            when (property.value.tokenType) {
                TokenType.DOUBLE -> entity.setProperty(property.key, property.value.tokenContent.toDouble())
                TokenType.LONG -> entity.setProperty(property.key, property.value.tokenContent.trim('L').toLong())
                TokenType.INT -> entity.setProperty(property.key, property.value.tokenContent.toInt())
                TokenType.STRING -> entity.setProperty(property.key, property.value.tokenContent)
                TokenType.KEYWORD -> entity.setProperty(property.key, property.value.tokenContent.toBoolean())
                else -> {
                    throw RuntimeException("Property type must be string, char, boolean, int, long or double.")
                }
            }
        }
        commandArguments.link.forEach {
            val linkedEntity = transaction.getEntity(transaction.toEntityId(it.value.tokenContent))
            entity.setLink(it.key, linkedEntity)
        }
        commandArguments.links.forEach {
            val linkedEntity = transaction.getEntity(transaction.toEntityId(it.value.tokenContent))
            entity.addLink(it.key, linkedEntity)
        }
    }

    fun runNew(commandString: String): EntityId? {
        val itr: Iterator<StrollToken> = tokenize(commandString)
        val new = itr.next()
        assert(new.tokenType == TokenType.KEYWORD && new.tokenContent == "new")
        val entityType: String = itr.next().tokenContent

        val commandArguments = readCommandArguments(itr) ?: throw RuntimeException()

        return entityStore.computeInTransaction { transaction ->
            val entity = transaction.newEntity(entityType)
            setPropertiesAndLinks(transaction, entity, commandArguments)
            entity.id
        }
    }

    fun runUpdate(commandString: String) {
        val itr: Iterator<StrollToken> = tokenize(commandString)
        val update = itr.next()
        assert(update.tokenType == TokenType.KEYWORD && update.tokenContent == "update")
        val entityId: String = itr.next().tokenContent

        val commandArguments = readCommandArguments(itr) ?: throw RuntimeException()

        entityStore.executeInTransaction { transaction ->
            val entity = transaction.getEntity(transaction.toEntityId(entityId))
            setPropertiesAndLinks(transaction, entity, commandArguments)
        }
    }

    fun runSet(commandString: String) {
        val itr: Iterator<StrollToken> = tokenize(commandString)
        val set = itr.next()
        assert(set.tokenType == TokenType.KEYWORD && set.tokenContent == "update")
        val entityId: String = itr.next().tokenContent

        val commandArguments = readCommandArguments(itr) ?: throw RuntimeException()

        entityStore.executeInTransaction { transaction ->
            val entity = transaction.getEntity(transaction.toEntityId(entityId))
            clearPropertiesAndLinks(entity)
            setPropertiesAndLinks(transaction, entity, commandArguments)
        }
    }

    fun runDelete(commandString: String) {
        val itr: Iterator<StrollToken> = tokenize(commandString)
        val delete = itr.next()
        assert(delete.tokenType == TokenType.KEYWORD && delete.tokenContent == "delete")
        val startBracketOrIdentity = itr.next()

        entityStore.executeInTransaction { transaction ->
            when (startBracketOrIdentity.tokenType) {
                TokenType.START_BRACKET -> {
                    var next = itr.next()
                    while (next.tokenType == TokenType.IDENTITY) {
                        val entity = transaction.getEntity(transaction.toEntityId(next.tokenContent))
                        entity.delete()

                        next = itr.next()
                        if (next.tokenType == TokenType.COMMA) {
                            next = itr.next()
                        }
                    }
                    assert(next.tokenType == TokenType.END_BRACKET)
                }
                TokenType.IDENTITY -> {
                    val entity = transaction.getEntity(transaction.toEntityId(startBracketOrIdentity.tokenContent))
                    entity.delete()
                }
                else -> throw RuntimeException("Unexpected argument passed to delete $startBracketOrIdentity")
            }
        }
    }

    fun runFind(commandString: String): List<EntityId> {
        val itr: Iterator<StrollToken> = tokenize(commandString)
        val find = itr.next()
        assert(find.tokenType == TokenType.KEYWORD && find.tokenContent == "find")
        val entityType: String = itr.next().tokenContent

        val commandArguments = readCommandArguments(itr) ?: throw RuntimeException()

        if (commandArguments.link.isEmpty() &&
            commandArguments.links.isEmpty() &&
            commandArguments.linkExistsCheck.isEmpty() &&
            commandArguments.properties.isEmpty() &&
            commandArguments.propertyExistsCheck.isEmpty()) {
            return entityStore.computeInReadonlyTransaction {
                it.getAll(entityType).map {
                    it.id
                }
            }
        }

        val resultList: MutableList<EntityId> = mutableListOf()
        entityStore.executeInReadonlyTransaction { transaction ->
            commandArguments.properties.forEach {
                //TODO for properties
            }
            commandArguments.link.forEach {
                //TODO for single links
            }
            commandArguments.links.forEach {
                //TODO for multiple links
            }
            commandArguments.propertyExistsCheck.forEach {
                //TODO for property exists
            }
            commandArguments.linkExistsCheck.forEach {
                //TODO for link exists
            }
        }
        return resultList
    }
}
