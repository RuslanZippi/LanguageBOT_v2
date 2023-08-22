package app.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Message {

    @Id
    private long id;

    private Long idLastMessage;
    private Long idLastMessageUser;
}
