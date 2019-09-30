package com.kimjio.lib.meal.task;

import java.util.HashMap;
import java.util.Map;

public class Filter {
    private Map<String, Object> map = new HashMap<>();

    private Filter() {
    }

    <T> T get(String name, Class<T> clazz) {
        try {
            return clazz.cast(map.get(name));
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static class Builder {

        private Filter filter;

        public Builder() {
            filter = new Filter();
        }

        public Builder addFilter(String name, Object value) {
            filter.map.put(name, value);
            return this;
        }

        public Filter build() {
            return filter;
        }
    }
}
