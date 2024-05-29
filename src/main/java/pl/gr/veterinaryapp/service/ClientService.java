package pl.gr.veterinaryapp.service;

import pl.gr.veterinaryapp.model.dto.ClientRequestDto;
import pl.gr.veterinaryapp.model.entity.Client;

import java.util.List;

public interface ClientService {

    Client getClientById(Long id);

    Client createClient(ClientRequestDto clientRequestDTO);

    void deleteClient(Long id);

    List<Client> getAllClients();
}
