package feed.catalog.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by aditya.kumar on 22/07/19.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.cassandra")
public class CassandraConfig extends AbstractCassandraConfiguration {

    /**
     * Keyspace name to use.
     */
    private String keyspaceName;

    /**
     * Schema action to take at startup.
     */
    private SchemaAction schemaAction;


    /**
     * Cluster node addresses.
     */
    private String contactPoints;


    @Override protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return schemaAction;
    }


    //Remove the auto creation of keyspace and table
    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keyspaceName).ifNotExists(Boolean.TRUE);
        return Arrays.asList(specification);
    }

    /*@Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return Arrays.asList(DropKeyspaceSpecification.dropKeyspace(KEYSPACE));
    }*/

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"feed.catalog.domain"};
    }
}
