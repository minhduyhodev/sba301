package com.fpt.sba301.slot18.service;

import com.fpt.sba301.slot18.entity.Orchid;
import com.fpt.sba301.slot18.entity.OrchidCategory;
import com.fpt.sba301.slot18.repository.OrchidCategoryRepository;
import com.fpt.sba301.slot18.repository.OrchidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrchidServiceImpl implements OrchidService {

    private final OrchidRepository orchidRepository;
    private final OrchidCategoryRepository categoryRepository;

    public OrchidServiceImpl(OrchidRepository orchidRepository,
                             OrchidCategoryRepository categoryRepository) {
        this.orchidRepository = orchidRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Orchid> findAll() {
        return orchidRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Orchid> findById(Long id) {
        return orchidRepository.findById(id);
    }

    @Override
    @Transactional
    public Orchid create(Orchid orchid) {
        if (orchid.getCategory() != null && orchid.getCategory().getId() != null) {
            OrchidCategory cat = categoryRepository.findById(orchid.getCategory().getId()).orElse(null);
            orchid.setCategory(cat);
        }
        return orchidRepository.save(orchid);
    }

    @Override
    @Transactional
    public Orchid update(Long id, Orchid input) {
        Optional<Orchid> existingOpt = orchidRepository.findById(id);
        if (existingOpt.isPresent()) {
            Orchid existing = existingOpt.get();
            existing.setOrchidName(input.getOrchidName());
            existing.setNatural(input.getNatural());
            existing.setOrchidDescription(input.getOrchidDescription());
            existing.setAttractive(input.getAttractive());
            existing.setOrchidUrl(input.getOrchidUrl());
            if (input.getCategory() != null && input.getCategory().getId() != null) {
                OrchidCategory cat = categoryRepository.findById(input.getCategory().getId()).orElse(null);
                existing.setCategory(cat);
            }
            return orchidRepository.save(existing);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        orchidRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Orchid> searchByName(String name) {
        return orchidRepository.findByOrchidNameContainingIgnoreCase(name);
    }
}
