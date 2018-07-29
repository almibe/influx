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

class Tokenizer {
    fun tokenize(command: String): List<InfluxToken> {
        val tokens = mutableListOf<InfluxToken>()
        val itr = command.iterator()

        if (itr.hasNext()) {
            startNextToken(itr.nextChar(), itr, tokens)
        }

        return tokens.toList()
    }

    private fun startNextToken(currentChar: Char, itr: Iterator<Char>, tokens: MutableList<InfluxToken>) {
        when (currentChar) {
            //single character checks
            ':' -> tokens.add(InfluxToken(TokenType.COLON, ":"))
            '{' -> tokens.add(InfluxToken(TokenType.START_BRACE, "{"))
            '}' -> tokens.add(InfluxToken(TokenType.END_BRACE, "}"))
            ',' -> tokens.add(InfluxToken(TokenType.COMMA, ","))
            //multi character checks
            in 'a'..'z' -> checkKeyword(currentChar, itr, tokens)
            in 'A'..'Z' -> checkKeyword(currentChar, itr, tokens)
            '"' -> checkString(itr, tokens)
            in '0'..'9' -> checkNumber(currentChar, itr, tokens)
            '-', '=' -> checkArrow(currentChar, itr, tokens)
        }
        if (itr.hasNext()) {
            startNextToken(itr.next(), itr, tokens)
        }
    }

    private fun checkKeyword(firstChar: Char, itr: Iterator<Char>, tokens: MutableList<InfluxToken>) {
        val keyword = StringBuilder(firstChar.toString())

        if (!itr.hasNext()) {
            tokens.add(InfluxToken(TokenType.KEYWORD, keyword.toString()))
            return
        }

        var currentChar = itr.next()
        //TODO if I add identity as a token type also check for # here
        //TODO and then use regex checks to see what the final type is similar to what I did with numbers
        while (currentChar in 'a'..'z' || currentChar in 'A'..'Z' || currentChar in '0'..'9') {
            keyword.append(currentChar)
            if (itr.hasNext()) {
                currentChar = itr.next()
            } else {
                tokens.add(InfluxToken(TokenType.KEYWORD, keyword.toString()))
                return
            }
        }
        tokens.add(InfluxToken(TokenType.KEYWORD, keyword.toString()))
        startNextToken(currentChar, itr, tokens)
    }

    private fun checkString(itr: Iterator<Char>, tokens: MutableList<InfluxToken>) {
        val keyword = StringBuilder()
        var currentChar: Char? = null
        while (itr.hasNext()) {
             currentChar = itr.next()
            if (currentChar == '"') {
                break
            } else if(currentChar == '\\') {
                //TODO handle escape char
            } else {
                keyword.append(currentChar)
            }
        }
        if (currentChar == '"') {
            tokens.add(InfluxToken(TokenType.STRING, keyword.toString()))
            if (itr.hasNext()) {
                startNextToken(itr.next(), itr, tokens)
            }
        } else {
            throw RuntimeException("String not closed.")
        }
    }

    private fun checkNumber(firstChar: Char, itr: Iterator<Char>, tokens: MutableList<InfluxToken>) {
        val number = StringBuilder(firstChar.toString())

        if (!itr.hasNext()) {
            tokens.add(InfluxToken(TokenType.NUMBER, number.toString()))
        } else {
            var currentChar = itr.next()
            while (currentChar in '0'..'9' || currentChar == '.') {
                number.append(currentChar)
                if (itr.hasNext()) {
                    currentChar = itr.next()
                } else {
                    break
                }
            }
            when {
                number.matches(Regex("[0-9]+")) -> tokens.add(InfluxToken(TokenType.NUMBER, number.toString()))
                number.matches(Regex("[0-9]+\\.[0-9]+")) -> tokens.add(InfluxToken(TokenType.NUMBER, number.toString()))
                else -> throw RuntimeException("Number incorrectly formed $number.")
            }
        }
    }

    private fun checkArrow(firstChar: Char, itr: Iterator<Char>, tokens: MutableList<InfluxToken>) {
        if (itr.hasNext() && itr.next() == '>') {
            if (firstChar == '-') {
                tokens.add(InfluxToken(TokenType.ARROW, "->"))
            }
            if (firstChar == '=') {
                tokens.add(InfluxToken(TokenType.FAT_ARROW, "=>"))
            }
        } else {
            throw RuntimeException("Incorrectly formed arrow.")
        }
        if (itr.hasNext()) {
            startNextToken(itr.next(), itr, tokens)
        }
    }
}
