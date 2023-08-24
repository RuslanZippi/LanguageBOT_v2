package app.dao.repository;

import app.dao.EditWord;
import app.dao.EditWordKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditWordRep  extends CrudRepository<EditWord, EditWordKey> {

    Optional<EditWord> findById(EditWordKey editWordKey);
}
