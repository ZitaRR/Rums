package com.rums;

import java.util.List;
import java.util.function.Predicate;

public interface Crud<T extends Identity> {
    T getById(String id);
    List<T> getRange(Predicate<T> predicate);
    List<T> getAll();
    void insert(T entity);
    void update(T entity);
    void delete(T entity);
    boolean exists(T entity);
    void commit();
}
