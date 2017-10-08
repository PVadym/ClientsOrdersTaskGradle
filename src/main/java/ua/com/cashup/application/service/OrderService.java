package ua.com.cashup.application.service;

import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.entity.Order;

import java.util.List;

/**
 * Created by Вадим on 04.10.2017.
 */
public interface OrderService {

    List<Order> getAllOrders();

    List<Order> getAllOrdersByClient(Client client);

    Order getById(long id);

    Order save(Order order);

    Order confirmOrder(Order order);
}
