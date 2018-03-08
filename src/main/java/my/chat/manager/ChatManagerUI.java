package my.chat.manager;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import my.chat.manager.view.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Theme(ValoTheme.THEME_NAME)
@Title("Chat-Manager")
@Push
@SpringUI
@Viewport("width=device-width,initial-scale=1.0,user-scalable=no")
public class ChatManagerUI extends UI{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatManagerUI.class);

    private MainView mainView;

    @Autowired
    public ChatManagerUI(final MainView mainView){
        this.mainView = mainView;
    }

    @Override
    protected void init(VaadinRequest request) {
        setErrorHandler(event -> {
            Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
            LOGGER.error("Error during request!", t);
        });
//        getNavigator().setErrorView(ChatErrorView.class);

        setContent(this.mainView);

        LOGGER.info("Session-Id: {}", UI.getCurrent().getSession().getSession().getId());
        LOGGER.info("Push-Id: {}", UI.getCurrent().getSession().getPushId());
    }
}


