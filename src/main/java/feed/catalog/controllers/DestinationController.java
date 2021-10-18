package feed.catalog.controllers;

import feed.catalog.api.response.dto.DestinationDTO;
import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import feed.catalog.domain.Destination;
import feed.catalog.services.DestinationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(path = "/{clientId}/")
public class DestinationController {

    DestinationService destinationService;

    @Autowired
    public void setService(DestinationService service){
        destinationService = service;
    }

    @PostMapping(path = "destination" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<String> createDestination(@Valid @RequestBody DestinationDTO destinationDto, @PathVariable("clientId") String clientId) throws FeedCatalogInvocableException {
        log.info("Create Feed API called for client: " + clientId);
        Destination createdDestination = destinationService.create(destinationDto,clientId);
        return new Resource<String>("OK", ControllerLinkBuilder.linkTo(methodOn(this.getClass()).getDestination(createdDestination.getClientId(),createdDestination.getDestinationName())).withSelfRel());
    }

    @Transactional
    @PutMapping(path = "destination/{destinationName}" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Resource updateDestination(@RequestBody DestinationDTO destinationDto,@PathVariable("clientId") String clientId, @PathVariable("destinationName") String destinationName ) throws FeedCatalogInvocableException{
        Destination editedDestination = destinationService.update(destinationDto, clientId, destinationName);
        return new Resource(editedDestination.getDestinationName(), ControllerLinkBuilder.linkTo(methodOn(this.getClass()).getDestination(editedDestination.getClientId(),editedDestination.getDestinationName())).withSelfRel());
    }


    @GetMapping(path = "destination/{destinationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getDestination(@PathVariable("clientId") String clientId, @PathVariable("destinationName") String destinationName) throws FeedCatalogInvocableException{
        DestinationDTO destinationDto= destinationService.getDTO(clientId , destinationName);
        return ResponseEntity.status(HttpStatus.OK).body(destinationDto);
    }

    @GetMapping(path = "destinations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllDestinations(@PathVariable("clientId") String clientId){
        List<DestinationDTO> destinationDTOList = destinationService.getAllDTO(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(destinationDTOList);
    }

    @Transactional
    @PutMapping(path = "destination/{destinationName}/delete" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Resource deleteDestination(@PathVariable("clientId") String clientId, @PathVariable("destinationName") String destinationName ) throws FeedCatalogInvocableException {
        destinationService.delete(clientId, destinationName, "API");
        return new Resource("Deleted " + destinationName + " for Client " + clientId );
    }


}
