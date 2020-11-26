/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package dev.ligature.formats.ntriples

import dev.ligature.Statement
import munit.FunSuite

class WanderSuite extends FunSuite {
  test("run empty script") {
    val res = runFile("empty.wander")
    ???
  }

  test("run empty script with comments") {
    val res = runFile("comments.wander")
    ???
  }

  test("get all datasets") {
    val res = runFile("allDatasets.wander")
    ???
  }

  test("create dataset") {
    val res = runFile("createDataset.wander")
    ???
  }

  test("delete dataset") {
    val res = runFile("deleteDatasett.wander")
    ???
  }
}

object WanderSuite {
  def createLigatureInstance(): LigatureInstace = {
    ???
  }

  def runFile(fileName: String): Either[Error, Result] = {
    ???
  }
}
