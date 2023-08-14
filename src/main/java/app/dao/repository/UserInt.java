package app.dao.repository;

import app.dao.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInt extends CrudRepository<User, Integer> {

   User findById(int id);
}
