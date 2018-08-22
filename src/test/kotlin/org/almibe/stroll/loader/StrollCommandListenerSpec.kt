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

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class StrollCommandListenerSpec : StringSpec({
    "new with single link" {
        val script = "stroll.new { User name: \"Margret\", contact -> 0-2, supervises => [ 0-3, 0-2] }"
        val result = readScript(script)

        val strollScript = StrollScript()
        //TODO add line with correct contents

        result shouldBe strollScript
    }

    "five major types" {
        val script = "new TypeTest { int: 3, long: 1009L, double: 3.14, string: \"Test\", boolean: true }"
        val result = readScript(script)

        val strollScript = StrollScript()
        //TODO add line with correct contents

        result shouldBe strollScript
    }

    "update by adding age and extra user link" {
        val script = "update 0-4 { age: 45, supervises => [ 0-1 ], supervises => 0-0 }"
        val result = readScript(script)

        val strollScript = StrollScript()
        //TODO add line with correct contents

        result shouldBe strollScript
    }

    "delete list of entities" {
        val script = "delete [2-1, 2-2]"
        val result = readScript(script)

        val strollScript = StrollScript()
        //TODO add line with correct contents

        result shouldBe strollScript
    }

    "find User based on links" {
        val script = "find User { supervises => [ 0-1, 0-3 ] }"
        val result = readScript(script)

        val strollScript = StrollScript()
        //TODO add line with correct contents

        result shouldBe strollScript
    }

    "find Users with property and link exists queries" {
        val script = "find User { supervises => _ }"
        val result = readScript(script)

        val strollScript = StrollScript()
        //TODO add line with correct contents

        result shouldBe strollScript
    }

    "new and find with namespaced id" {
        val script = "new test.space.User { username:\"Juniper\" }"
        val result = readScript(script)

        val strollScript = StrollScript()
        //TODO add line with correct contents

        result shouldBe strollScript
    }
})
