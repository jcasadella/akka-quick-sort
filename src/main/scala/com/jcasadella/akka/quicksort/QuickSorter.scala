package com.jcasadella.akka.quicksort

import akka.actor.{Props, ActorRef, Actor}

class QuickSorter extends Actor {

  override def receive: Actor.Receive = {
    case Sort(list) => {
      list match {
        case l :: Nil =>
          sender ! Result(List(l))
        case Nil => sender ! Result(List())
        case head :: tail => {
          val pivotValue = head
          val (left, right) = tail.partition(x => x < head)
          val leftSorter = context.actorOf(Props[QuickSorter])
          val rightSorter = context.actorOf(Props[QuickSorter])
          leftSorter ! Sort(left)
          rightSorter ! Sort(right)
          context.become(waitingBothResults(pivotValue, leftSorter, rightSorter))
        }
      }
    }
  }

  def waitingBothResults(pivot: Int, leftSorter: ActorRef, rightSorter: ActorRef): Receive = {
    case Result(firstRes) => {
      if (sender() == leftSorter) {
        context.become(waitingRightResult(pivot, firstRes))
      } else {
        context.become(waitingLeftResult(pivot, firstRes))
      }
    }
  }

  def waitingRightResult(pivot: Int, leftRes: List[Int]): Receive = {
    case Result(rightRes) => {
      context.parent ! Result(leftRes ++ List(pivot) ++ rightRes)
      context.stop(self)
    }
  }

  def waitingLeftResult(pivot: Int, rightRes: List[Int]): Receive = {
    case Result(leftRes) => {
      context.parent ! Result(leftRes ++ List(pivot) ++ rightRes)
      context.stop(self)
    }
  }

}
