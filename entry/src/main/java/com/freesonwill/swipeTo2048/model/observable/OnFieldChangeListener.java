package com.freesonwill.swipeTo2048.model.observable;

public interface OnFieldChangeListener<T> {
    void onDataChange(ObservableField<T> field,T oldVal, T newVal);
}
