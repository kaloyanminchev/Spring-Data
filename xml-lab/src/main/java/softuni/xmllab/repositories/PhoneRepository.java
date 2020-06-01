package softuni.xmllab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.xmllab.domain.entities.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Phone findByPhoneNumber(String phoneNumber);
}
