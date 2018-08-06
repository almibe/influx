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

import jetbrains.exodus.entitystore.EntityId
import jetbrains.exodus.entitystore.EntityIterable
import jetbrains.exodus.entitystore.PersistentEntityStore
import org.almibe.stroll.tokenizer.StrollToken
import org.almibe.stroll.tokenizer.TokenType
import org.almibe.stroll.tokenizer.Tokenizer

class Stroll(private val entityStore: PersistentEntityStore) {
    private val tokenizer = Tokenizer()

    private fun tokenize(command: String): Iterator<StrollToken> = tokenizer.tokenize(command).iterator()

    fun runNew(commandString: String): EntityId? {
        val itr: Iterator<StrollToken> = tokenize(commandString)
        //new User {}
        //new User { username: "Josh"}
        //new User { username: "Josh", id: 34, age: 56}
        val new = itr.next()
        assert(new.tokenType == TokenType.KEYWORD && new.tokenContent == "new")
        val entityType: String = itr.next().tokenContent
        val newCommand = NewCommand(entityType)

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
                    val colon = itr.next()
                    val value = itr.next()
                    assert(colon.tokenType == TokenType.COLON)
                    assert(value.tokenType == TokenType.NUMBER ||
                            value.tokenType == TokenType.STRING)
                    newCommand.properties.put(propertyName, value)
                    val braceOrComma = itr.next()
                    assert(braceOrComma.tokenType == TokenType.COMMA ||
                           braceOrComma.tokenType == TokenType.END_BRACE)
                }
                else -> {
                    return null
                }
            }
        }

        return entityStore.computeInTransaction { trans ->
            val entity = trans.newEntity(newCommand.entityType)
            newCommand.properties.forEach {
                when (it.value.tokenType) {
                    TokenType.NUMBER -> {}
                    TokenType.STRING -> {}
                    else -> {
                        trans.abort()
                    }
                }
            }
            entity.id
        }
    }

    fun runFind(commandString: String): EntityIterable? {
        val itr: Iterator<StrollToken> = tokenize(commandString)
        TODO()
    }

    fun handleDelete(commandString: String): EntityIterable? {
        val itr: Iterator<StrollToken> = tokenize(commandString)
        TODO()
    }
}
