package my.chat.manager.model.impl;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import my.chat.manager.beans.Message;
import my.chat.manager.beans.UserChatStat;
import my.chat.manager.beans.UserOnlineStatusChangedEvent;
import my.chat.manager.kafka.MessageConsumer;
import my.chat.manager.kafka.UserOnlineStatusConsumer;
import my.chat.manager.model.ChatManagerViewModel;
import my.chat.manager.model.ModelStateChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@UIScope
@SpringComponent
public class ChatManagerViewModelImpl implements ChatManagerViewModel{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatManagerViewModelImpl.class);

    private final Collection<ModelStateChangeListener> stateChangelisteners = new ArrayList<>();
    private final Map<String, UserChatStat> userChatStatMap = new HashMap<>();
    private final Map<String, Boolean> userOnlineStatusMap = new HashMap<>();
    private final MessageConsumer messageConsumer;
    private final UserOnlineStatusConsumer userOnlineStatusConsumer;

    @Autowired
    public ChatManagerViewModelImpl(
            final MessageConsumer messageConsumer,
            final UserOnlineStatusConsumer UserOnlineStatusConsumer
    ){
        this.messageConsumer = messageConsumer;
        this.userOnlineStatusConsumer = UserOnlineStatusConsumer;
    }

    @PostConstruct
    private void startProcessing(){
        this.messageConsumer.setNewMessageReceivedListener(m -> onMessageReceived(m));
        this.userOnlineStatusConsumer.setUserOnlineStatusChangedListener(e -> onOnlineStatusChanged(e));
    }

    @PreDestroy
    private void stopProcessing(){
        this.messageConsumer.setNewMessageReceivedListener(null);
        this.userOnlineStatusConsumer.setUserOnlineStatusChangedListener(null);
    }

    @Override
    public void addModelStateChangeListener(final ModelStateChangeListener listener) {
        if( !this.stateChangelisteners.contains(listener) ){
            this.stateChangelisteners.add(listener);
        }
    }

    @Override
    public void removeModelStateChangeListener(final ModelStateChangeListener listener) {
        if( this.stateChangelisteners.contains(listener) ){
            this.stateChangelisteners.remove(listener);
        }
    }

    private void onMessageReceived(final Message message) {
        final String user = message.getSender();
        if( !this.userChatStatMap.containsKey(user) ){
            final UserChatStat newStat = new UserChatStat();
            newStat.setUser(user);
            this.userChatStatMap.put(user, newStat);
        }
        final UserChatStat stats = this.userChatStatMap.get(user);
        stats.increaseMessageCount();
        stats.increaseWordsCount(message.getMessageText().split(" ").length);

        LOGGER.info("Chat stats updated for user '{}': messages={}\twords={}", stats.getUser(), stats.getMessageCount(), stats.getWordsCount());
        update();
    }

    private void onOnlineStatusChanged(final UserOnlineStatusChangedEvent event) {
        LOGGER.info("Online status of user '{}' changed to {}.", event.getUser(), event.isOnline());
        this.userOnlineStatusMap.put(event.getUser(), event.isOnline());
        update();
    }

    private synchronized void update(){
        this.stateChangelisteners.forEach(l -> l.onModelStateChanged());
    }

    public Stream<String> registeredUsers(){
        return this.userOnlineStatusMap.keySet().stream();
    }

    public Stream<String> onlineUsers() {
        return registeredUsers() //
                .filter(u -> this.userOnlineStatusMap.get(u).booleanValue());
    }

    @Override
    public Stream<String> getRegisteredUsers() {
        return this.userOnlineStatusMap.keySet().stream() //
                .distinct() //
                .sorted();
    }

    @Override
    public Stream<String> getOnlineUsers() {
        return getRegisteredUsers() //
                .filter(u -> this.userOnlineStatusMap.get(u).booleanValue()) //
                .distinct() //
                .sorted();
    }

    @Override
    public Stream<UserChatStat> getUserChatStats() {
        return this.userChatStatMap.values().stream();
    }
}
