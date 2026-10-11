package fu.se.chapter13orchid.repositories;

import fu.se.chapter13orchid.pojos.Orchid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrchidRepository extends JpaRepository<Orchid, Integer> {
}
