package ua.com.cashup.application.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.cashup.application.enums.Gender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Вадим on 07.10.2017.
 */
@RunWith(SpringRunner.class)
public class ClientTest {

    private Client client;

    @Before
    public void setUp() throws Exception {
        client = new Client("Pylypchenko", "Vadym",new Date(), Gender.MALE,11111,new ArrayList<>());
    }

    @Test
    public void toStringTest() throws Exception {

        String toString = client.toString();
        assertTrue(toString.contains("Pylypchenko")
                &&toString.contains("Vadym")
                &&toString.contains("MALE")
                &&toString.contains("11111"));
    }

    @Test
    public void getAndSetId() throws Exception {
        assertEquals(client.getId(), 0);
        client.setId(2);
        assertEquals(client.getId(), 2);
    }


    @Test
    public void getAndSetSurname() throws Exception {
        assertEquals(client.getSurname(), "Pylypchenko");
        client.setSurname("Test");
        assertEquals(client.getSurname(), "Test");

    }


    @Test
    public void getAndSetName() throws Exception {
        assertEquals(client.getName(), "Vadym");
        client.setName("Test");
        assertEquals(client.getName(), "Test");
    }

    @Test
    public void getAndSetBirthday() throws Exception {
        Date birthday = new Date();
        client.setBirthday(birthday);
        assertEquals(client.getBirthday(), birthday);

    }


    @Test
    public void getAndSetGender() throws Exception {

        assertEquals(client.getGender(), Gender.MALE);
        client.setGender(Gender.FEMALE);
        assertEquals(client.getGender(), Gender.FEMALE);

    }


    @Test
    public void getAndSetTIN() throws Exception {
        assertEquals(client.getTIN(), 11111);
        client.setTIN(22222);
        assertEquals(client.getTIN(), 22222);

    }

    @Test
    public void getAndSetOrders() throws Exception {

        assertEquals(client.getOrders().size(), 0);
        Order order = new Order();
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        client.setOrders(orderList);

        assertTrue(client.getOrders().contains(order));

    }

}