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

import org.almibe.stroll.parser.ModalStrollLexer
import org.almibe.stroll.parser.Stroll
import org.almibe.stroll.parser.StrollListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.tree.TerminalNode

data class StrollScript(
        val lines: MutableList<Line> = mutableListOf()
)

data class Line (
        val assignmentVariableName: String?,
        val methodCallVariableName: String,
        val callName: String,
        val callContents: List<CallContent>
)

interface CallContent

enum class CallPunctutation: CallContent {
    COLON, COMMA, START_BRACKET, END_BRACKET, ARROW, FAT_ARROW
}

enum class ValueType {
    STRING, INT, LONG, DOUBLE, BOOLEAN, UNDERSCORE, IDENTITY, NAME, VARIABLE
}

data class CallValue(val type: ValueType, val value: String): CallContent

fun readScript(script: String): StrollScript {
    val stream = CharStreams.fromString(script)
    val lexer = ModalStrollLexer(stream)
    val tokens = CommonTokenStream(lexer)
    val parser = Stroll(tokens)
    val walker = ParseTreeWalker()
    val listener = StrollScriptListener()
    walker.walk(listener, parser.script())
    parser.script()
    return listener.currentScript
}

class StrollScriptListener : StrollListener {
    val currentScript: StrollScript = StrollScript()

    override fun enterCallPunctuation(p0: Stroll.CallPunctuationContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitLine(p0: Stroll.LineContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterCallContents(p0: Stroll.CallContentsContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitCallContents(p0: Stroll.CallContentsContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterScript(p0: Stroll.ScriptContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterCall(p0: Stroll.CallContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitCall(p0: Stroll.CallContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitCallValue(p0: Stroll.CallValueContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterEveryRule(ctx: ParserRuleContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterLine(p0: Stroll.LineContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitEveryRule(ctx: ParserRuleContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitScript(p0: Stroll.ScriptContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitCallPunctuation(p0: Stroll.CallPunctuationContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitErrorNode(node: ErrorNode?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitTerminal(node: TerminalNode?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterCallValue(p0: Stroll.CallValueContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
