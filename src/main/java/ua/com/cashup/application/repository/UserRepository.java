package ua.com.cashup.application.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.cashup.application.entity.User;

/**
 * Created by Вадим on 06.10.2017.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
