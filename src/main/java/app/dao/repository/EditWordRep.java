package app.dao.repository;

import app.dao.EditWord;
import app.dao.EditWordKey;
import app.dao.User;
import app.dao.Word;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EditWordRep  extends CrudRepository<EditWord, EditWordKey> {

    EditWord findByUserAndWord(User user, Word word);
    List<EditWord> findByUser(User user);
}
