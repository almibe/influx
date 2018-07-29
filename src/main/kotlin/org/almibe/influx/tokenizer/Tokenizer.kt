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
        while(itr.hasNext()) {
            val currentChar = itr.nextChar()
            val token = when (currentChar) {
                //single character checks
                ':' -> InfluxToken(TokenType.COLON, ":")
                '{' -> InfluxToken(TokenType.START_BRACE, "{")
                '}' -> InfluxToken(TokenType.END_BRACE, "}")
                ',' -> InfluxToken(TokenType.COMMA, ",")
                //multi character checks
                '"' -> checkString(itr)
                in '0'..'9' -> checkNumber(itr)
                in 'a'..'z' -> checkKeyword(itr)
                in 'A'..'Z' -> checkKeyword(itr)
                '-', '=' -> checkArrow(itr)
                else -> null
            }
            if (token != null) {
                tokens.add(token)
            } else if (itr.hasNext()) {
                continue
            } else {
                break
            }
        }
        return tokens.toList()
    }

    private fun checkString(itr: Iterator<Char>): InfluxToken? {
//        val keyword = StringBuilder()
//
//        if (currentCharPos < length) {
//            currentChar = command[currentCharPos]
//            currentCharPos++
//        } else {
//            return null
//        }
//
//        while (currentChar!! in 'a'..'z' || currentChar!! in 'A'..'Z' || currentChar!! in '0'..'9') {
//            keyword.append(currentChar!!)
//            if (currentCharPos < length) {
//                currentChar = command[currentCharPos]
//                currentCharPos++
//            } else {
//                break
//            }
//        }
//        return InfluxToken(TokenType.KEYWORD, keyword.toString())
        return null
    }

    private fun checkNumber(itr: Iterator<Char>): InfluxToken? {
        return null
    }

    private fun checkKeyword(itr: Iterator<Char>): InfluxToken? {
//        val keyword = StringBuilder()
//        while (currentChar in 'a'..'z' || currentChar in 'A'..'Z' || currentChar in '0'..'9') {
//            keyword.append(currentChar)
//            if (currentCharPos < length) {
//                currentChar = command[currentCharPos]
//                currentCharPos++
//            } else {
//                break
//            }
//        }
//        return InfluxToken(TokenType.KEYWORD, keyword.toString())
        return null
    }

    private fun checkArrow(itr: Iterator<Char>): InfluxToken? {
        return null
    }
}
