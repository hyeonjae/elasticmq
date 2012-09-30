package org.elasticmq.storage.interfaced

import org.elasticmq.storage._

trait InterfacedCommandExecutor extends StorageCommandExecutor {
  def queuesStorage: QueuesStorage
  def messagesStorage(queueName: String): MessagesStorage
  def messageStatisticsStorage(queueName: String): MessageStatisticsStorage

  def execute[R](command: StorageCommand[R]) = command match {
    case CreateQueueCommand(queue) => queuesStorage.createQueue(queue)
    case UpdateQueueCommand(queue) => queuesStorage.updateQueue(queue)
    case DeleteQueueCommand(name) => queuesStorage.deleteQueue(name)
    case LookupQueueCommand(name) => queuesStorage.lookupQueue(name)
    case ListQueuesCommand() => queuesStorage.listQueues
    case GetQueueStatisticsCommand(name, deliveryTime) => queuesStorage.queueStatistics(name, deliveryTime)

    case ClearStorageCommand() => queuesStorage.clear()

    case SendMessageCommand(queueName, message) => messagesStorage(queueName).sendMessage(message)
    case UpdateNextDeliveryCommand(queueName, messageId, newNextDelivery) =>
      messagesStorage(queueName).updateNextDelivery(messageId, newNextDelivery)
    case ReceiveMessagesCommand(queueName, deliveryTime, newNextDelivery, maxCount) =>
      messagesStorage(queueName).receiveMessages(deliveryTime, newNextDelivery, maxCount)
    case DeleteMessageCommand(queueName, messageId) => messagesStorage(queueName).deleteMessage(messageId)
    case LookupMessageCommand(queueName, messageId) => messagesStorage(queueName).lookupMessage(messageId)

    case UpdateMessageStatisticsCommand(queueName, messageId, messageStatistics) =>
      messageStatisticsStorage(queueName).updateMessageStatistics(messageId, messageStatistics)
    case GetMessageStatisticsCommand(queueName, messageId) =>
      messageStatisticsStorage(queueName).readMessageStatistics(messageId)
  }
}
