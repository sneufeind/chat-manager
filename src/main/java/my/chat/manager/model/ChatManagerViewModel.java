package my.chat.manager.model;

import my.chat.manager.beans.UserChatStat;

import java.util.stream.Stream;

public interface ChatManagerViewModel extends ViewModel{

    Stream<String> getRegisteredUsers();

    Stream<String> getOnlineUsers();

    Stream<UserChatStat> getUserChatStats();
}
