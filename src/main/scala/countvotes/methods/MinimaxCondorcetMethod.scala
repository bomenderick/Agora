package countvotes.methods

import com.typesafe.scalalogging.LazyLogging
import countvotes.structures._

import collection.mutable.{ListBuffer, HashMap => MMap}

/**
  * Algorithm : https://en.wikipedia.org/wiki/Minimax_Condorcet
  * Variant : winning votes => W = \arg \min_X ( \max_Y score(Y, X))
  */
object MinimaxCondorcetMethod extends VoteCountingMethod[WeightedBallot] with LazyLogging{

  private val result: Result = new Result
  private val report: Report[WeightedBallot] = new Report[WeightedBallot]
  private val rational0 = Rational(0, 1)
  private val majorityThreshold = Rational(1, 2)

  def runScrutiny(election: Election[WeightedBallot], candidates: List[Candidate], numVacancies: Int): Report[WeightedBallot] = {

    print("\n INPUT ELECTION: \n")
    printElection(election)

    report.setCandidates(candidates)

    report.setWinners(winners(election, candidates, numVacancies))

    report
  }

  def winners(election: Election[WeightedBallot], ccandidates: List[Candidate], numVacancies: Int):
  List[(Candidate, Rational)] = {

    logger.info("Computing minimax Condorcet Winner")

    val pairwiseComparisons = getPairwiseComparisonForWeightedElection(election, ccandidates)
    val mcScores = getMinimaxCondorcetScores(pairwiseComparisons, ccandidates)

    List(ccandidates.map(c => (c, mcScores.map {_(ccandidates.indexOf(c))}.max)).minBy(_._2))

  }

  def getMinimaxCondorcetScores(pairwiseComparisons: Array[Array[Rational]], ccandidates: List[Candidate]): Array[Array[Rational]] = {

    val minimaxCondorcetScores = Array.fill(ccandidates.size, ccandidates.size)(Rational(0, 1))

    for (i <- ccandidates.indices)
      for (j <- ccandidates.indices)
        if (pairwiseComparisons(i)(j) > pairwiseComparisons(j)(i)) {
          minimaxCondorcetScores(i)(j) = pairwiseComparisons(i)(j)
        }

    minimaxCondorcetScores
  }
}