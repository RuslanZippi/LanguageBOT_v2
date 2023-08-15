package app.dao;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "subtopic",schema = "public")
public class SubTopic {

    @Id
    private int id;

    private String name;

    @ManyToMany
    @JoinTable(name = "subtopic_user",
    joinColumns = @JoinColumn(name = "subtopic_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "theme")
    private Theme theme;

    @ManyToMany
    @JoinTable(name = "word_subtopic",
    joinColumns = @JoinColumn(name = "subtopic_id"),
    inverseJoinColumns = @JoinColumn(name = "word_id"))
    private List<Word> words;
}
