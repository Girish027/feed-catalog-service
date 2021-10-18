package feed.catalog.domain;

import com.datastax.driver.core.DataType;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by aditya.kumar on 22/07/19.
 */

@Data
@Entity
@Table("feeds")
public class Feed implements Serializable {
    @JsonCreator
    public Feed(){}

    @Id
    @CassandraType(type = DataType.Name.VARCHAR)
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    @PrimaryKey
    private String id;

    @CassandraType(type = DataType.Name.TINYINT)
    @Indexed(value = "feeds_by_deleted")
    private int isDeleted = 0;

    @Indexed(value = "feedName")
    private String feedName;

    private String description;

    @Indexed(value = "feed_by_client")
    private String clientId;

    private boolean active;

    private String compressionConfig; // compression config

    private String platform; // source config

    private String scheduleConfig; // schedule config

    private String exportFormat; // export format config

    private LocalDateTime createdAt ;

    private LocalDateTime editedAt ;

    @Indexed(value = "feed_by_destination")
    private String destinationName;

    @CassandraType(type = DataType.Name.LIST , typeArguments = DataType.Name.VARCHAR)
    private List<String> reportList;

    @Override
    public String toString() {
        return "{ \"Feed\" : {" +
                "\n\t \"feedName\":\""  + feedName + "\"" +
                ",\n\t \"id\"" +":\""  + id + "\"" +
                ",\n\t \"description\"" +":\""  + description + "\"" +
                ",\n\t \"clientId\"" +":\""  + clientId + "\"" +
                ",\n\t \"isActive\"" +":\""  + active + "\"" +
                ",\n\t \"isCompressed\"" +":\""  + compressionConfig + "\"" +
                ",\n\t \"platform\"" +":\""  + platform + "\"" +
                ",\n\t \"isAdhoc\"" +":\""  + scheduleConfig + "\"" +
                ",\n\t \"createdAt\"" +":\""  + createdAt + "\"" +
                ",\n\t \"editedAt\"" +":\""  + editedAt + "\"" +
                ",\n\t \"format\"" +":\""  + exportFormat + "\"" +
                ",\n\t \"reportList\"" +":\""  + reportList + "\"" +
                "}" +
                "\n\t}";
    }
}
