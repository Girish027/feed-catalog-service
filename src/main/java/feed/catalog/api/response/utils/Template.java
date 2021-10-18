package feed.catalog.api.response.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import feed.catalog.domain.Audit;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Template {

    final static ObjectMapper objectMapper = new ObjectMapper();

    public Template(){
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS,false);
    }

    @Autowired
    private CassandraOperations operations;

    @Timed
    @ExceptionHandler
    public <T> T create(T entity) {
        operations.insert(entity);
        return entity;
    }

    @Timed
    @ExceptionHandler
    public <T> T create(T entity, Class<T> claz) {
        throw new RuntimeException("No data access for this layer");
    }

    @Timed
    @ExceptionHandler
    public <T> List<T> createList(List<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities must not be null");
        List<T> result = new ArrayList<>();
        for (T entity : entities) {
            operations.insert(entity);
            result.add(entity);
        }
        return result;
    }

    @Timed
    @ExceptionHandler
    public <T> T update(T entity) {
        operations.update(entity);
        return entity;
    }

    @Timed
    @ExceptionHandler
    public <T> Optional<T> findById(Object id, Class<T> claz) {
        return Optional.ofNullable(operations.selectOneById(id, claz));
    }

    @Timed
    @ExceptionHandler
    public <T> void deleteById(Object id, Class<T> claz) {
        operations.deleteById(id , claz);
    }

    @Timed
    @ExceptionHandler
    public void delete(Object entity) {
        operations.delete(entity);
    }

    @Timed
    @ExceptionHandler
    public <T> void delete(List<T> entities) {
        operations.delete(entities);
    }

    @Timed
    @ExceptionHandler
    public <T> void deleteAll(Class<T> claz) {
        throw new FeedCatalogInvocableException("Cannot perform the requested action",new Throwable());
    }

    @Timed
    @ExceptionHandler
    public <T> List<T> findAll(Class<T> claz) {
        throw new FeedCatalogInvocableException("Change method: handle multiple cassandra repository");
    }

    @Timed
    @ExceptionHandler
    public <T> List<T> findAll(List<Object> ids, Class<T> claz) {
        throw new FeedCatalogInvocableException("Cannot perform the requested action",new Throwable());
    }

    @Timed
    @ExceptionHandler
    public <T> void truncate(Class<T> claz) {
        throw new FeedCatalogInvocableException("Cannot perform the requested action",new Throwable());
    }

    @Timed
    @ExceptionHandler
    public <T> long getCount(Class<T> claz) {
        return operations.count(claz);
    }

    @Timed
    @ExceptionHandler
    @Lazy
    public <T> boolean exists(Object id, Class<T> claz) {
        return operations.exists(id ,claz);
    }

    @Timed
    @ExceptionHandler
    private <T> T audit(T entity,String operation) {
        Audit audit = new Audit();
        audit.setOperationType(operation);
        audit.setDomain(entity.getClass().getName());
        audit.setEntry(entity.toString());
        operations.insert(audit);
        return entity;
    }

}

