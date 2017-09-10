package agora.votecounter

import agora.structures._

import scala.collection.mutable.{HashSet, HashMap => Map}

import spire.math.Rational
import agora.votecounter.stv.Input

abstract class VoteCounter[B <: Ballot] {
  
  
  def winners(e: Election[B], ccandidates: List[Candidate], numVacancies: Int): List[(Candidate,Rational)]

  def runVoteCounter(election: Election[B], candidates: List[Candidate], numVacancies: Int): Report[B]  = {

    val result: Result = new Result
    val report: Report[B] = new Report[B]

    
    if (!election.isEmpty && election.head.isInstanceOf[PreferenceBallot]) {
      var tls = Election.totals(election.asInstanceOf[Election[PreferenceBallot]], candidates)

      result.addTotalsToHistory(tls)
      
      report.newCount(Input, None, None, Some(tls), None, None)
    }


    report.setCandidates(candidates)



    report.setWinners(winners(election, candidates, numVacancies))

    report
  }
}



