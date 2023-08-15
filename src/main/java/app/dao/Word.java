package app.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "word", schema = "public")
public class Word {

    @Id
    private long id;

    @NotNull
    private String engTranslation;

    private String description;

    @NotNull
    private String rusTranslation;

    @ManyToMany
    @JoinTable(name = "word_user",
    joinColumns = @JoinColumn(name = "word_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @ManyToMany
    @JoinTable(name = "word_theme",
    joinColumns = @JoinColumn(name = "word_id"),
    inverseJoinColumns = @JoinColumn(name = "theme_id"))
    private List<Theme> themes;

    @ManyToMany
    @JoinTable(name = "word_subtopic",
    joinColumns = @JoinColumn(name = "word_id"),
    inverseJoinColumns = @JoinColumn(name = "subtopic_id"))
    private List<SubTopic> topics;
}
