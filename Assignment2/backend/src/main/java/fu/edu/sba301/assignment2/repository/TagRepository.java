package fu.edu.sba301.assignment2.repository;

import fu.edu.sba301.assignment2.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByTagNameIgnoreCase(String tagName);
    List<Tag> findByTagNameContainingIgnoreCase(String tagName);
}
