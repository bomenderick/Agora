package countvotes.methods

import countvotes.structures._
import countvotes.algorithms._
import scala.math._

abstract class EgalitarianVotingMethod[B <: WeightedBallot with Weight] extends VoteCountingMethod[B] {
  val report: Report[B] = new Report[B]

  //def computeWinners(election: Election[B], numVacancies: Int): List[(Candidate,Rational)]

  def getCandidateList(election: Election[B]): List[Candidate] = {
    var candidateList:List[Candidate] = List()
    for(i <- election){
      for(j <- i.preferences){
        if(!(candidateList contains j)){
          candidateList = candidateList :+ j
        }
      }
    }
    candidateList
  }

  def rank(election: Election[B], voter: Int, candidate: Candidate): (Boolean,Int) = {
    for(i <- 0 to (election(voter).preferences.length-1)) { //Kept in "old" format as the rank itself is returned. Seems to be the simplest way, instead of getting the candidates, then searching back for their index.
      if(election(voter).preferences(i) == candidate){
        return (true,i)
      }
    }
    return (false,0)
  }

  def utilityIndividual(election: Election[B], voter: Int, candidate: Candidate): Int = rank(election,voter,candidate) match {
    case (true,rank) => return getCandidateList(election).length + 1 - rank
    case (false,_) => return 0
    return 0
  }

  def utilitySet(election: Election[B], voter: Int, candidates: List[Candidate]): Int = {
    val sum: Int = candidates map { c => utilityIndividual(election, voter, c)} reduce { _ + _ }
    sum
  }

  def socialWelfare(election: Election[B], candidates: List[Candidate], fairness: Double): Double = {
    var sum: Double = 0
    for(i <- 0 to (election.length-1)){
      sum = sum + exp((1/fairness) * log((election(i).weight).toDouble * utilitySet(election,i,candidates)))
    }
    sum
  }

  def maxTuple1(x:(Double, List[Candidate]), y:(Double, List[Candidate])): (Double, List[Candidate]) = {
    if (x._1 > y._1) {x}
    else {y}
  }
}
