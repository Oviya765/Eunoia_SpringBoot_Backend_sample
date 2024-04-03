package com.grocery.eunoia.controller;
import com.grocery.eunoia.model.ModelEunoia;
import com.grocery.eunoia.service.ServiceEunoia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
// import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/eunoias")
public class ControllerEunoia {

    @Autowired
    private ServiceEunoia serviceEunoia;

    @GetMapping
    public List<ModelEunoia> getAllEunoias() {
        return serviceEunoia.getAllEunoias();
    }

    // @GetMapping("/")
    // public Page<ModelEunoia> getEunoiasPaginate(@RequestParam(defaultValue = "10") int page, @RequestParam(defaultValue = "10") int size,) {

    @GetMapping("/{id}")
    public ResponseEntity<ModelEunoia> getEunoiaById(@PathVariable Long id) {
        Optional<ModelEunoia> eunoia = serviceEunoia.getEunoiaById(id);
        return eunoia.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<ModelEunoia> getEunoiaByEmail(@RequestParam String email) {
        Optional<ModelEunoia> eunoia = serviceEunoia.getEunoiaByEmail(email);
        return eunoia.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEunoia(@RequestBody ModelEunoia eunoia) {
        Optional<ModelEunoia> existingEunoiaOptional = serviceEunoia.getEunoiaByEmail(eunoia.getEmail());
        if (existingEunoiaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists. Please use a different email.");
        } else {
            // Attempt to create the new user
            try {
                ModelEunoia createdEunoia = serviceEunoia.createOrUpdateEunoia(eunoia);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdEunoia);
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid request. Please provide valid data.");
            }
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ModelEunoia> updateEunoia(@PathVariable Long id, @RequestBody ModelEunoia eunoia) {
        eunoia.setId(id);
        ModelEunoia updatedEunoia = serviceEunoia.createOrUpdateEunoia(eunoia);
        return new ResponseEntity<>(updatedEunoia, HttpStatus.OK);
    }

    @PutMapping("/updateByEmail/{email}")
    public ResponseEntity<ModelEunoia> updateEunoiaByEmail(@PathVariable String email, @RequestBody ModelEunoia eunoia) {
        Optional<ModelEunoia> existingEunoia = serviceEunoia.getEunoiaByEmail(email);
        if (existingEunoia.isPresent()) {
            ModelEunoia updatedEunoia = serviceEunoia.updateEunoiaByEmail(email, eunoia);
            return new ResponseEntity<>(updatedEunoia, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEunoia(@PathVariable Long id) {
        serviceEunoia.deleteEunoia(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/deleteByEmail/{email}")
    public ResponseEntity<Void> deleteEunoiaByEmail(@PathVariable String email) {
        Optional<ModelEunoia> existingEunoia = serviceEunoia.getEunoiaByEmail(email);
        if (existingEunoia.isPresent()) {
            serviceEunoia.deleteEunoiaByEmail(email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
