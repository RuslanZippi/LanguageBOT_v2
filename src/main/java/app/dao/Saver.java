package app.dao;

import jakarta.persistence.*;
import lombok.Data;
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

    private String rusWord;
    private String engWord;

    private String themeName;
    private String subtopicName;

    @OneToOne
    private User user;
}
