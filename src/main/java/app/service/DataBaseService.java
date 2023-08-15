package app.service;

import app.dao.SubTopic;
import app.dao.Theme;
import app.dao.User;
import app.dao.Word;
import app.dao.repository.SubtopicRep;
import app.dao.repository.ThemeRep;
import app.dao.repository.UserRep;
import app.dao.repository.WordRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataBaseService {

    @Autowired
    private UserRep userInt;

    @Autowired
    private WordRep wordInt;

    @Autowired
    private ThemeRep themeRep;

    @Autowired
    private SubtopicRep subtopicRep;
    public void saveUser(User user){
        userInt.save(user);
    }

    public long getCountWord(long chatID){
        User user = userInt.findById((int) chatID);
        long count =wordInt.countWordsByUsersId(user.getId());
        return count;
    }
    public List<Word> getListWords(long chatId){
        List<Word> wordList = wordInt.findByUsersId(chatId);
        return wordList;
    }

    public long getCountTheme(long chatId){
        return themeRep.countThemesByUsersId(chatId);
    }

    public long getCountSubtopic(long chatID) {
        return subtopicRep.countSubtopicsByUsersId(chatID);
    }

    public List<String> getRusWordList(long chatId){
        List<String> rusList = new ArrayList<>();
        List<Word> wordList = wordInt.findByUsersId(chatId);
        for (Word w : wordList){
            rusList.add(w.getRusTranslation());
        }
        return  rusList;
    }

    public List<String> getEngWordList(long chatId){
        List<String> engList = new ArrayList<>();
        List<Word> wordList = wordInt.findByUsersId(chatId);
        for (Word w : wordList){
            engList.add(w.getEngTranslation());
        }
        return  engList;
    }

    public List<String> getThemeNameList(long chatId){
        List<String> themeNameList = new ArrayList<>();
        List<Theme> themes = themeRep.findByUsersId(chatId);

        for (Theme t: themes){
            themeNameList.add(t.getName());
        }
        return themeNameList;
    }

    public  List<String> getSubtopicNameList(long chatId){
        List<String> subtopicNameList = new ArrayList<>();
        List<SubTopic> subTopics = subtopicRep.findByUsersId(chatId);

        for (SubTopic t: subTopics){
            subtopicNameList.add(t.getName());
        }
        return subtopicNameList;
    }
}
