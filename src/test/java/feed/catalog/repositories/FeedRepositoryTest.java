package feed.catalog.repositories;

import feed.catalog.domain.Feed;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings(value="unchecked")
public class FeedRepositoryTest {

    @Mock
    private FeedRepository feedRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testPersistence() {

        Feed feed = new Feed();
        feed.setId("id1234");
        feed.setFeedName("Optus client");
        feedRepository.save(feed);

        //then
        Assert.assertNotNull(feed.getId());
    }
}