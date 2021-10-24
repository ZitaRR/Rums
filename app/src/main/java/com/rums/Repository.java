package com.rums;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Repository<T extends Identity> implements Crud<T> {
    private FirebaseDatabase context;
    private DatabaseReference reference;
    private HashMap<String, T> entities = new HashMap<>();

    public Repository(Class<T> type){
        context = FirebaseDatabase.getInstance();
        reference = context.getReference(type.getSimpleName());
    }

    @Override
    public T getById(String id) {
        return entities.entrySet()
                .stream()
                .filter(entity -> entity.getValue().getId().equals(id))
                .map(Map.Entry::getValue)
                .findFirst().get();
    }

    @Override
    public List<T> getRange(Predicate<Map.Entry<String, T>> predicate) {
        return entities.entrySet()
                .stream()
                .filter(predicate)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public void insert(T entity) {
        entities.put(entity.getId(), entity);
    }

    @Override
    public void update(T entity) {
        if(entities.containsKey(entity.getId())){
            return;
        }

        entities.put(entity.getId(), entity);
    }

    @Override
    public void delete(T entity) {
        entities.remove(entity.getId());
    }

    @Override
    public void commit() {
        reference.setValue(entities);
    }
}
