package com.fpt.sba301.slot20.services;

import com.fpt.sba301.slot20.pojos.Orchid;
import com.fpt.sba301.slot20.repositories.IOrchidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrchidService implements IOrchidService {

    @Autowired
    private IOrchidRepository iOrchidRepository;

    @Override
    public List<Orchid> getAllOrchids() {
        return iOrchidRepository.findAll();
    }

    @Override
    public Orchid insertOrchid(Orchid orchid) {
        return iOrchidRepository.save(orchid);
    }

    @Override
    public Orchid updateOrchid(int orchidID, Orchid orchid) {
        Optional<Orchid> existing = iOrchidRepository.findById(orchidID);
        if (existing.isPresent()) {
            Orchid o = existing.get();
            o.setOrchidName(orchid.getOrchidName());
            o.setNatural(orchid.isNatural());
            o.setOrchidDescription(orchid.getOrchidDescription());
            o.setOrchidCategory(orchid.getOrchidCategory());
            o.setAttractive(orchid.isAttractive());
            o.setOrchidURL(orchid.getOrchidURL());
            return iOrchidRepository.save(o);
        }
        return null;
    }

    @Override
    public void deleteOrchid(int orchidID) {
        iOrchidRepository.deleteById(orchidID);
    }

    @Override
    public Optional<Orchid> getOrchidByID(int orchidID) {
        return iOrchidRepository.findById(orchidID);
    }
}
