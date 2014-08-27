package com.abishekk92

import akka.actor.{ActorRef, Actor, Props, ActorSystem}
import com.rabbitmq.client.ConnectionFactory
import com.github.sstone.amqp.{Amqp, Consumer, ConnectionOwner}
import scala.concurrent.duration._
import com.github.sstone.amqp.Amqp._
import com.github.sstone.amqp.Amqp.Ack
import com.github.sstone.amqp.Amqp.DeclareQueue
import com.github.sstone.amqp.Amqp.QueueParameters
import com.github.sstone.amqp.Amqp.Delivery

/**
 * Created by abishek on 23/08/14.
 */
object LogQueueConsumer extends App {
    implicit val system = ActorSystem("LogQueueConsumer")

    val connectionFactory = new ConnectionFactory();

    connectionFactory.setUri("amqp://localhost/%2F")

    val connection = system.actorOf(ConnectionOwner.props(connectionFactory, 1 second))

    val listener = system.actorOf(Props(new Actor {
      def receive = {
        case Delivery(consumerTag, envelope, properties, body) => {
          println(new String(body))
          sender ! Ack(envelope.getDeliveryTag)
        }
      }
    }))

  private val consumer: ActorRef = ConnectionOwner.createChildActor(connection, Consumer.props(listener, channelParams = None, autoack = false))

  Amqp.waitForConnection(system, consumer).await()

  private val queueParams: QueueParameters = QueueParameters(name="log_queue",passive=false, durable=false, exclusive = false, autodelete = true)

  consumer ! DeclareQueue(queueParams)

  consumer ! QueueBind(queue = "log_queue", exchange = "amq.direct", routing_key = "log_key")

  consumer ! AddQueue(QueueParameters(name="log_queue", passive=false))

  System.in.read()

  system.shutdown()
}