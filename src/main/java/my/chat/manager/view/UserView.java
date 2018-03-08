package my.chat.manager.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import my.chat.manager.model.ChatManagerViewModel;
import my.chat.manager.model.ModelStateChangeListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@ViewScope
@SpringView(name = UserView.VIEW_NAME)
public class UserView extends HorizontalLayout implements View, ModelStateChangeListener {

    public static final String VIEW_NAME = "";

    private final ChatManagerViewModel viewModel;
    private final UIUpdater uiUpdater;

    private ListSelect<String> registeredUsers;
    private ListSelect<String> onlineUsers;

    @Autowired
    public UserView(final ChatManagerViewModel viewModel, final UIUpdater uiUpdater){
        this.viewModel = viewModel;
        this.uiUpdater = uiUpdater;
    }

    @PostConstruct
    void init() {
        this.registeredUsers = new ListSelect<>("Registered users");
        this.registeredUsers.setSizeFull();
        this.registeredUsers.setWidth(200.f, Unit.PIXELS);
        this.onlineUsers = new ListSelect<>("Online users");
        this.onlineUsers.setSizeFull();
        this.onlineUsers.setWidth(200.f, Unit.PIXELS);

        addComponents(this.registeredUsers, this.onlineUsers);
    }

    @PreDestroy
    private void cleanUp(){
        this.viewModel.removeModelStateChangeListener(this);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.viewModel.addModelStateChangeListener(this);
        update();
    }

    @Override
    public void onModelStateChanged() {
        update();
    }

    private void update(){
        updateRegisteredUsers();
        updateOnlineUsers();
        this.uiUpdater.update(getUI());
    }

    private void updateOnlineUsers() {
        this.onlineUsers.setItems(this.viewModel.getOnlineUsers());
    }

    private void updateRegisteredUsers() {
        this.registeredUsers.setItems(this.viewModel.getRegisteredUsers());
    }
}
