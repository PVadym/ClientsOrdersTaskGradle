package ua.com.cashup.application.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ua.com.cashup.application.entity.Client;

/**
 * Created by Вадим on 04.10.2017.
 */
public interface ClientRepository extends CrudRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.name LIKE :name AND c.surname LIKE :surname")
    public Client getByNameAndSurname(@Param("name") String name, @Param("surname") String surname);

    @Query("SELECT c FROM Client c WHERE c.TIN =:TIN")
    public Client getByTIN(@Param("TIN") int TIN);

}
