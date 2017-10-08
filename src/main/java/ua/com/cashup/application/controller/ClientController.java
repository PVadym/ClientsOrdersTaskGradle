package ua.com.cashup.application.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.cashup.application.entity.Client;
import ua.com.cashup.application.errors.ApplicationError;
import ua.com.cashup.application.service.ClientService;

import java.util.List;

/**
 * Created by Вадим on 05.10.2017.
 */
@RestController
public class ClientController {


    private ClientService clientService;

    private static final Logger LOGGER = Logger.getLogger(ClientController.class);

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(value = "/clients/add" , produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity createClient(@RequestBody Client client){
        LOGGER.info("Creating new Client: " + client);
        if (clientService.isExistClient(client)){

            LOGGER.error(String.format("Client with Taxpayer Identification Number %d already exist in DataBase", client.getTIN()));

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApplicationError(String.format("Client with Taxpayer Identification Number %s already exist in DataBase",
                            client.getTIN())));
        }
        Client newClient = clientService.save(client);

        LOGGER.info("Client successfully created: " + newClient);

        return ResponseEntity.ok(newClient);
    }

    @GetMapping(value = "/clients", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public List<Client> getAllClients(){

        LOGGER.info("Getting all clients.");

        return clientService.getAllClients();
    }

    @GetMapping(value = "/clients/{TIN}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity getClientByTIN(@PathVariable int TIN){

        LOGGER.info(String.format("Getting client with Taxpayer Identification Number %d",TIN));

        Client client = clientService.getClientByTIN(TIN);
        if (client == null){

            LOGGER.error(String.format("Client with Taxpayer Identification Number %d does not exist", TIN));

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApplicationError(String.format("Client with Taxpayer Identification Number %d does not exist", TIN)));
        }
        LOGGER.info(String.format("Client with Taxpayer Identification Number %d get successfully",TIN));

        return ResponseEntity.ok(client);
    }


    @GetMapping(value = "/clients/find", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity getClientByNameAndSurname(
            @RequestParam (value = "name", required = false) String name,
            @RequestParam (value = "surname", required = false) String surname){

        LOGGER.info(String.format("Getting client with name \"%s\" and surname \"%s\" ", name, surname));

        Client client = clientService.getClientByNameAndSurname(name, surname);
        if (client == null){

            LOGGER.error(String.format("Client with name \"%s\" and surname \"%s\" does not exist", name, surname));

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApplicationError(String.format("Client with name \"%s\" and surname \"%s\" does not exist", name, surname)));
        }
        LOGGER.info(String.format("Client with name \"%s\" and surname \"%s\" get successfully", name, surname));
        return ResponseEntity.ok(client);
    }

    @PostMapping(value = "clients/update", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity updateClient(@RequestBody Client client){

        LOGGER.info("Updating client : " + client);

        Client clientToUpdate = clientService.getClientById(client.getId());
        if (clientToUpdate == null){
            LOGGER.error(String.format("Client with id %d does not exist", client.getId()));

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApplicationError(String.format("Client with id %d does not exist", client.getId())));
        }
        clientService.edit(client);
        LOGGER.info("Client updated successfully: " + client);

        return ResponseEntity.ok(client);
    }
}
