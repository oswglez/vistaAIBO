package com.expectra.roombooking.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class BaseService<T, ID> {

    @Autowired
    protected JpaRepository<T, ID> repository;

    public List<T> findAll() {
        return repository.findAll();
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public T update(ID id, T updatedEntity) {
        return repository.findById(id)
                .map(entity -> {
                    BeanUtils.copyProperties(updatedEntity, entity, "id");
                    return repository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id " + id));
    }

    public void deleteById(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found with id " + id);
        }
    }
}
