package com.grocery.eunoia.service;

import com.grocery.eunoia.model.ModelEunoia;
import com.grocery.eunoia.repository.RepositoryEunoia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceEunoia {
    @Autowired
    private RepositoryEunoia repository;

    public List<ModelEunoia> getAllEunoias() {
        return repository.findAll();
    }

    public Optional<ModelEunoia> getEunoiaById(Long id) {
        return repository.findById(id);
    }

    public Optional<ModelEunoia> getEunoiaByEmail(String email) {
        return repository.findByEmail(email);
    }

    public ModelEunoia createOrUpdateEunoia(ModelEunoia eunoia) {
        return repository.save(eunoia);
    }

    public void deleteEunoia(Long id) {
        repository.deleteById(id);
    }

    public void deleteEunoiaByEmail(String email) {
        Optional<ModelEunoia> eunoia = repository.findByEmail(email);
        eunoia.ifPresent(value -> repository.delete(value));
    }

    public ModelEunoia updateEunoiaByEmail(String email, ModelEunoia eunoia) {
        Optional<ModelEunoia> existingEunoia = repository.findByEmail(email);
        if (existingEunoia.isPresent()) {
            eunoia.setId(existingEunoia.get().getId());
            return repository.save(eunoia);
        } else {
            return null;
        }
    }
}
