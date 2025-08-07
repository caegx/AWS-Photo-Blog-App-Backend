package spring.cloud.repositories;


import org.springframework.data.repository.CrudRepository;
import spring.cloud.entities.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}