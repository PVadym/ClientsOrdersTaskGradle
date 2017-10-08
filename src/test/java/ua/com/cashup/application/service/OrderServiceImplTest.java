package ua.com.cashup.application.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.entity.Order;
import ua.com.cashup.application.enums.Currency;
import ua.com.cashup.application.enums.Gender;
import ua.com.cashup.application.repository.ClientRepository;
import ua.com.cashup.application.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Created by Вадим on 07.10.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client client;
    private Order order1;
    private Order order2;
    private List<Order> orders;

    @Before
    public void setUp() throws Exception {
        this.order1 = new Order(new Date(),"ok",100.0, Currency.EUR,false);
        this.order1.setId(1);
        this.order2 = new Order(new Date(),"ok",200.0, Currency.UAH,false);
        this.order2.setId(2);
        this.orders = new ArrayList<>();
        this.orders.add(order1);
        this.orders.add(order2);
        this.client = new Client("TestSurname", "TestName",new Date(), Gender.MALE, 11111, orders);
        this.client.setId(1);

    }

    @Test
    public void getAllOrders() throws Exception {
        //prepare
        when(orderRepository.findAll()).thenReturn(orders);
        //testing and validate
        assertEquals(orderService.getAllOrders(),orders);
        verify(orderRepository).findAll();

    }

    @Test
    public void getAllOrdersByClient() throws Exception {
        //prepare
        when(orderRepository.getAllByClient(client)).thenReturn(orders);
        //testing and validate
        assertEquals(orderService.getAllOrdersByClient(client),orders);
        verify(orderRepository).getAllByClient(client);

    }

    @Test
    public void getById() throws Exception {

        when(orderRepository.findOne(order1.getId())).thenReturn(order1);
        assertEquals(orderService.getById(order1.getId()), order1);
        when(orderRepository.findOne(order2.getId())).thenReturn(order2);
        assertEquals(order2,orderService.getById(2));
        orderService.getById(2);

        verify(orderRepository,times(3)).findOne(anyLong());

    }

    @Test
    public void save() throws Exception {

        //prepare
        when(orderRepository.save(order1)).thenReturn(order1);
        //testing
        Order order = orderService.save(order1);
        //validate
        verify(orderRepository).save(order1);
        assertEquals(order1,order);

    }

    @Test
    public void confirmOrder() throws Exception {

        //prepare
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.findOne(1L)).thenReturn(order1);
        //testing
        orderService.confirmOrder(order1);
        //validate
        verify(orderRepository).save(order1);
        verify(orderRepository).findOne(1L);

        assertTrue(order1.isConfirmation());

    }

}