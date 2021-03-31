package com.freesonwill.swipeTo2048.model.observable;

import java.util.ArrayList;
import java.util.List;

public class ObservableField<T> {
    private T t;
    private List<OnFieldChangeListener<T>> listeners = new ArrayList<>();

    public ObservableField(T t){
        this.t = t;
    }

    public void set(T t) {
        set(t, true);
    }

    public void set(T t, boolean notify) {
        T oldVal = t;
        this.t = t;
        if (notify) {
            for (OnFieldChangeListener l : listeners) {
                l.onDataChange(this, oldVal, t);
            }
        }
    }

    public T get() {
        return t;
    }

    public void addListener(OnFieldChangeListener<T> l) {
        listeners.add(l);
    }

    public void removeListener(OnFieldChangeListener<T> l) {
        listeners.remove(l);
    }
}

