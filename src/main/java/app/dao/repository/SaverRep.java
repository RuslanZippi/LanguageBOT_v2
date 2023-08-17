package app.dao.repository;

import app.dao.Saver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaverRep  extends CrudRepository<Saver,Long> {

    Saver findByUserId(long id);

}
