package feed.catalog.repositories;

import feed.catalog.domain.Destination;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends  CassandraRepository<Destination,String> {

    @Query(value = "SELECT * FROM destination WHERE clientId=?0",allowFiltering = true)
    List<Destination> findByClient(@Param("clientId") String client);

}