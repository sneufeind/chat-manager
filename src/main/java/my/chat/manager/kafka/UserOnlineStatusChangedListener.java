package my.chat.manager.kafka;

import my.chat.manager.beans.UserOnlineStatusChangedEvent;

public interface UserOnlineStatusChangedListener {

    void onOnlineStatusChanged(UserOnlineStatusChangedEvent event);
}
