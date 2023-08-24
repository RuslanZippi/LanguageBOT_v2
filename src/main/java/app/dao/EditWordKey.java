package app.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class EditWordKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "word_id")
    private Long wordId;

}
