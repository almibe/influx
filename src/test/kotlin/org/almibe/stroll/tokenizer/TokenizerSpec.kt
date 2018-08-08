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

package org.almibe.stroll.tokenizer

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TokenizerSpec : StringSpec({
    val tokenizer = Tokenizer()

    "comma test" {
        val command = ","
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.COMMA, ",")
        tokens.size shouldBe 1
    }

    "keyword test" {
        val command = "find"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.KEYWORD, "find")
        tokens.size shouldBe 1
    }

    "string test" {
        val command = "\"string\""
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.STRING, "string")
        tokens.size shouldBe 1
    }

    "int test" {
        val command = "6"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.INT, "6")
        tokens.size shouldBe 1
    }

    "double digit int test" {
        val command = "42"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.INT, "42")
        tokens.size shouldBe 1
    }

    "long test" {
        val command = "6L"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.LONG, "6L")
        tokens.size shouldBe 1
    }

    "double digit long test" {
        val command = "42L"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.LONG, "42L")
        tokens.size shouldBe 1
    }

    "zero double test" {
        val command = "0.0"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.DOUBLE, "0.0")
        tokens.size shouldBe 1
    }

    "double test" {
        val command = "42.420"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.DOUBLE, "42.420")
        tokens.size shouldBe 1
    }

    "arrow test" {
        val command = " -> "
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.ARROW, "->")
        tokens.size shouldBe 1
    }

    "fat arrow test" {
        val command = " => "
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.FAT_ARROW, "=>")
        tokens.size shouldBe 1
    }

    "punctuation test" {
        val command = ":_->=>,][{}"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.COLON, ":")
        tokens[1] shouldBe StrollToken(TokenType.UNDERSCORE, "_")
        tokens[2] shouldBe StrollToken(TokenType.ARROW, "->")
        tokens[3] shouldBe StrollToken(TokenType.FAT_ARROW, "=>")
        tokens[4] shouldBe StrollToken(TokenType.COMMA, ",")
        tokens[5] shouldBe StrollToken(TokenType.END_BRACKET, "]")
        tokens[6] shouldBe StrollToken(TokenType.START_BRACKET, "[")
        tokens[7] shouldBe StrollToken(TokenType.START_BRACE, "{")
        tokens[8] shouldBe StrollToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 9
    }

    "punctuation with spaces test" {
        val command = " : -> => , _ { ] } [ "
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.COLON, ":")
        tokens[1] shouldBe StrollToken(TokenType.ARROW, "->")
        tokens[2] shouldBe StrollToken(TokenType.FAT_ARROW, "=>")
        tokens[3] shouldBe StrollToken(TokenType.COMMA, ",")
        tokens[4] shouldBe StrollToken(TokenType.UNDERSCORE, "_")
        tokens[5] shouldBe StrollToken(TokenType.START_BRACE, "{")
        tokens[6] shouldBe StrollToken(TokenType.END_BRACKET, "]")
        tokens[7] shouldBe StrollToken(TokenType.END_BRACE, "}")
        tokens[8] shouldBe StrollToken(TokenType.START_BRACKET, "[")
        tokens.size shouldBe 9
    }

    "colon assignment without spaces" {
        val command = "{name:\"Bob\"}"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.START_BRACE, "{")
        tokens[1] shouldBe StrollToken(TokenType.KEYWORD, "name")
        tokens[2] shouldBe StrollToken(TokenType.COLON, ":")
        tokens[3] shouldBe StrollToken(TokenType.STRING, "Bob")
        tokens[4] shouldBe StrollToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 5
    }

    "colon assignment with spaces" {
        val command = " { age : 42 }"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.START_BRACE, "{")
        tokens[1] shouldBe StrollToken(TokenType.KEYWORD, "age")
        tokens[2] shouldBe StrollToken(TokenType.COLON, ":")
        tokens[3] shouldBe StrollToken(TokenType.INT, "42")
        tokens[4] shouldBe StrollToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 5
    }

    "arrow assignment without spaces" {
        val command = "{company->nintendo}"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.START_BRACE, "{")
        tokens[1] shouldBe StrollToken(TokenType.KEYWORD, "company")
        tokens[2] shouldBe StrollToken(TokenType.ARROW, "->")
        tokens[3] shouldBe StrollToken(TokenType.KEYWORD, "nintendo")
        tokens[4] shouldBe StrollToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 5
    }

    "fat arrow assignment with spaces" {
        val command = "  { company => 3-42  }    "
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.START_BRACE, "{")
        tokens[1] shouldBe StrollToken(TokenType.KEYWORD, "company")
        tokens[2] shouldBe StrollToken(TokenType.FAT_ARROW, "=>")
        tokens[3] shouldBe StrollToken(TokenType.IDENTITY, "3-42")
        tokens[4] shouldBe StrollToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 5
    }

    "new command test" {
        val command = "new User { name: \"Bob\", age:42 } "
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.KEYWORD, "new")
        tokens[1] shouldBe StrollToken(TokenType.KEYWORD, "User")
        tokens[2] shouldBe StrollToken(TokenType.START_BRACE, "{")
        tokens[3] shouldBe StrollToken(TokenType.KEYWORD, "name")
        tokens[4] shouldBe StrollToken(TokenType.COLON, ":")
        tokens[5] shouldBe StrollToken(TokenType.STRING, "Bob")
        tokens[6] shouldBe StrollToken(TokenType.COMMA, ",")
        tokens[7] shouldBe StrollToken(TokenType.KEYWORD, "age")
        tokens[8] shouldBe StrollToken(TokenType.COLON, ":")
        tokens[9] shouldBe StrollToken(TokenType.INT, "42")
        tokens[10] shouldBe StrollToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 11
    }

    "identity test" {
        val command = "0-76"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.IDENTITY, "0-76")
        tokens.size shouldBe 1
    }

    "arrow to identity test" {
        val command = "user->0-76"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.KEYWORD, "user")
        tokens[1] shouldBe StrollToken(TokenType.ARROW, "->")
        tokens[2] shouldBe StrollToken(TokenType.IDENTITY, "0-76")
        tokens.size shouldBe 3
    }

    "fat arrow to identity test" {
        val command = "  {  links => [0-76, 45-45]}"
        val tokens = tokenizer.tokenize(command)
        tokens[0] shouldBe StrollToken(TokenType.START_BRACE, "{")
        tokens[1] shouldBe StrollToken(TokenType.KEYWORD, "links")
        tokens[2] shouldBe StrollToken(TokenType.FAT_ARROW, "=>")
        tokens[3] shouldBe StrollToken(TokenType.START_BRACKET, "[")
        tokens[4] shouldBe StrollToken(TokenType.IDENTITY, "0-76")
        tokens[5] shouldBe StrollToken(TokenType.COMMA, ",")
        tokens[6] shouldBe StrollToken(TokenType.IDENTITY, "45-45")
        tokens[7] shouldBe StrollToken(TokenType.END_BRACKET, "]")
        tokens[8] shouldBe StrollToken(TokenType.END_BRACE, "}")
        tokens.size shouldBe 9
    }
})
