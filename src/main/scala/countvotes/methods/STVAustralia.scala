// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package countvotes.methods


import countvotes.structures._
import countvotes.algorithms._


import scala.collection.immutable.ListMap
import collection.mutable.{HashMap => Map}
import scala.collection.SortedMap
import collection.mutable.HashSet
import collection.breakOut
import scala.util.Random
import scala.util.Sorting
import java.io._


abstract class STVAustralia extends STV[ACTBallot]
 {


 def tryToDistributeSurplusVotes(
   election: Election[ACTBallot], ccandidates: List[Candidate], winner: Candidate,
   ctotal:Rational, markings: Option[Set[Int]] ):
    (Election[ACTBallot], List[(Candidate,Rational)])

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 def runScrutiny(election: Election[ACTBallot], candidates: List[Candidate], numVacancies: Int):
    Report[ACTBallot] = {  // all ballots of e are marked when the function is called
   val quota = cutQuotaFraction(computeQuota(election.length, numVacancies))
   println("Number of ballots:" + election.length)
   println("Quota: " + quota)
   result.setQuota(quota)
   report.setQuota(quota)

   val tls = totals(election, candidates) // Here are totals also of those candidates
                                          // that are NOT OCCURING in the ballots (i.e. when nobody mentioned them in preferences)
   result.addTotalsToHistory(tls)

   //report.setCandidates(getCandidates(election))  // Here are candidates OCCURING in the election
   report.setCandidates(candidates)  // Here are totals also of those candidates that are NOT OCCURING in the ballots


   report.newCount(Input, None, Some(election), Some(tls), None, None)
   report.setLossByFractionToZero

   report.setWinners(winners(election, candidates, numVacancies))

   report
 }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ACT Legislation:
// 9(1): If a candidate is excluded in accordance with clause 8, the ballot papers counted for the candidate
// shall be sorted into groups according to their transfer values when counted for him or her.
//
 // like ACT
// Senate Legislation:
// (13AA)(a) and (13AA)(b)
//

  def determineStepsOfExclusion(election: Election[ACTBallot], candidate: Candidate): List[(Candidate, Rational)] = {
   var s: Set[(Candidate, Rational)] = Set()

   for (b <- election) {
      if (b.preferences.nonEmpty && b.preferences.head == candidate && !s.contains((candidate,b.value))) {
        s += ((candidate, b.value)) }
    }
   s.toList.sortBy(x => x._2).reverse //>
  }


}
