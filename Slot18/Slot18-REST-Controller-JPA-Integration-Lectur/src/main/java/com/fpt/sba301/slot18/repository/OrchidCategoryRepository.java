package com.fpt.sba301.slot18.repository;

import com.fpt.sba301.slot18.entity.OrchidCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrchidCategoryRepository extends JpaRepository<OrchidCategory, Long> {
}
