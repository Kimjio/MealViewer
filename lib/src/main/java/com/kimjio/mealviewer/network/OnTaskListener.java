package com.kimjio.mealviewer.network;

public interface OnTaskListener<T> {
    void onTaskFinished(T t);
}
