package com.fpt.sba301.slot18.controller;

import com.fpt.sba301.slot18.entity.Orchid;
import com.fpt.sba301.slot18.service.OrchidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orchids")
public class OrchidController {

    private final OrchidService service;

    public OrchidController(OrchidService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Orchid>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orchid> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Orchid>> search(
            @RequestParam(defaultValue = "") String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<Orchid> create(@RequestBody Orchid orchid) {
        Orchid created = service.create(orchid);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orchid> update(@PathVariable Long id,
                                         @RequestBody Orchid input) {
        Orchid updated = service.update(id, input);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
