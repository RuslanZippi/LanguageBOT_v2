package app.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "word", schema = "public")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "word_theme",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id"))
    private List<Theme> themes;

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "word_subtopic",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "subtopic_id"))
    private List<SubTopic> topics;

    @OneToMany(mappedBy = "word")
    private List<EditWord> editWords;


}
