/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package dev.ligature.formats.ntriples

import cats.effect.IO
import cats.parse.{Parser1, Parser => P}
import dev.ligature.{NamedNode, Statement}

object Wander {
  def runScript(in: String): Task[Either[Error, Result]] = {
    ???
  }
}
