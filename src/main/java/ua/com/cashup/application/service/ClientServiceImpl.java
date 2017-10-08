package ua.com.cashup.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.repository.ClientRepository;

import java.util.List;

/**
 * Created by Вадим on 05.10.2017.
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository repository;

    @Override
    public List<Client> getAllClients() {
        return (List<Client>) repository.findAll();
    }

    @Override
    public Client getClientById(long id) {
        return repository.findOne(id);
    }

    @Override
    public Client getClientByNameAndSurname(String name, String surname) {
        return repository.getByNameAndSurname(name, surname);
    }

    @Override
    public Client getClientByTIN(int TIN) {
        return repository.getByTIN(TIN);
    }

    @Override
    public Client save(Client client) {
       return repository.save(client);

    }

    @Override
    public void edit(Client client) {
        repository.save(client);

    }

    @Override
    public boolean isExistClient(Client client) {
        return repository.getByTIN(client.getTIN()) != null;
    }
}
