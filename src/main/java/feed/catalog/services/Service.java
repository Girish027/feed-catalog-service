package feed.catalog.services;

import feed.catalog.domain.Feed;

import java.util.List;
import java.util.Optional;

/**
 * Created by aditya.kumar on 22/07/19.
 */
public interface Service {

    List<Object> listAll();

    Optional<Feed> getById(String id);

    Object saveOrUpdate(Object object);

    void delete(String id);

    //Object saveOrUpdateProductForm(ProductForm productForm);
}
