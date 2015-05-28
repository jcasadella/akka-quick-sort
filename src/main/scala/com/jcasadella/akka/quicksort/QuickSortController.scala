package com.jcasadella.akka.quicksort

import akka.actor.{ActorRef, Props, Actor}
import akka.event.LoggingReceive


trait SortMessage

case class Sort(list: List[Int]) extends SortMessage

case class Result(sorted: List[Int]) extends SortMessage


class QuickSortController extends Actor {

  override def receive: Receive = normal

  def normal(): Receive = LoggingReceive {
    case Sort(list) =>
      val sorter = context.actorOf(Props[QuickSorter])
      println(s"INPUT: $list")
      sorter ! Sort(list)
      context.become(waitingResult(sender()))
  }

  def waitingResult(caller: ActorRef): Receive = LoggingReceive {
    case Result(res) =>
      println(s"RESULT $res")
      caller ! Result(res)
      context.become(normal)
  }

}
