package mostwanted.repository;

import mostwanted.domain.entities.Car;
import mostwanted.domain.entities.RaceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceEntryRepository extends JpaRepository<RaceEntry, Long> {
    RaceEntry findByCar(Car car);
}
