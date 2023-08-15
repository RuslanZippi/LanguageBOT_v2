package app.dao;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name ="user", schema = "public")
public class User {

    @Id
    private long id;

    @ManyToMany
    @JoinTable(name = "word_user",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "word_id"))
    private List<Word> words;

    @ManyToMany
    @JoinTable(name="theme_user",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "theme_id"))
    private List<Theme> themes;

    @ManyToMany
    @JoinTable(name = "subtopic_user",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "subtopic_id"))
    private List<SubTopic> topics;
}
