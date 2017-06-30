import countvotes.methods.UncoveredSetMethod
import countvotes.parsers.{CandidatesParser, PreferencesParser}
import countvotes.structures.Candidate
import org.specs2.mutable.Specification

/**
  * Test class for unconvered set for the preference profile in 21-example.e
  */
class UnconveredSetTest extends Specification{

  val expectedUncoveredSet = Set(Candidate("A"), Candidate("B"), Candidate("C"))

  "UnconveredSet Test " should {

    "verify result" in { unconveredSetMethodVerification("21-example.e", "21-candidates.txt") shouldEqual expectedUncoveredSet }
  }

  def unconveredSetMethodVerification(electionFile: String, candidatesFile: String): Set[Candidate] = {

    val candidates = CandidatesParser.read("../Agora/files/Examples/" + candidatesFile)
    val election =  PreferencesParser.read("../Agora/files/Examples/" + electionFile)

    UncoveredSetMethod.winners(election, candidates, candidates.length).map {_._1}.toSet
  }

}
