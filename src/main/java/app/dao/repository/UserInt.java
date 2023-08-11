package app.dao.repository;

import app.dao.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInt extends CrudRepository<User, Integer> {

    List<User> findByUserId(int userId);
}
