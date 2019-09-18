package com.kimjio.lib.meal.network;

public interface OnTaskListener<T> {
    void onTaskFinished(T t);
}
