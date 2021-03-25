package com.freesonwill.swipeTo2048.model.observable;

import java.util.ArrayList;
import java.util.List;

public class ObservableField<T> {
    private T t;
    private List<OnFieldChangeListener> listeners = new ArrayList<>();

    public void set(T t) {
        T oldVal = t;
        this.t = t;
        for (OnFieldChangeListener l : listeners) {
            l.onDataChange(this, oldVal, t);
        }
    }

    public T get() {
        return t;
    }

    private void addListener(OnFieldChangeListener l) {
        listeners.add(l);
    }

    public void removeListener(OnFieldChangeListener l) {
        listeners.remove(l);
    }
}

