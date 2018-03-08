package my.chat.manager.kafka;


import my.chat.manager.beans.Message;

public interface MessageConsumerListener {

    void newMessageReceived(Message newMessage);
}
