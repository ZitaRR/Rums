package com.rums;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface Crud<T extends Identity> {
    T getById(String id);
    List<T> getRange(Predicate<Map.Entry<String, T>> predicate);
    List<T> getAll();
    void insert(T entity);
    void update(T entity);
    void delete(T entity);
    void commit();
}
