package ua.com.cashup.application.service;

import ua.com.cashup.application.entity.Client;

import java.util.List;

/**
 * Created by Вадим on 04.10.2017.
 */
public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(long id);

    Client getClientByNameAndSurname(String name, String surname);

    Client getClientByTIN(int TIN);

    Client save(Client client);

    void edit(Client client);

    boolean isExistClient(Client client);
}
