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
import org.almibe.stroll.parser.StrollListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.tree.TerminalNode

data class Property(val type: String, val value: String)
data class CommandArguments (
        var commandType: CommandType? = null,
        var entityType: String = "", //used by new
        var entityId: String = "", //used by set update
        val entityIds: MutableList<String> = mutableListOf(), //used by delete
        var commandName: String = "", //used by simple command
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

class StrollCommandListener : StrollListener {
    val currentCommand = CommandArguments()

    override fun exitNewCommand(context: Stroll.NewCommandContext) {
        currentCommand.commandType = CommandType.NEW
        currentCommand.entityType = context.NAME().text
    }

    override fun exitUpdateCommand(context: Stroll.UpdateCommandContext) {
        currentCommand.commandType = CommandType.UPDATE
        currentCommand.entityId = context.IDENTITY().text
    }

    override fun exitSetCommand(context: Stroll.SetCommandContext) {
        currentCommand.commandType = CommandType.SET
        currentCommand.entityId = context.IDENTITY().text
    }

    override fun exitDeleteCommand(context: Stroll.DeleteCommandContext) {
        currentCommand.commandType = CommandType.DELETE
        context.IDENTITY().forEach { node ->
            currentCommand.entityIds.add(node.text)
        }
    }

    override fun exitPropertyAssignment(context: Stroll.PropertyAssignmentContext) {
        val name = context.NAME().text
        val property = createProperty(context.propertyValue())
        currentCommand.properties[name] = property
    }

    private fun createProperty(context: Stroll.PropertyValueContext): Property {
        val type = getType(context)
        val value = getValue(context)
        return Property(type, value)
    }

    private fun getType(context: Stroll.PropertyValueContext): String = when {
        context.BOOLEAN() != null -> "Boolean"
        context.STRING() != null -> "String"
        context.LONG() != null -> "Long"
        context.DOUBLE() != null -> "Double"
        context.INT() != null -> "Int"
        else -> "Other"
    }

    private fun getValue(context: Stroll.PropertyValueContext): String = when {
        context.BOOLEAN() != null -> context.text
        context.STRING() != null -> context.text.substring(1, context.text.length-1)
        context.LONG() != null -> context.text
        context.DOUBLE() != null -> context.text
        context.INT() != null -> context.text
        else -> context.text
    }

    override fun exitLinkAssignment(context: Stroll.LinkAssignmentContext) {
        val name = context.NAME().text
        val linkTo = context.IDENTITY().text
        if (context.ARROW() != null) {
            currentCommand.link[name] = linkTo
        } else {
            currentCommand.links.add(Pair(name, linkTo))
        }
    }

    override fun exitLinksListAssigment(context: Stroll.LinksListAssigmentContext) {
        val name = context.NAME().text
        val linkTo = context.IDENTITY().map { node ->
            Pair(name, node.text)
        }
        currentCommand.links.addAll(linkTo)
    }

    override fun exitFindCommand(p0: Stroll.FindCommandContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun exitSimpleCommand(p0: Stroll.SimpleCommandContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitPropertyStartsWith(p0: Stroll.PropertyStartsWithContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitLinkExists(p0: Stroll.LinkExistsContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitPropertyValue(p0: Stroll.PropertyValueContext?) {

    }

    override fun exitPropertyRange(p0: Stroll.PropertyRangeContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitFindParameter(p0: Stroll.FindParameterContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitPropertyExists(p0: Stroll.PropertyExistsContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitErrorNode(node: ErrorNode) {
        throw RuntimeException("In error node. $node")
    }

    override fun enterLinksListAssigment(p0: Stroll.LinksListAssigmentContext?) {}
    override fun exitCommand(p0: Stroll.CommandContext?) {}
    override fun enterSetCommand(p0: Stroll.SetCommandContext?) {}
    override fun enterPropertyAssignment(p0: Stroll.PropertyAssignmentContext?) {}
    override fun enterFindCommand(p0: Stroll.FindCommandContext?) {}
    override fun enterPropertyRange(p0: Stroll.PropertyRangeContext?) {}
    override fun enterLinkAssignment(p0: Stroll.LinkAssignmentContext?) {}
    override fun enterFindParameter(p0: Stroll.FindParameterContext?) {}
    override fun enterDeleteCommand(p0: Stroll.DeleteCommandContext?) {}
    override fun enterSimpleCommand(p0: Stroll.SimpleCommandContext?) {}
    override fun enterPropertyExists(p0: Stroll.PropertyExistsContext?) {}
    override fun enterLinkExists(p0: Stroll.LinkExistsContext?) {}
    override fun enterPropertyStartsWith(p0: Stroll.PropertyStartsWithContext?) {}
    override fun enterUpdateCommand(p0: Stroll.UpdateCommandContext?) {}
    override fun enterPropertyValue(p0: Stroll.PropertyValueContext?) {}
    override fun visitTerminal(node: TerminalNode?) {}
    override fun enterEveryRule(ctx: ParserRuleContext) {}
    override fun exitEveryRule(ctx: ParserRuleContext) {}
    override fun enterPropertyOrLinkAssignments(context: Stroll.PropertyOrLinkAssignmentsContext) {}
    override fun enterCommand(context: Stroll.CommandContext) {}
    override fun enterNewCommand(context: Stroll.NewCommandContext) {}
    override fun enterPropertyOrLinkAssignment(context: Stroll.PropertyOrLinkAssignmentContext) {}
    override fun exitPropertyOrLinkAssignment(context: Stroll.PropertyOrLinkAssignmentContext) {}
    override fun exitPropertyOrLinkAssignments(p0: Stroll.PropertyOrLinkAssignmentsContext?) {}
}
