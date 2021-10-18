package feed.catalog.dao;

import feed.catalog.api.response.utils.Template;
import feed.catalog.domain.AuditDestination;
import feed.catalog.domain.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DestinationDaoImpl implements DestinationDao {

    @Autowired
    private Template template;

    @Override
    public Destination createDestination(Destination destination,String client) {
        return template.create(destination);
    }

    @Override
    public Optional<Destination> getDestination(String id) {
        return template.findById(id, Destination.class);
    }

    @Override
    public Destination updateDestination(Destination entity) {
        return template.update(entity);
    }

    public boolean exists(String filter ,Class<?> claz){
        return template.exists(filter,claz);
    }

    @Override
    public void deleteDestination(String id) {
        template.deleteById(id,Destination.class);
    }

    @Override
    public AuditDestination updateAudit(AuditDestination auditDestination)
    {
        return template.create(auditDestination);
    }

}
