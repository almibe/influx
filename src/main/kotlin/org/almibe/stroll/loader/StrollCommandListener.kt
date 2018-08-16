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

data class StrollScript(
        val expression: List<Expression>
)
interface ExpressionArgument

data class Expression (
        val assignmentVariableName: String?,
        val methodCallVariableName: String?,
        val expressionName: String,
        val arguments: List<ExpressionArgument>,
        val chained: Boolean
): ExpressionArgument

data class EntityPattern(
        val properties: MutableMap<String, Property> = mutableMapOf(),
        val link: MutableMap<String, String> = mutableMapOf(),
        val links: MutableList<Pair<String, String>> = mutableListOf())

data class Keyword(val keyword: String): ExpressionArgument

data class PropertyValue(val value: String): ExpressionArgument

data class Identitiy(val identity: String): ExpressionArgument

fun readCommand(command: String): CommandArguments {
    val stream = CharStreams.fromString(command)
    val lexer = ModalStrollLexer(stream)
    val tokens = CommonTokenStream(lexer)
    val parser = Stroll(tokens)
    val walker = ParseTreeWalker()
    val listener = StrollCommandListener()
    walker.walk(listener, parser.script())
    parser.script()
    return listener.currentCommand
}

class StrollCommandListener : StrollListener {
    override fun exitMethodCall(p0: Stroll.MethodCallContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitExpressionArguements(p0: Stroll.ExpressionArguementsContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterScript(p0: Stroll.ScriptContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterEntityPattern(p0: Stroll.EntityPatternContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterAssignedExpression(p0: Stroll.AssignedExpressionContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterExpressionArguements(p0: Stroll.ExpressionArguementsContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitAssignedExpression(p0: Stroll.AssignedExpressionContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitEntityPattern(p0: Stroll.EntityPatternContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterMethodCall(p0: Stroll.MethodCallContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val currentCommand = CommandArguments()

    override fun exitExpression(p0: Stroll.ExpressionContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun visitErrorNode(node: ErrorNode) {
        throw RuntimeException("In error node. $node")
    }

    override fun enterExpression(p0: Stroll.ExpressionContext?) {}
    override fun exitPropertyValue(p0: Stroll.PropertyValueContext?) {}
    override fun enterLinksListAssigment(p0: Stroll.LinksListAssigmentContext?) {}
    override fun enterPropertyAssignment(p0: Stroll.PropertyAssignmentContext?) {}
    override fun enterLinkAssignment(p0: Stroll.LinkAssignmentContext?) {}
    override fun enterPropertyValue(p0: Stroll.PropertyValueContext?) {}
    override fun visitTerminal(node: TerminalNode?) {}
    override fun enterEveryRule(ctx: ParserRuleContext) {}
    override fun exitEveryRule(ctx: ParserRuleContext) {}
    override fun enterPropertyOrLinkAssignment(context: Stroll.PropertyOrLinkAssignmentContext) {}
    override fun exitPropertyOrLinkAssignment(context: Stroll.PropertyOrLinkAssignmentContext) {}
    override fun exitScript(p0: Stroll.ScriptContext?) {}
}
