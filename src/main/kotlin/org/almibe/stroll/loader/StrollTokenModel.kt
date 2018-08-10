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

@Deprecated("Delete after antlr rewrite")
enum class TokenType {
    STRING, //character string in double quotes
    INT, //any fixed point number that doesn't end with L
    LONG, //any fixed point number that ends with L
    DOUBLE, //any floating point number -- stroll doesn't support floats
    KEYWORD, //any character string out of quotes -- includes boolean values
    ARROW, // -> outside of quotes
    FAT_ARROW, // => outside of quotes
    COLON,
    START_BRACE,
    END_BRACE,
    START_BRACKET,
    END_BRACKET,
    COMMA,
    IDENTITY,
    UNDERSCORE
}

data class StrollToken(val tokenType: TokenType, val tokenContent: String)

data class CommandArguments (
        var commandType: CommandType? = null,
        var entityName: String = "",
        //three collections used by all commands
        val properties: MutableMap<String, StrollToken> = mutableMapOf(),
        val link: MutableMap<String, StrollToken> = mutableMapOf(),
        val links: MutableList<Pair<String, StrollToken>> = mutableListOf(),
        //collections only used by find
        val propertyExistsCheck: MutableList<String> = mutableListOf(),
        val linkExistsCheck: MutableList<String> = mutableListOf()
        //TODO eventually this will contain range and startsWith info
)

