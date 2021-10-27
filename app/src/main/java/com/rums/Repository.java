package com.rums;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Repository<T extends Identity> implements Crud<T> {
    private FirebaseDatabase context;
    private DatabaseReference reference;
    private List<T> entities = new ArrayList<>();

    public Repository(Class<T> type){
        context = FirebaseDatabase.getInstance();
        reference = context.getReference(type.getSimpleName());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    return;
                }

                for(DataSnapshot data : snapshot.getChildren()){
                    T entity = data.getValue(type);
                    entities.add(entity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Repo", error.getMessage());
            }
        });
    }

    @Override
    public T getById(String id) {
        return entities
                .stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst().get();
    }

    @Override
    public List<T> getRange(Predicate<T> predicate) {
        return entities
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> getAll() {
        return entities;
    }

    @Override
    public void insert(T entity) {
        if(exists(entity)){
            return;
        }

        entities.add(entity);
    }

    @Override
    public void update(T entity) {
        if(!exists(entity)){
            return;
        }

        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i).getId().equals(entity.getId())){
                entities.set(i, entity);
                break;
            }
        }
    }

    @Override
    public void delete(T entity) {
        entities.remove(entity.getId());
    }

    @Override
    public boolean exists(T entity){
        return entities
                .stream()
                .anyMatch(e -> e.getId().equals(entity.getId()));
    }

    @Override
    public void commit() {
        reference.setValue(entities);
    }
}
