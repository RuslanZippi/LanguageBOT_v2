package app.dao.repository;

import app.dao.Word;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordInt extends CrudRepository<Word,Long> {

    Word findById();
    List<Word> findAllByName(String name);

}
