package softuni.xmllab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.xmllab.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
