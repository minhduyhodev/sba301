package com.fpt.sba301.slot18.service;

import com.fpt.sba301.slot18.entity.Orchid;

import java.util.List;
import java.util.Optional;

public interface OrchidService {
    List<Orchid> findAll();

    Optional<Orchid> findById(Long id);

    Orchid create(Orchid orchid);

    Orchid update(Long id, Orchid input);

    void delete(Long id);

    List<Orchid> searchByName(String name);
}
