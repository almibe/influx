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

class Tokenizer {
    fun tokenize(command: String): List<StrollToken> {
        val tokens = mutableListOf<StrollToken>()
        val itr = command.iterator()

        if (itr.hasNext()) {
            startNextToken(itr.nextChar(), itr, tokens)
        }

        return tokens.toList()
    }

    private fun startNextToken(currentChar: Char, itr: Iterator<Char>, tokens: MutableList<StrollToken>) {
        when (currentChar) {
            //single character checks
            ':' -> tokens.add(StrollToken(TokenType.COLON, ":"))
            '{' -> tokens.add(StrollToken(TokenType.START_BRACE, "{"))
            '}' -> tokens.add(StrollToken(TokenType.END_BRACE, "}"))
            '[' -> tokens.add(StrollToken(TokenType.START_BRACKET, "["))
            ']' -> tokens.add(StrollToken(TokenType.END_BRACKET, "]"))
            ',' -> tokens.add(StrollToken(TokenType.COMMA, ","))
            '_' -> tokens.add(StrollToken(TokenType.UNDERSCORE, "_"))
            //multi character checks
            in 'a'..'z' -> checkKeyword(currentChar, itr, tokens)
            in 'A'..'Z' -> checkKeyword(currentChar, itr, tokens)
            '"' -> checkString(itr, tokens)
            in '0'..'9' -> checkNumberOrIdentity(currentChar, itr, tokens)
            '-', '=' -> checkArrow(currentChar, itr, tokens)
        }
        if (itr.hasNext()) {
            startNextToken(itr.next(), itr, tokens)
        }
    }

    private fun checkKeyword(firstChar: Char, itr: Iterator<Char>, tokens: MutableList<StrollToken>) {
        val keyword = StringBuilder(firstChar.toString())
        if (!itr.hasNext()) {
            tokens.add(StrollToken(TokenType.KEYWORD, keyword.toString()))
            return
        }
        var currentChar: Char? = itr.next()
        while (currentChar in 'a'..'z' || currentChar in 'A'..'Z' || currentChar in '0'..'9') {
            keyword.append(currentChar)
            if (itr.hasNext()) {
                currentChar = itr.next()
            } else {
                currentChar = null
                break
            }
        }

        when {
            keyword.matches(Regex("[a-zA-Z0-9]+")) -> tokens.add(StrollToken(TokenType.KEYWORD, keyword.toString()))
            else -> throw RuntimeException("Keyword or Identity incorrectly formed $keyword.")
        }

        if (currentChar != null) {
            startNextToken(currentChar, itr, tokens)
        }
    }

    private fun checkString(itr: Iterator<Char>, tokens: MutableList<StrollToken>) {
        val keyword = StringBuilder()
        var currentChar: Char? = null
        while (itr.hasNext()) {
             currentChar = itr.next()
            if (currentChar == '"') {
                break
            } else if(currentChar == '\\') {
                TODO("handle escape char")
            } else {
                keyword.append(currentChar)
            }
        }
        if (currentChar == '"') {
            tokens.add(StrollToken(TokenType.STRING, keyword.toString()))
            if (itr.hasNext()) {
                startNextToken(itr.next(), itr, tokens)
            }
        } else {
            throw RuntimeException("String not closed.")
        }
    }

    private fun checkNumberOrIdentity(firstChar: Char, itr: Iterator<Char>, tokens: MutableList<StrollToken>) {
        val number = StringBuilder(firstChar.toString())

        if (!itr.hasNext()) {
            tokens.add(StrollToken(TokenType.INT, number.toString()))
            return
        }
        var currentChar: Char? = itr.next()
        while (currentChar in '0'..'9' || currentChar == '.' || currentChar == '-' || currentChar == 'L') {
            number.append(currentChar)
            if (itr.hasNext()) {
                currentChar = itr.next()
            } else {
                currentChar = null
                break
            }
        }
        when {
            number.matches(Regex("[0-9]+")) -> tokens.add(StrollToken(TokenType.INT, number.toString()))
            number.matches(Regex("[0-9]+L")) -> tokens.add(StrollToken(TokenType.LONG, number.toString()))
            number.matches(Regex("[0-9]+\\.[0-9]+")) -> tokens.add(StrollToken(TokenType.DOUBLE, number.toString()))
            number.matches(Regex("[0-9]+-[0-9]+")) -> tokens.add(StrollToken(TokenType.IDENTITY, number.toString()))
            else -> throw RuntimeException("Number incorrectly formed $number.")
        }

        if (currentChar != null) {
            startNextToken(currentChar, itr, tokens)
        }
    }

    private fun checkArrow(firstChar: Char, itr: Iterator<Char>, tokens: MutableList<StrollToken>) {
        if (itr.hasNext() && itr.next() == '>') {
            if (firstChar == '-') {
                tokens.add(StrollToken(TokenType.ARROW, "->"))
            }
            if (firstChar == '=') {
                tokens.add(StrollToken(TokenType.FAT_ARROW, "=>"))
            }
        } else {
            throw RuntimeException("Incorrectly formed arrow.")
        }
        if (itr.hasNext()) {
            startNextToken(itr.next(), itr, tokens)
        }
    }
}
