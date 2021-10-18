package feed.catalog.dao;

import feed.catalog.domain.AuditDestination;
import feed.catalog.domain.Destination;

import java.util.Optional;


public interface DestinationDao {
    Destination createDestination(Destination destination, String client);

    Optional<Destination> getDestination(String id);

    Destination updateDestination(Destination entity);

    boolean exists(String filter, Class<?> claz);

    void deleteDestination(String id);

    AuditDestination updateAudit(AuditDestination auditDestination);

}
