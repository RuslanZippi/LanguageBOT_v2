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
    @Column(name = "eng_translation")
    private String engTranslation;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "rus_translation")
    private String rusTranslation;

    @ManyToMany(mappedBy = "words")
    private List<User> users;

   /* @ManyToMany
    @JoinTable(name = "word_theme",
    joinColumns = @JoinColumn(name = "word_id"),
    inverseJoinColumns = @JoinColumn(name = "theme_id"))
    private List<Theme> themes;

    @ManyToMany
    @JoinTable(name = "word_subtopic",
    joinColumns = @JoinColumn(name = "word_id"),
    inverseJoinColumns = @JoinColumn(name = "subtopic_id"))
    private List<SubTopic> topics;
    */
}
