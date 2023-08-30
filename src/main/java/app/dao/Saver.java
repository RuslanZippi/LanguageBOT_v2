package app.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "save_word", schema = "public")
@Getter
@Setter
public class Saver {

    @Id
    private long id;

    @Column(name = "status_create_word")
    private boolean statusCreateWord;
    @Column(name = "status_theme")
    private boolean statusCreateTheme;
    @Column(name = "status_subtopic")
    private boolean statusCreateSubtopic;
    @Column(name = "editing_word")
    private boolean editingWord;
    @Column(name = "editing_theme")
    private boolean editingTheme;
    @Column(name = "editing_subtopic")
    private  boolean editingSubtopic;

    @Column(name = "status_create_with_theme")
    private boolean statusCreateWithTheme;

    private boolean editWordWithoutTheme;

    private String rusWord;
    private String engWord;

    private String themeName;
    private String subtopicName;

    private String idParentTheme;

    private Long wordToTheme;
    private Long wordToSubtopic;

    @OneToOne
    private User user;
}
