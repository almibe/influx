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

import jetbrains.exodus.entitystore.StoreTransaction
import org.almibe.stroll.loader.CommandArguments
import org.almibe.stroll.loader.StrollCommandListener
import org.almibe.stroll.parser.ModalStrollLexer
import org.almibe.stroll.parser.Stroll
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

enum class CommandType {
    NEW,
    UPDATE,
    SET,
    DELETE,
    FIND
}

data class StrollResult(val commandType: CommandType)

class Stroll {
    fun runCommand(command: String, transaction: StoreTransaction): StrollResult {
        val commandArguments = readCommand(command)
        TODO()
    }

    fun readCommand(command: String): CommandArguments {
        val stream = CharStreams.fromString(command)
        val lexer = ModalStrollLexer(stream)
        val tokens = CommonTokenStream(lexer)
        val parser = Stroll(tokens)
        val walker = ParseTreeWalker()
        val listener = StrollCommandListener()
        walker.walk(listener, parser.command())
        parser.command()
        return listener.model
    }
}
