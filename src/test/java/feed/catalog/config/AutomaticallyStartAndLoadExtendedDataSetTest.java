package feed.catalog.config;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.datastax.driver.core.ResultSet;
import org.junit.Rule;
import org.junit.Test;

public class AutomaticallyStartAndLoadExtendedDataSetTest {

   /* @Rule
    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("simple.cql","keyspaceNameToCreate"));

    @Test
    public void should_have_started_and_execute_cql_script() throws Exception {
        ResultSet result = cassandraCQLUnit.session.execute("select * from mytable WHERE id='myKey01'");
        assertThat(result.iterator().next().getString("value"), is("myValue01"));
    }*/

}