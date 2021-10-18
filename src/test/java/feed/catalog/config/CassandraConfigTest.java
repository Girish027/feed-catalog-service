package feed.catalog.config;

import com.datastax.driver.core.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.test.context.ActiveProfiles;

/*@Configuration
@PropertySource("classpath:application.properties")
@EmbeddedCassandra
@ActiveProfiles(profiles = "test")
@CassandraDataSet(value = "test.cql", keyspace = "local_test")*/
public class CassandraConfigTest  {
    @Value("${service.cassandra.keyspace}")
    public String keyspace;
    private Logger logger = LoggerFactory.getLogger(CassandraConfigTest.class);
    @Bean
    public CassandraClusterFactoryBean cluster() {
        try {
           /* EmbeddedCassandraServerHelper.startEmbeddedCassandra(EmbeddedCassandraServerHelper.DEFAULT_CASSANDRA_YML_FILE, 1000000L);
            EmbeddedCassandraServerHelper.getCluster().getConfiguration().getSocketOptions().setReadTimeoutMillis(1000000);
            Session session = EmbeddedCassandraServerHelper.getSession();
            CQLDataLoader dataLoader = new CQLDataLoader(session);
            dataLoader.load(new FileCQLDataSet("test.cql", true, false, getKeyspaceName()));*/
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Can't start Embedded Cassandra", e);
        }
        return new CassandraClusterFactoryBean();
    }
    //@Override
    protected String getKeyspaceName() {
        return keyspace;
    }
    //@Override
    protected int getPort() {
        return 9142;
    }
}