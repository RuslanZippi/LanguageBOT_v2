package app.service;

import app.dao.User;
import app.dao.Word;
import app.dao.repository.UserInt;
import app.dao.repository.WordInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataBaseService {

    @Autowired
    private UserInt userInt;

    @Autowired
    private WordInt wordInt;

    public void saveUser(User user){
        userInt.save(user);
    }

    public int getCountWord(long chatID){
        User user = userInt.findById((int) chatID);
        int count =(int) wordInt.countWordsByUsersId(user.getId());
        return count;
    }
    public List<Word> getListWords(long chatId){
        List<Word> wordList = wordInt.findByUsersId((int)chatId);
        return wordList;
    }
}
