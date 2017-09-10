package agora.methods

import agora.structures.{PreferenceBallot => Ballot}
import agora.algorithms._
import agora.structures._

import spire.math.Rational

import scala.collection.mutable.{HashMap => MMap}

object ApprovalRule extends VoteCounter[Ballot]
  with SimpleApproval {

  override def winners(election: Election[Ballot], ccandidates: List[Candidate],  numVacancies: Int): List[(Candidate, Rational)] = {

    val tls = countApprovals(election, ccandidates)
    tls.toList.sortWith( _._2 > _._2 ).take(numVacancies)
  }
}