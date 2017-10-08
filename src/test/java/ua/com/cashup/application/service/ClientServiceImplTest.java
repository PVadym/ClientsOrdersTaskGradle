package ua.com.cashup.application.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.enums.Gender;
import ua.com.cashup.application.repository.ClientRepository;

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
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client1;
    private Client client2;
    private List<Client> clients;

    @Before
    public void setUp() throws Exception {
        this.client1 = new Client("TestSurname1", "TestName1",new Date(), Gender.MALE, 11111, new ArrayList<>());
        this.client1.setId(1);
        this.client2 = new Client("TestSurname2", "TestName2",new Date(), Gender.FEMALE, 22222, new ArrayList<>());
        this.client2.setId(2);
        this.clients = new ArrayList<>();
        this.clients.add(client1);
        this.clients.add(client2);
    }

    @Test
    public void getAllClients() throws Exception {
        //prepare
        when(clientRepository.findAll()).thenReturn(clients);
        //testing and validate
        assertEquals(clientService.getAllClients(),clients);
        verify(clientRepository).findAll();

    }


    @Test
    public void getClientById() throws Exception {
        when(clientRepository.findOne(client1.getId())).thenReturn(client1);
        assertEquals(clientService.getClientById(client1.getId()), client1);
        when(clientRepository.findOne(anyLong())).thenReturn(null);
        clientService.getClientById(anyLong());

        verify(clientRepository,times(2)).findOne(anyLong());

    }

    @Test
    public void getClientByNameAndSurname() throws Exception {
        when(clientRepository.getByNameAndSurname(client1.getName(), client1.getSurname())).thenReturn(client1);
        when(clientRepository.getByNameAndSurname(client2.getName(), client2.getSurname())).thenReturn(client2);

        assertEquals(clientService.getClientByNameAndSurname(client1.getName(), client1.getSurname()), client1);
        clientService.getClientByNameAndSurname(client2.getName(), client2.getSurname());

        verify(clientRepository,times(2)).getByNameAndSurname(anyString(),anyString());

    }

    @Test
    public void getClientByTIN() throws Exception {

        when(clientRepository.getByTIN(client1.getTIN())).thenReturn(client1);
        when(clientRepository.getByTIN(client2.getTIN())).thenReturn(client2);
        assertEquals(clientService.getClientByTIN(client1.getTIN()), client1);
        clientService.getClientByTIN(client2.getTIN());

        verify(clientRepository,times(2)).getByTIN(anyInt());

    }

    @Test
    public void save() throws Exception {
        //prepare
        when(clientRepository.save(client1)).thenReturn(client1);
        //testing
        Client clientToTest = clientService.save(client1);
        //validate
        verify(clientRepository).save(client1);
        assertEquals(client1,clientToTest);


    }

    @Test
    public void edit() throws Exception {

        //prepare
        when(clientRepository.save(client1)).thenReturn(client1);
        //testing
        clientService.edit(client1);
        //validate
        verify(clientRepository).save(client1);


    }

    @Test
    public void isExistClient() throws Exception {

        when(clientRepository.getByTIN(11111)).thenReturn(client1);
        when(clientRepository.getByTIN(33333)).thenReturn(null);

        assertTrue(clientService.isExistClient(client1));

        Client newClient = new Client();
        newClient.setTIN(33333);
        clientService.isExistClient(newClient);

        verify(clientRepository).getByTIN(11111);
        verify(clientRepository).getByTIN(33333);
        verify(clientRepository,times(2)).getByTIN(anyInt());

    }

}