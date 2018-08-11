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

package org.almibe.stroll.loader

import org.almibe.stroll.CommandType
import org.almibe.stroll.parser.ModalStrollLexer
import org.almibe.stroll.parser.Stroll
import org.almibe.stroll.parser.StrollBaseListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

data class Property(val type: String, val value: String)
data class CommandArguments (
        var commandType: CommandType? = null,
        var entityName: String = "",
        //three collections used by all commands
        val properties: MutableMap<String, Property> = mutableMapOf(),
        val link: MutableMap<String, String> = mutableMapOf(),
        val links: MutableList<Pair<String, String>> = mutableListOf(),
        //collections only used by find
        val propertyExistsCheck: MutableList<String> = mutableListOf(),
        val linkExistsCheck: MutableList<String> = mutableListOf()
        //TODO eventually this will contain range and startsWith info
)

fun readCommand(command: String): CommandArguments {
    val stream = CharStreams.fromString(command)
    val lexer = ModalStrollLexer(stream)
    val tokens = CommonTokenStream(lexer)
    val parser = Stroll(tokens)
    val walker = ParseTreeWalker()
    val listener = StrollCommandListener()
    walker.walk(listener, parser.command())
    parser.command()
    return listener.currentCommand
}

class StrollCommandListener : StrollBaseListener() {
    val currentCommand = CommandArguments()


}
