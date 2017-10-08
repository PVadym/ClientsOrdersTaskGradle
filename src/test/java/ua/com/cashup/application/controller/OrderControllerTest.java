package ua.com.cashup.application.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.cashup.application.TestUtil;
import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.entity.Order;
import ua.com.cashup.application.enums.Currency;
import ua.com.cashup.application.enums.Gender;
import ua.com.cashup.application.service.ClientService;
import ua.com.cashup.application.service.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Вадим on 07.10.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = OrderController.class)
@WithMockUser
public class OrderControllerTest {

    @MockBean
    private ClientService clientService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

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
    public void createOrder() throws Exception {

        when(clientService.getClientByTIN(client.getTIN())).thenReturn(client);
        when(orderService.save(any(Order.class))).thenReturn(order1);

        mockMvc.perform(post("/orders/add/{TIN}",client.getTIN())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order1))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.amount", is(100.0)));

        verify(orderService, times(1)).save(any(Order.class));
        verify(clientService, times(1)).getClientByTIN(client.getTIN());

    }

    @Test
    public void createOrder_NOT_FOUND() throws Exception {

        when(clientService.getClientByTIN(client.getTIN())).thenReturn(null);

        mockMvc.perform(post("/orders/add/{TIN}",client.getTIN())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order1))
        )
                .andExpect(status().isNotFound());

        verify(orderService, never()).save(any(Order.class));
        verify(clientService, times(1)).getClientByTIN(client.getTIN());

    }

    @Test
    public void getAllOrders() throws Exception {

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is("ok")))
                .andExpect(jsonPath("$[0].amount", is(100.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].status", is("ok")))
                .andExpect(jsonPath("$[1].amount", is(200.0)));

        verify(orderService, times(1)).getAllOrders();
        verifyNoMoreInteractions(orderService);

    }

    @Test
    public void getAllOrdersByClient() throws Exception {

        when(orderService.getAllOrdersByClient(client)).thenReturn(orders);
        when(clientService.getClientByTIN(client.getTIN())).thenReturn(client);

        mockMvc.perform(get("/orders/{TIN}", client.getTIN()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is("ok")))
                .andExpect(jsonPath("$[0].amount", is(100.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].status", is("ok")))
                .andExpect(jsonPath("$[1].amount", is(200.0)));

        verify(orderService, times(1)).getAllOrdersByClient(client);
        verify(clientService, times(1)).getClientByTIN(client.getTIN());
        verifyNoMoreInteractions(orderService);

    }

    @Test
    public void getAllOrdersByClient_NOT_FOUND() throws Exception {

        when(orderService.getAllOrdersByClient(client)).thenReturn(orders);
        when(clientService.getClientByTIN(anyInt())).thenReturn(null);

        mockMvc.perform(get("/orders/{TIN}", client.getTIN()))
                .andExpect(status().isNotFound());

        verify(orderService, never()).getAllOrdersByClient(client);
        verify(clientService, times(1)).getClientByTIN(client.getTIN());
        verifyNoMoreInteractions(orderService);

    }

    @Test
    public void confirmOrder() throws Exception {

        when(orderService.getById(order1.getId())).thenReturn(order1);
        when(orderService.confirmOrder(any(Order.class))).thenReturn(order1);

        mockMvc.perform(post("/orders/confirm")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order1))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.amount", is(100.0)));

        verify(orderService, times(1)).confirmOrder(any(Order.class));
        verify(orderService, times(1)).getById(order1.getId());

    }

    @Test
    public void confirmOrder_NOT_FOUND() throws Exception {

        when(orderService.getById(order1.getId())).thenReturn(null);

        mockMvc.perform(post("/orders/confirm")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order1))
        )
                .andExpect(status().isNotFound());

        verify(orderService, never()).confirmOrder(any(Order.class));
        verify(orderService, times(1)).getById(order1.getId());

    }

}