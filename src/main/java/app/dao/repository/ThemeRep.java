package app.dao.repository;

import app.dao.Theme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRep extends CrudRepository<Theme, Long> {

    long countThemesByUsersId(long id);
    List<Theme> findByUsersId(long id);
    Theme findById(long id);
}
