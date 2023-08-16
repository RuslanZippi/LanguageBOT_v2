package app.service;

import app.dao.*;
import app.dao.repository.*;
import jakarta.transaction.Transactional;
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

    @Autowired
    private SaverRep saverRep;

    public void saveUser(User user) {
        userInt.save(user);
    }

    public void checkUser(long chatID) {
        User user;
        Saver saver;
        if (userInt.findById(chatID) == null) {
            user = new User();
            user.setId(chatID);
            userInt.save(user);
            saver = new Saver();
            saver.setUser(user);
            saver.setId(user.getId() + 1);
            saver.setStatus(false);
            saverRep.save(saver);
        }
        //saverRep.save(saver);
    }

    public long getCountWord(long chatID) {
        User user = userInt.findById((int) chatID);
        long count = wordInt.countWordsByUsersId(user.getId());
        return count;
    }

    public List<Word> getListWords(long chatId) {
        List<Word> wordList = wordInt.findByUsersId(chatId);
        return wordList;
    }

    public long getCountTheme(long chatId) {
        return themeRep.countThemesByUsersId(chatId);
    }

    public long getCountSubtopic(long chatID) {
        return subtopicRep.countSubtopicsByUsersId(chatID);
    }

    public List<String> getRusWordList(long chatId) {
        List<String> rusList = new ArrayList<>();
        List<Word> wordList = wordInt.findByUsersId(chatId);
        for (Word w : wordList) {
            rusList.add(w.getRusTranslation());
        }
        return rusList;
    }

    public List<String> getEngWordList(long chatId) {
        List<String> engList = new ArrayList<>();
        List<Word> wordList = wordInt.findByUsersId(chatId);
        for (Word w : wordList) {
            engList.add(w.getEngTranslation());
        }
        return engList;
    }

    public List<String> getThemeNameList(long chatId) {
        List<String> themeNameList = new ArrayList<>();
        List<Theme> themes = themeRep.findByUsersId(chatId);

        for (Theme t : themes) {
            themeNameList.add(t.getName());
        }
        return themeNameList;
    }

    public List<String> getSubtopicNameList(long chatId) {
        List<String> subtopicNameList = new ArrayList<>();
        List<SubTopic> subTopics = subtopicRep.findByUsersId(chatId);

        for (SubTopic t : subTopics) {
            subtopicNameList.add(t.getName());
        }
        return subtopicNameList;
    }

    public boolean isCreated(long chatId) {
        boolean checker = saverRep.findByUserId(chatId).isStatus();
        return checker;
    }

    public void createNewWord(long chatId) {
        Saver saver = saverRep.findByUserId(chatId);
        saver.setStatus(true);
        saverRep.save(saver);
    }

    @Transactional
    public void createNewWordTest(long chatId) {
        Word word = new Word();
        word.setEngTranslation("engTest");
        word.setRusTranslation("rusTest");
        word.setId(1223);
//        List<User> userList = new ArrayList<>();
//        userList.add(userInt.findById(759230168));
        //word.setUsers(userList);
        wordInt.save(word);
//        Theme theme = new Theme();
//        theme.setName("TestTheme");
//        themeRep.save(theme);

    }

    public void writeWord(long chatId, String word, String language) {
        Saver saver = saverRep.findByUserId(chatId);
        if (language.equals("rus")) {
            saver.setRusWord(word);
        } else {
            saver.setEngWord(word);
        }
        saverRep.save(saver);
    }

    @Transactional
    public void saveWord(long chatId) {
        Saver saver = saverRep.findByUserId(chatId);
        Word word = new Word();
        word.setEngTranslation(saver.getEngWord());
        word.setRusTranslation(saver.getRusWord());
        User user = saver.getUser();
        user.getWords().add(word);
        word.setUsers(List.of(user));
        wordInt.save(word);
        userInt.save(user);
        saver.setStatus(false);
        saverRep.save(saver);
    }

}
