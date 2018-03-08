package my.chat.manager.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import my.chat.manager.beans.UserChatStat;
import my.chat.manager.model.ChatManagerViewModel;
import my.chat.manager.model.ModelStateChangeListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@ViewScope
@SpringView(name = ChatStatsView.VIEW_NAME)
public class ChatStatsView extends HorizontalLayout implements View, ModelStateChangeListener {

    public static final String VIEW_NAME = "chat_stats";

    private final ChatManagerViewModel viewModel;
    private final UIUpdater uiUpdater;

    private Grid<UserChatStat> statsTable;

    @Autowired
    public ChatStatsView(final ChatManagerViewModel viewModel, final UIUpdater uiUpdater){
        this.viewModel = viewModel;
        this.uiUpdater = uiUpdater;
    }

    @PostConstruct
    void init() {
        this.statsTable = new Grid<>();
        this.statsTable.setSizeFull();
        this.statsTable.addColumn(UserChatStat::getUser).setCaption("User");
        this.statsTable.addColumn(UserChatStat::getMessageCount).setCaption("Message count");
        this.statsTable.addColumn(UserChatStat::getWordsCount).setCaption("Word count");
        addComponents(this.statsTable);
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
        this.statsTable.setItems(this.viewModel.getUserChatStats());
        this.uiUpdater.update(getUI());
    }
}
