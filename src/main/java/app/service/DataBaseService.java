package app.service;

import app.dao.*;
import app.dao.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
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

    public void checkUser(long chatID) {
        User user;
        Saver saver;
        if (userInt.findById(chatID) == null) {
            user = new User();
            user.setId(chatID);
            // userInt.save(user);
            saver = new Saver();
            saver.setUser(user);
            saver.setId(user.getId() + 1);
            saver.setStatusCreateWord(false);
            saver.setStatusCreateTheme(false);
            saver.setStatusCreateSubtopic(false);
            saver.setRusWord("");
            saver.setEngWord("");
            saver.setThemeName("");
            saver.setSubtopicName("");
            user.setSaver(saver);
            userInt.save(user);
            saverRep.save(saver);
        }
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

    public boolean isCreatedWord(long chatId) {
        return saverRep.findByUserId(chatId).isStatusCreateWord();
    }

    public void createNewWord(long chatId) {
        Saver saver = saverRep.findByUserId(chatId);
        saver.setStatusCreateWord(true);
        saverRep.save(saver);
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
        saverRep.save(setDefault(saver));
    }

    private Saver setDefault(Saver saver) {
        saver.setStatusCreateWord(false);
        saver.setStatusCreateTheme(false);
        saver.setStatusCreateSubtopic(false);
        saver.setRusWord("");
        saver.setEngWord("");
        saver.setThemeName("");
        saver.setSubtopicName("");
        return saver;
    }

    public void stopSaveWord(long chatId) {
        Saver saver = saverRep.findByUserId(chatId);
        saverRep.save(setDefault(saver));
    }

    public boolean ifExitsRus(long chatId) {
        return saverRep.findByUserId(chatId).getRusWord().equals("");
    }

    public boolean ifExitsEng(long chatId) {
        return saverRep.findByUserId(chatId).getEngWord().equals("");
    }

    @Transactional
    public void saveTheme(long chatId, String themeName) {
        User user = userInt.findById(chatId);
        Theme theme = new Theme();
        theme.setName(themeName);
        user.getThemes().add(theme);
        theme.setUsers(List.of(user));
        themeRep.save(theme);
        userInt.save(user);
        setDefault(saverRep.findByUserId(chatId));
    }

    public boolean ifCreatedTheme(long chatId){
        return saverRep.findByUserId(chatId).isStatusCreateTheme();
    }
    public void createNewTheme(long chatID) {
        Saver saver = saverRep.findByUserId(chatID);
        saver.setStatusCreateTheme(true);
        saverRep.save(saver);

    }
}
