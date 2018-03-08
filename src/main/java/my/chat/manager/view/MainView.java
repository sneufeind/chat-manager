package my.chat.manager.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@UIScope
@SpringViewDisplay
public class MainView extends VerticalLayout implements ViewDisplay {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);

    private final Map<Class<? extends View>, Button> navigationButtons = new HashMap();
    private Panel springViewDisplay;
    private Layout navigationBar;

    public MainView(){}

    @PostConstruct
    public void init(){
        // Title & User
        final Label titleLabel = new Label("Chat-Manager");
        titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
        titleLabel.addStyleName(ValoTheme.LABEL_BOLD);
        titleLabel.addStyleName(ValoTheme.LABEL_H1);

        // Navigable Panel
        this.navigationBar = new CssLayout();
        this.navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        // Root Panel
        this.springViewDisplay = new Panel();
        this.springViewDisplay.setSizeFull();

        setSizeFull();
        addComponents(titleLabel, this.navigationBar, this.springViewDisplay);
        setExpandRatio(this.springViewDisplay, 1.0f);

        attachViews();
    }

    private void attachViews(){
        attachNavigation(UserView.class, UserView.VIEW_NAME, "Users");
        attachNavigation(ChatStatsView.class, ChatStatsView.VIEW_NAME, "Stats");
    }

    private void attachNavigation(final Class<? extends View> view, final String viewName, final String caption){
        final Button naviBtn = createNavigationButton(caption, viewName);
        this.navigationButtons.put(view, naviBtn);
        this.navigationBar.addComponent(naviBtn);
    }

    private Button createNavigationButton(final String caption, final String viewName) {
        final Button button = new Button(caption);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    @Override
    public void showView(View view) {
        if(view instanceof Component) {
            this.springViewDisplay.setContent(Component.class.cast(view));
        }else{
            LOGGER.error("Can not show view '{}' because it is not a component.", view);
        }
    }
}

