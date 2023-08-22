package app.dao.repository;

import app.dao.SubTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtopicRep extends CrudRepository<SubTopic,Long> {

    long countSubtopicsByUsersId(long id);
    List<SubTopic> findByUsersId(long id);
    List<SubTopic> findByThemeId(long id);
    SubTopic findById(long id);
}
