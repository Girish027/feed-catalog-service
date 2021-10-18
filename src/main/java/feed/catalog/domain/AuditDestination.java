package feed.catalog.domain;

import com.datastax.oss.driver.api.mapper.annotations.Entity;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table("audit_destination")
public class AuditDestination
{
    @GeneratedValue
    @Id
    @PrimaryKey
    private String id;

    private String destination_id;

    private String clientId;

    private String destinationName;

    private String destinationDisplayName;

    private List<String> sinks;

    private String encryption;

    private LocalDateTime createdAt = LocalDateTime.now();;

    private String user;

    private String operation;


}
