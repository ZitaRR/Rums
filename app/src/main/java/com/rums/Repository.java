package com.rums;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Repository<T extends Identity> implements Crud<T>, EventHandler<List<T>> {
    private FirebaseDatabase context;
    private DatabaseReference reference;
    private List<T> entities = new ArrayList<>();
    private List<Consumer<T>> listeners = new ArrayList<>();
    private boolean initialized = false;

    public Repository(Class<T> type){
        Log.d("Tag__1", "Repository type: " + type);

        context = FirebaseDatabase.getInstance();
        reference = context.getReference(type.getSimpleName());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    initialized = true;
                    return;
                }

                for(DataSnapshot data : snapshot.getChildren()){
                    T entity = data.getValue(type);
                    if(exists(entity)){
                        continue;
                    }
                    entities.add(entity);
                }

                if(!initialized){
                    initialized = true;
                    BaseClassActivity.getCurrentInstance().repositoryIsInitialized(type);
                    return;
                }

                publish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Repo", error.getMessage());
            }
        });
    }

    public String getUniqueKey() {
        return reference.push().getKey();
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
        if(!initialized){
            throw new IllegalStateException("Repository has not yet been initialized");
        }

        reference.setValue(entities);
        entities.clear();
    }

    @Override
    public void subscribe(Consumer event) {
        if(listeners.contains(event)){
            return;
        }

        listeners.add(event);
    }

    @Override
    public void unsubscribe(Consumer event) {
        if(!listeners.contains(event)){
            return;
        }

        listeners.remove(event);
    }

    @Override
    public void publish() {
        if(!initialized){
            throw new IllegalStateException("Repository has not yet been initialized");
        }

        for(Consumer event : listeners){
            event.accept(entities);
        }
    }
}
