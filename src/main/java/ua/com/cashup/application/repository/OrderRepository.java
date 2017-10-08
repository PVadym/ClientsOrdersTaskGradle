package ua.com.cashup.application.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.entity.Order;

import java.util.List;

/**
 * Created by Вадим on 04.10.2017.
 */
public interface OrderRepository extends CrudRepository<Order, Long> {
    public List<Order> getAllByClient(Client client);
}
