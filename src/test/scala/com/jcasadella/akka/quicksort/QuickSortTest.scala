package com.jcasadella.akka.quicksort

import akka.actor.{ActorRef, Actor, ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.duration._

class QuickSortTest extends TestKit(ActorSystem("QuickSortTest")) with WordSpecLike with BeforeAndAfterAll {

  override protected def afterAll(): scala.Unit = {
    system.shutdown()
  }

  "A quick sort controller" should {
    val probe = TestProbe()
    val controller = system.actorOf(Props[QuickSortController])

    "receive an empty result" in {
      probe.send(controller, Sort(List()))
      probe.expectMsg(1.seconds, Result(List()))
    }

    "receive a result with the same value" in {
      probe.send(controller, Sort(List(0)))
      probe.expectMsg(1.seconds, Result(List(0)))
    }

    "receive a result with a sorted list" in {
      probe.send(controller, Sort( scala.util.Random.shuffle(1 to 5).toList))
      probe.expectMsg(1.seconds, Result(List(1,2,3,4,5)))
    }

    "receive a result with a sorted list with negative values" in {
      probe.send(controller, Sort( scala.util.Random.shuffle(-5 to 5).toList))
      probe.expectMsg(1.seconds, Result(List(-5,-4,-3,-2,-1,0,1,2,3,4,5)))
    }

    "handle more requests after completed a result" in {
      probe.send(controller, Sort( scala.util.Random.shuffle(1 to 5).toList))
      probe.expectMsg(1.seconds, Result(List(1,2,3,4,5)))
      probe.send(controller, Sort( scala.util.Random.shuffle(1 to 5).toList))
      probe.expectMsg(1.seconds, Result(List(1,2,3,4,5)))
    }
  }

  "A sorter's parent actor" should {

    val probe = TestProbe()
    val sorterParent = system.actorOf(Props(new SorterFakeParent(probe.ref)), "sorterParent")

    "receive a sort and result message" in {
      probe.send(sorterParent, Sort(List(2,1)))
      probe.expectMsg(1.seconds, Sort(List(2,1)))
      probe.expectMsg(1.seconds, Result(List(1,2)))
    }
  }

  class SorterFakeParent(probe: ActorRef) extends Actor {
    val sorter = context.actorOf(Props[QuickSorter], "testChild")

    def receive = {
      case msg =>
        probe forward msg
        sorter forward msg
    }
  }
}
