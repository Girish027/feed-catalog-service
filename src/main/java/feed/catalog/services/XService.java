package feed.catalog.services;

import java.util.List;
import java.util.Optional;

public interface XService<T,ID> {

    T create(T entity);

    <T> Optional<T> get(ID id);

    List<T> getAll();
}
