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
import ua.com.cashup.application.enums.Gender;
import ua.com.cashup.application.service.ClientService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by Вадим on 07.10.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = ClientController.class)
public class ClientControllerTest {

    @MockBean
    private ClientService clientService;

    @Autowired
    private MockMvc mockMvc;

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
    public void getUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().is(401));

    }

    @Test
    @WithMockUser
    public void createClient() throws Exception {
        when(clientService.save(any(Client.class))).thenReturn(client1);

        mockMvc.perform(post("/clients/add")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client1))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.surname", is("TestSurname1")))
                .andExpect(jsonPath("$.name", is("TestName1")))
                .andExpect(jsonPath("$.tin", is(11111)));

        verify(clientService, times(1)).save(any(Client.class));
        verify(clientService, times(1)).isExistClient(any(Client.class));
    }

    @Test
    @WithMockUser
    public void getAllClients() throws Exception {
        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].surname", is("TestSurname1")))
                .andExpect(jsonPath("$[0].name", is("TestName1")))
                .andExpect(jsonPath("$[0].tin", is(11111)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].surname", is("TestSurname2")))
                .andExpect(jsonPath("$[1].name", is("TestName2")))
                .andExpect(jsonPath("$[1].tin", is(22222)));

        verify(clientService, times(1)).getAllClients();
        verifyNoMoreInteractions(clientService);

    }

    @Test
    @WithMockUser
    public void getClientByTIN() throws Exception {

        when(clientService.getClientByTIN(client1.getTIN())).thenReturn(client1);

        mockMvc.perform(get("/clients/{id}", client1.getTIN()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.surname", is("TestSurname1")))
                .andExpect(jsonPath("$.name", is("TestName1")))
                .andExpect(jsonPath("$.tin", is(11111)));

        verify(clientService, times(1)).getClientByTIN(11111);
        verifyNoMoreInteractions(clientService);

    }

    @Test
    @WithMockUser
    public void getClientByNameAndSurname() throws Exception {

        when(clientService.getClientByNameAndSurname(client1.getName(), client1.getSurname())).thenReturn(client1);

        mockMvc.perform(get("/clients/find")
                .param("name", "TestName1")
                .param("surname", "TestSurname1"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.surname", is("TestSurname1")))
                .andExpect(jsonPath("$.name", is("TestName1")))
                .andExpect(jsonPath("$.tin", is(11111)));

        verify(clientService, times(1)).getClientByNameAndSurname(client1.getName(),client1.getSurname());
        verifyNoMoreInteractions(clientService);

    }

    @Test
    @WithMockUser
    public void updateClient() throws Exception {


        when(clientService.getClientById(client1.getId())).thenReturn(client1);

        mockMvc.perform(post("/clients/update")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client1))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.surname", is("TestSurname1")))
                .andExpect(jsonPath("$.name", is("TestName1")))
                .andExpect(jsonPath("$.tin", is(11111)));

        verify(clientService, times(1)).edit(any(Client.class));

    }

    @Test
    @WithMockUser
    public void getClientByTIN_NOT_FOUND() throws Exception {

        when(clientService.getClientByTIN(anyInt())).thenReturn(null);

        mockMvc.perform(get("/clients/{id}", client1.getTIN()))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).getClientByTIN(anyInt());
        verifyNoMoreInteractions(clientService);

    }

    @Test
    @WithMockUser
    public void createClient_CONFLICT() throws Exception {
        when(clientService.isExistClient(any(Client.class))).thenReturn(true);

        mockMvc.perform(post("/clients/add")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client1))
        )
                .andExpect(status().isConflict());

        verify(clientService, never()).save(any(Client.class));
        verify(clientService, times(1)).isExistClient(any(Client.class));
    }


}