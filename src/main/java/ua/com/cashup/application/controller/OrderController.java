package ua.com.cashup.application.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.entity.Order;
import ua.com.cashup.application.errors.ApplicationError;
import ua.com.cashup.application.service.ClientService;
import ua.com.cashup.application.service.OrderService;

import java.util.List;

/**
 * Created by Вадим on 05.10.2017.
 */
@RestController
public class OrderController {

    private ClientService clientService;

    private OrderService orderService;

    private static final Logger LOGGER = Logger.getLogger(OrderController.class);

    @Autowired
    public OrderController(ClientService clientService, OrderService orderService) {
        this.clientService = clientService;
        this.orderService = orderService;
    }

    @PostMapping(value = "/orders/add/{TIN}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity createOrder(@PathVariable int TIN,
                                     @RequestBody Order order){
        LOGGER.info("Creating new Order: " + order);

        Client client = clientService.getClientByTIN(TIN);
        if (client == null){

            LOGGER.error(String.format("Client with Taxpayer Identification Number %d does not exist", TIN));

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApplicationError(String.format("Client with Taxpayer Identification Number %d does not exist", TIN)));
        }
        order.setClient(client);
        Order savedOrder = orderService.save(order);

        LOGGER.info("Order successfully created: " + savedOrder);

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping(value = "/orders", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public List<Order> getAllOrders(){

        LOGGER.info("Getting all orders.");

        return orderService.getAllOrders();
    }

    @GetMapping(value = "/orders/{TIN}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity getAllOrdersByClient(@PathVariable int TIN){

        LOGGER.info(String.format("Getting all orders for client with Taxpayer Identification Number %d",TIN));

        Client client = clientService.getClientByTIN(TIN);
        if (client == null){

            LOGGER.error(String.format("Client with Taxpayer Identification Number %d does not exist", TIN));

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApplicationError(String.format("Client with Taxpayer Identification Number %d does not exist", TIN)));
        }

        List<Order> orders = orderService.getAllOrdersByClient(client);

        LOGGER.info(String.format("All orders for client with Taxpayer Identification Number %d get successfully",TIN));

        return ResponseEntity.ok(orders);
    }

    @PostMapping(value = "/orders/confirm", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity confirmOrder(@RequestBody Order order ){

        LOGGER.info(String.format("Confirming order with id %d", order.getId()));
        Order orderFromDB = orderService.getById(order.getId());
        if (orderFromDB == null){

            LOGGER.error(String.format("Order with ID %d does not exist in DB", order.getId()));

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApplicationError(String.format("Order with ID %d does not exist in DB", order.getId())));
        }

        Order confirmedOrder = orderService.confirmOrder(order);
        LOGGER.info(String.format("Order with id %d confirm successfully.", order.getId()));
        return ResponseEntity.ok(confirmedOrder);

    }



}
