package com.fpt.sba301.slot20.repositories;

import com.fpt.sba301.slot20.pojos.Orchid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrchidRepository extends JpaRepository<Orchid, Integer> {
}
