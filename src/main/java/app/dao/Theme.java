package app.dao;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "theme",schema = "public")
@Getter
@Setter
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;

    @ManyToMany(mappedBy = "themes")
    private List<User> users;

    @OneToMany(mappedBy = "theme")
    private List<SubTopic> subTopics;

    @ManyToMany(mappedBy = "themes")
    private List<Word> words;
}
