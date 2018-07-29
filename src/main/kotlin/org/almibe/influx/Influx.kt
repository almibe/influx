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

import jetbrains.exodus.entitystore.EntityIterable
import jetbrains.exodus.entitystore.PersistentEntityStore
import org.almibe.influx.tokenizer.TokenType
import org.almibe.influx.tokenizer.Tokenizer

class Influx(private val entityStore: PersistentEntityStore) {
    private val tokenizer = Tokenizer()

    fun run(command: String): EntityIterable? {
        val itr = tokenizer.tokenize(command).iterator()

        if (!itr.hasNext()) {
            return null
        }
        val firstToken = itr.next()
        when {
            firstToken.tokenType != TokenType.KEYWORD -> return null
            firstToken.tokenContent == "new" -> {

            }
            firstToken.tokenContent == "find" -> {

            }
            else -> return null
        }


        return null
    }
}
