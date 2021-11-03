package com.rums;

import java.util.function.Consumer;

public interface EventHandler<T> {
    void subscribe(Consumer<T> event);
    void unsubscribe(Consumer<T> event);
    void publish();
}
