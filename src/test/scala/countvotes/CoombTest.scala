package countvotes

import countvotes.methods.CoombRuleMethod
import countvotes.parsers.{CandidatesParser, PreferencesParser}
import countvotes.structures.Candidate
import org.specs2.mutable.Specification

import scala.collection.mutable.ListBuffer


class CoombTest extends Specification {

  val expectedCoombWinnerList = List(Candidate("Nashville"))

  "Coomb Test" should {
    "verify result" in {
      coombMethodVerification("14-example.e", "14-candidates.txt") shouldEqual expectedCoombWinnerList
    }
  }

  def coombMethodVerification(electionFile: String, candidatesFile: String): List[Candidate] = {

    val candidates = CandidatesParser.read("../Agora/files/Examples/" + candidatesFile)
    val election = PreferencesParser.read("../Agora/files/Examples/" + electionFile)

    CoombRuleMethod.winners(election, candidates, 1).map { _._1 }

  }
}
