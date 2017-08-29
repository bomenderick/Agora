package countvotes

import countvotes.methods.{SmithSetMethod}
import countvotes.parsers.{CandidatesParser, PreferencesParser}
import countvotes.structures.Candidate
import org.specs2.mutable.Specification

/**
  * Created by deepeshpandey on 16/07/17.
  */
class SmithSetTest extends Specification {

  val expectedSmithSet = Set(Candidate("A"), Candidate("B"), Candidate("C"))

  "SmithSet Test " should {

    "verify result" in { topCycleSetVerification("30-example.e", "30-candidates.txt") shouldEqual expectedSmithSet }
  }

  def topCycleSetVerification(electionFile: String, candidatesFile: String): Set[Candidate] = {

    val candidates = CandidatesParser.read("../Agora/files/Examples/" + candidatesFile)
    val election =  PreferencesParser.read("../Agora/files/Examples/" + electionFile)

    SmithSetMethod.winners(election, candidates, 1).map {_._1}.toSet
  }

}