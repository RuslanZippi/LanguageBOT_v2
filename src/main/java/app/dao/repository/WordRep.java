package app.dao.repository;

import app.dao.SubTopic;
import app.dao.Theme;
import app.dao.Word;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRep extends CrudRepository<Word,Long> {

    Word findById(long id);
    Word findByEngTranslation(String engTranslation);
    List<Word> findByUsersId(long id);
    List<Word> findByThemesId(long themeId);
    List<Word> findByTopicsId(long subtopicId);
    long countWordsByUsersId(long id);

}
