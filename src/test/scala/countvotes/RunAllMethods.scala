package countvotes


import ammonite.ops._
import org.specs2.mutable.Specification

import scala.collection.mutable

/**
  * This class executes all vote counting methods.
  * Its purpose is merely to check that Agora compiles and 
  * does not throw any obvious run-time exceptions.
  *
  */
class RunAllMethods extends Specification {

  def test(method: String, electionFile: String, candidatesFile: String): Boolean = {
    Main.main(Seq("-d", "files/Examples/", "-b", electionFile, "-c", candidatesFile, "-v", "2", "-m", method).toArray)
    return true
  }
  
  def str(i: Int) = if (i < 10) "0" + i else i.toString
  
  def run(method: String): Boolean = {
    (1 to 12) map { i => str(i) } map { 
      i: String => test(method, s"$i-example.txt",s"$i-candidates.txt")
    } reduce { (b1,b2) => b1 && b2}
  }

  "Agora" should {
    "execute EVACS" in { test("EVACS", "02-example.txt", "02-candidates.txt") shouldEqual true }
    "execute EVACSnoLP" in { test("EVACSnoLP", "02-example.txt", "02-candidates.txt") shouldEqual true }
    "execute EVACSDWD" in { test("EVACSDWD", "02-example.txt", "02-candidates.txt") shouldEqual true }
    "execute Senate" in { test("Senate", "02-example.txt", "02-candidates.txt") shouldEqual true }
    "execute Simple" in { test("Simple", "02-example.txt", "02-candidates.txt") shouldEqual true }
    "execute Egalitarian" in { test("Egalitarian", "02-example.txt", "02-candidates.txt") shouldEqual true }
    "execute Majority" in { test("Majority", "02-example.txt", "02-candidates.txt") shouldEqual true }
  }
}
