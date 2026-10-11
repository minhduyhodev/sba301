package com.fpt.sba301.slot20.services;

import com.fpt.sba301.slot20.pojos.Orchid;

import java.util.List;
import java.util.Optional;

public interface IOrchidService {
    List<Orchid> getAllOrchids();

    Orchid insertOrchid(Orchid orchid);

    Orchid updateOrchid(int orchidID, Orchid orchid);

    void deleteOrchid(int orchidID);

    Optional<Orchid> getOrchidByID(int orchidID);
}
