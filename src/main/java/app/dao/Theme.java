package app.dao;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "theme",schema = "public")
@Data
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String name;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "theme_user",
    joinColumns = @JoinColumn(name = "theme_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(mappedBy = "theme")
    private List<SubTopic> subTopics;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "word_theme",
    joinColumns = @JoinColumn(name = "theme_id"),
    inverseJoinColumns = @JoinColumn(name = "word_id"))
    private List<Word> words;
}
