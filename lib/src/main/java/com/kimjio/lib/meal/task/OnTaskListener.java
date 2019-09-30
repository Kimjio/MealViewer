package com.kimjio.lib.meal.task;

public interface OnTaskListener<T> {
    void onTaskFinished(T t, boolean error);
}
