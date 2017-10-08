package ua.com.cashup.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.entity.Order;
import ua.com.cashup.application.repository.OrderRepository;

import java.util.List;

/**
 * Created by Вадим on 05.10.2017.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository repository;

    @Override
    public List<Order> getAllOrders() {
        return (List<Order>) repository.findAll();
    }

    @Override
    public List<Order> getAllOrdersByClient(Client client) {
        return repository.getAllByClient(client);
    }

    @Override
    public Order getById(long id) {
        return repository.findOne(id);
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);

    }

    @Override
    public Order confirmOrder(Order order) {
        Order orderToConfirm = repository.findOne(order.getId());
        orderToConfirm.setConfirmation(true);
        return repository.save(orderToConfirm);

    }
}
