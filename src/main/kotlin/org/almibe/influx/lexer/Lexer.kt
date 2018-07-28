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

class Lexer(val command: String) {
    private var currentCharPos = 0
    private var currentChar: Char? = null
    private val length = command.length

    fun nextToken(): InfluxToken? {
        return if (currentCharPos < length) {
            currentChar = command[currentCharPos]
            currentCharPos++
            when (currentChar) {
                //single character checks
                ':' -> InfluxToken(TokenType.COLON, ":")
                '{' -> InfluxToken(TokenType.START_BRACE, "{")
                '}' -> InfluxToken(TokenType.END_BRACE, "}")
                ',' -> InfluxToken(TokenType.COMMA, ",")
                //multi character checks
                '"' -> checkString()
                in '0'..'9' -> checkNumber()
                in 'a'..'z' -> checkKeyword()
                in 'A'..'Z' -> checkKeyword()
                '-', '='-> checkArrow()
                else -> null
            }
        } else {
            null
        }
    }

    fun checkString(): InfluxToken? {
        return null
    }

    fun checkNumber(): InfluxToken? {
        return null
    }

    fun checkKeyword(): InfluxToken? {
        return null
    }

    fun checkArrow(): InfluxToken? {
        return null
    }
}
