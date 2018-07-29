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

package org.almibe.influx.tokenizer

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TokenizerSpec : StringSpec({
    val tokenizer = Tokenizer()

    "comma test" {
        val command = ","
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe InfluxToken(TokenType.COMMA, ",")
        tokens.size shouldBe 1
    }

    "keyword test" {
        val command = "find"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe InfluxToken(TokenType.KEYWORD, "find")
        tokens.size shouldBe 1
    }

    "string test" {
        val command = "\"string\""
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe InfluxToken(TokenType.STRING, "string")
        tokens.size shouldBe 1
    }

    "int test" {
        val command = "42"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe InfluxToken(TokenType.NUMBER, "42")
        tokens.size shouldBe 1
    }

    "float test" {
        val command = "42.0"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe InfluxToken(TokenType.NUMBER, "42.0")
        tokens.size shouldBe 1
    }

    "punctuation test" {
        val command = ":->=>,{}"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe InfluxToken(TokenType.COLON, ":")
        tokens[1] shouldBe InfluxToken(TokenType.ARROW, "->")
        tokens[2] shouldBe InfluxToken(TokenType.FAT_ARROW, "=>")
        tokens[3] shouldBe InfluxToken(TokenType.COMMA, ",")
        tokens[4] shouldBe InfluxToken(TokenType.START_BRACE, "{")
        tokens[5] shouldBe InfluxToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 6
    }

    "punctuation with spaces test" {
        val command = " : -> => , {  } "
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe InfluxToken(TokenType.COLON, ":")
        tokens[1] shouldBe InfluxToken(TokenType.ARROW, "->")
        tokens[2] shouldBe InfluxToken(TokenType.FAT_ARROW, "=>")
        tokens[3] shouldBe InfluxToken(TokenType.COMMA, ",")
        tokens[4] shouldBe InfluxToken(TokenType.START_BRACE, "{")
        tokens[5] shouldBe InfluxToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 6
    }

    "new command test" {
        val command = "new User { name: \"Bob\", age:42 } "
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe InfluxToken(TokenType.KEYWORD, "new")
        tokens[1] shouldBe InfluxToken(TokenType.KEYWORD, "User")
        tokens[2] shouldBe InfluxToken(TokenType.START_BRACE, "{")
        tokens[3] shouldBe InfluxToken(TokenType.KEYWORD, "name")
        tokens[4] shouldBe InfluxToken(TokenType.COLON, ":")
        tokens[5] shouldBe InfluxToken(TokenType.STRING, "Bob")
        tokens[6] shouldBe InfluxToken(TokenType.COMMA, ",")
        tokens[7] shouldBe InfluxToken(TokenType.KEYWORD, "age")
        tokens[8] shouldBe InfluxToken(TokenType.COLON, ":")
        tokens[9] shouldBe InfluxToken(TokenType.NUMBER, "42")
        tokens[10] shouldBe InfluxToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 11
    }
})
