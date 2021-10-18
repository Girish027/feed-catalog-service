package feed.catalog.domain;

import com.datastax.driver.core.DataType;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import javax.persistence.Id;
import java.util.List;


@Data
@Entity
@Table("destination")
public class Destination {


    @Id
    @CassandraType(type = DataType.Name.VARCHAR)
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    @PrimaryKey
    private String id;

    @Indexed(value = "destination_by_client")
    private String clientId;

    private String destinationName;

    private String destinationDisplayName;

    private List<String> sinks;

    private String encryption;

}
