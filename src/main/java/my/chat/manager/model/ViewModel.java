package my.chat.manager.model;

public interface ViewModel {

    void addModelStateChangeListener(ModelStateChangeListener listener);

    void removeModelStateChangeListener(ModelStateChangeListener listener);
}
