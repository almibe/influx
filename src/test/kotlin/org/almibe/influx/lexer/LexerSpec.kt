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

package org.almibe.influx.lexer

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class LexerSpec : StringSpec({
    "comma test" {
        val command = ","
        val lexer = Lexer(command)
        lexer.nextToken() shouldBe InfluxToken(TokenType.COMMA, ",")
        lexer.nextToken() shouldBe null
    }

    "keyword test" {
        val command = "find"
        val lexer = Lexer(command)
        lexer.nextToken() shouldBe InfluxToken(TokenType.KEYWORD, "find")
        lexer.nextToken() shouldBe null
    }

    "string test" {
        val command = "\"string\""
        val lexer = Lexer(command)
        lexer.nextToken() shouldBe InfluxToken(TokenType.STRING, "string")
        lexer.nextToken() shouldBe null
    }

    "punctuation test" {
        val command = ":->=>,{}"
        val lexer = Lexer(command)
        lexer.nextToken() shouldBe InfluxToken(TokenType.COLON, ":")
        lexer.nextToken() shouldBe InfluxToken(TokenType.ARROW, "->")
        lexer.nextToken() shouldBe InfluxToken(TokenType.FAT_ARROW, "=>")
        lexer.nextToken() shouldBe InfluxToken(TokenType.COMMA, ",")
        lexer.nextToken() shouldBe InfluxToken(TokenType.START_BRACE, "{")
        lexer.nextToken() shouldBe InfluxToken(TokenType.END_BRACE, "}")
        lexer.nextToken() shouldBe null
    }

    "new command test" {
        val command = "new User { name: \"Bob\", age:42 } "
        val lexer = Lexer(command)
        lexer.nextToken() shouldBe InfluxToken(TokenType.KEYWORD, "new")
        lexer.nextToken() shouldBe InfluxToken(TokenType.KEYWORD, "User")
        lexer.nextToken() shouldBe InfluxToken(TokenType.START_BRACE, "{")
        lexer.nextToken() shouldBe InfluxToken(TokenType.KEYWORD, "name")
        lexer.nextToken() shouldBe InfluxToken(TokenType.COLON, ":")
        lexer.nextToken() shouldBe InfluxToken(TokenType.STRING, "Bob")
        lexer.nextToken() shouldBe InfluxToken(TokenType.COMMA, ",")
        lexer.nextToken() shouldBe InfluxToken(TokenType.KEYWORD, "age")
        lexer.nextToken() shouldBe InfluxToken(TokenType.COLON, ":")
        lexer.nextToken() shouldBe InfluxToken(TokenType.NUMBER, "42")
        lexer.nextToken() shouldBe InfluxToken(TokenType.END_BRACE, "}")
        lexer.nextToken() shouldBe null
    }
})
