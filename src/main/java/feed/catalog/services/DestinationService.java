package feed.catalog.services;

import feed.catalog.api.response.dto.DestinationDTO;
import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import feed.catalog.api.response.utils.validator.annotations.implementations.DTOModelMapperImpl;
import feed.catalog.dao.DestinationDao;
import feed.catalog.domain.AuditDestination;
import feed.catalog.domain.Destination;
import feed.catalog.repositories.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DestinationService implements XService<Destination,String> {

    private static final String DELETE = "delete";


    private DestinationRepository destinationRepository;
    @Autowired
    private DestinationDao destinationDao;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }


    @Override
    public Destination create(Destination entity) {
        return null;
    }

    @Override
    public Optional<Destination> get(String id) {
        return destinationDao.getDestination(id);
    }


    public DestinationDTO getDTO(String clientId, String destinationName) {
        DestinationDTO destinationDTO = new DestinationDTO();
        Optional<Destination> destination = get(clientId + destinationName);
        if(!destination.isPresent())
        {
            throw new FeedCatalogInvocableException("Destination doesn't exist");
        }
        else {
            DTOModelMapperImpl.mapper(destination.get(), destinationDTO);
            return destinationDTO;
        }
    }

    public List<Destination> getAll() {
        return null;
    }

    public List<Destination> getAll(String clientId) {
        return destinationRepository.findByClient(clientId);
    }

    public List<DestinationDTO> getAllDTO(String clientId)
    {
        List<DestinationDTO> destinationDTOList = new ArrayList<>();
        List<Destination> destinations = getAll(clientId);
        if(destinations.isEmpty())
        {
            throw new FeedCatalogInvocableException(String.format("No Destinations configured for client %s",clientId));
        }
        else {
            for (Destination destination : destinations) {
                DestinationDTO destinationDTO = new DestinationDTO();
                DTOModelMapperImpl.mapper(destination, destinationDTO);
                destinationDTOList.add(destinationDTO);
            }
            return destinationDTOList;
        }

    }

    public Destination create(DestinationDTO entity, String clientId) {
        entity.setId(clientId + entity.getDestinationName());
        if (destinationDao.exists(entity.getId(), Destination.class))
        {
            throw new FeedCatalogInvocableException(String.format("Destination %s already exists for client: %s", entity.getDestinationName(), clientId));
        }
        else {
            if(entity.getDestinationDisplayName()==null)
                entity.setDestinationDisplayName(entity.getDestinationName());
            Destination destination = new Destination();
            DTOModelMapperImpl.mapper(entity, destination);
            destination.setClientId(clientId);
            return destinationDao.createDestination(destination, clientId);
        }
    }

    public Destination update(DestinationDTO entity, String clientId, String destinationName){

        if (!destinationDao.exists(clientId+destinationName, Destination.class))
        {
            throw new FeedCatalogInvocableException(String.format("Destination %s doesn't exists for client: %s", destinationName, clientId));
        }
        else {
            if (entity.getDestinationName()!=null) {
                throw new FeedCatalogInvocableException("Destination Name cannot be updated");
            } else {
                entity.setDestinationName(destinationName);
                Destination destination = get(clientId+destinationName).get();
                destination= DTOModelMapperImpl.mapper(entity, destination);
                destination.setClientId(clientId);
                destination.setId(clientId + destinationName);
                return destinationDao.updateDestination(destination);
            }
        }
    }

    public void delete(String clientId, String destinationName, String user)
    {
        if (!destinationDao.exists(clientId+destinationName,Destination.class))
        {
            throw new FeedCatalogInvocableException(String.format("No Destination %s exists for client: %s", destinationName, clientId));
        }
        else
        {
            DestinationDTO destinationDTO = getDTO(clientId,destinationName);
            AuditDestination auditDestination = new AuditDestination();
            DTOModelMapperImpl.mapper(destinationDTO, auditDestination);
            auditDestination.setId(UUID.randomUUID().toString());
            auditDestination.setClientId(clientId);
            auditDestination.setDestination_id(destinationDTO.getId());
            auditDestination.setOperation(DELETE);
            auditDestination.setUser(user);

            auditDestination = destinationDao.updateAudit(auditDestination);
            destinationDao.deleteDestination(auditDestination.getClientId() + auditDestination.getDestinationName());

        }

    }
}

