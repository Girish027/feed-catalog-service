package feed.catalog.domain;

import com.datastax.driver.core.DataType;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table("audit")
public class Audit
{
    @Id
    @GeneratedValue
    @CassandraType(type = DataType.Name.UUID )
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    @PrimaryKey
    final private UUID id = UUID.randomUUID();

    private String deletedBy;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    private String domain;

    private String operationType;

    @CassandraType(type = DataType.Name.TEXT)
    private String entry;


}
