package app.dao.repository;

import app.dao.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRep extends CrudRepository<User, Long> {

   User findById(long id);
}
