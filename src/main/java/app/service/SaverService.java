package app.service;

import app.dao.Word;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class SaverService {
    private boolean saveWord = false;
    private boolean saveTheme = false;
    private boolean saveSubtopic = false;

    private Word word =  new Word();

    public void setRusWord(String rusWord){
        word.setRusTranslation(rusWord);
    }
    public void setEngWord(String engWord){
        word.setEngTranslation(engWord);
    }
}
