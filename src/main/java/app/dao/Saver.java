package app.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "save_word", schema = "public")
@Data
public class Saver {

    @Id
    private long id;

    private boolean status;

    private String rusWord;
    private String engWord;

    @OneToOne
    private User user;
}
