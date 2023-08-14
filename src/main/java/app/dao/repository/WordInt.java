package app.dao.repository;

import app.dao.SubTopic;
import app.dao.Theme;
import app.dao.Word;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordInt extends CrudRepository<Word,Long> {

    Word findById(long id);
    Word findByName(String name);
    List<Word> findByUsersId(int id);
    List<Theme> findByThemesId(int themeId);
    List<SubTopic> findByTopicsId(int subtopicId);
    long countWordsByUsersId(int id);
}
