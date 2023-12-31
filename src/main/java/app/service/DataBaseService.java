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

    @Autowired
    private EditWordRep editWordRep;

    public void checkUser(long chatID) {
        User user;
        Saver saver;
        //setDefault(saver);
        System.out.println("user:" + userInt.findById(chatID));
        if (userInt.findById(chatID) == null) {
            user = new User();
            user.setId(chatID);
            saver = new Saver();
            saver.setUser(user);
            saver.setId(user.getId() + 1);
            setDefault(saver);
            user.setSaver(saver);
            userInt.save(user);
            saverRep.save(saver);
        }
        else {
            saver = saverRep.findByUserId(chatID);
            setDefault(saver);
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
        if(saver.getWordToTheme()!=0){
            Theme theme = themeRep.findById((long)saver.getWordToTheme());
            theme.getWords().add(word);
            word.setThemes(List.of(theme));
            themeRep.save(theme);
        }
        if(saver.getWordToSubtopic()!=0){
            SubTopic subTopic = subtopicRep.findById((long)saver.getWordToSubtopic());
            subTopic.setWords(List.of(word));
            word.setTopics(List.of(subTopic));
            subtopicRep.save(subTopic);
        }


        wordInt.save(word);
        userInt.save(user);
        saverRep.save(setDefault(saver));
    }

    public  boolean getStatusCreateWordWithTheme(long chatId){
        return saverRep.findByUserId(chatId).isStatusCreateWithTheme();
    }
    public void setWordToTheme(long chatId, long themeId){
        Saver saver = saverRep.findByUserId(chatId);
        saver.setWordToTheme(themeId);
        saver.setStatusCreateWithTheme(true);
        saverRep.save(saver);
    }
    private Saver setDefault(Saver saver) {
        saver.setStatusCreateWord(false);
        saver.setStatusCreateTheme(false);
        saver.setStatusCreateSubtopic(false);
        saver.setRusWord("");
        saver.setEngWord("");
        saver.setThemeName("");
        saver.setIdParentTheme("");
        saver.setSubtopicName("");
        saver.setWordToTheme(0L);
        saver.setWordToSubtopic(0L);
        saver.setEditingWord(false);
        saver.setEditingTheme(false);
        saver.setEditingSubtopic(false);
        saver.setEditWordWithoutTheme(false);
        saver.setStatusCreateWithTheme(false);

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
        Saver saver = saverRep.findByUserId(chatId);

        User user = userInt.findById(chatId);
        Theme theme = new Theme();
        theme.setName(themeName);
        user.getThemes().add(theme);
        theme.setUsers(List.of(user));
        themeRep.save(theme);
        userInt.save(user);
        if(saver.isStatusCreateWord()&&saver.isStatusCreateTheme()){
            System.out.println("check");
            saver.setWordToTheme(themeRep.findTopByUsersIdOrderByIdDesc(chatId).getId());
            saver.setStatusCreateTheme(false);
            saverRep.save(saver);
        }
        else{
            saverRep.save(setDefault(saverRep.findByUserId(chatId)));
        }
    }

    @Transactional
    public void saveNewSubtopic(long chatId,String subtopicName){
        User user = userInt.findById(chatId);
        Saver saver = saverRep.findByUserId(chatId);

        Theme theme = themeRep.findById(Long.parseLong(saver.getIdParentTheme()));
        SubTopic subTopic = new SubTopic();
        user.getTopics().add(subTopic);
        theme.getSubTopics().add(subTopic);
        subTopic.setName(subtopicName);
        subTopic.setUsers(List.of(user));
        subTopic.setTheme(theme);
        subtopicRep.save(subTopic);
        themeRep.save(theme);
        userInt.save(user);
        if(saver.isStatusCreateWord()&&saver.isStatusCreateSubtopic()){
            System.out.println("check");
            saver.setWordToSubtopic(themeRep.findTopByUsersIdOrderByIdDesc(chatId).getId());
            saver.setStatusCreateSubtopic(false);
            saverRep.save(saver);
        }
        else{
            saverRep.save(setDefault(saverRep.findByUserId(chatId)));
        }
        //saverRep.save(setDefault(saverRep.findByUserId(chatId)));

    }

    public List<Theme> getThemeList(long chatId){
        return themeRep.findByUsersId(chatId);
    }
    public boolean ifCreatedTheme(long chatId){
        //System.out.println("проверка создания темы");
        //System.out.println(saverRep.findByUserId(chatId).isStatusCreateTheme());
        return saverRep.findByUserId(chatId).isStatusCreateTheme();
    }
    public void createNewTheme(long chatID) {
        Saver saver = saverRep.findByUserId(chatID);
        saver.setStatusCreateTheme(true);
        saverRep.save(saver);
    }

    public void createNewSubtopic(long chatId){
        Saver saver =  saverRep.findByUserId(chatId);
        saver.setStatusCreateSubtopic(true);
        saverRep.save(saver);
    }

    public boolean ifCreatedSubtopic(long chatID) {
        return saverRep.findByUserId(chatID).isStatusCreateSubtopic();
    }

    public void saveIdParentTheme(long chatID, long parentThemeId) {
        Saver saver = saverRep.findByUserId(chatID);
        saver.setIdParentTheme(String.valueOf(parentThemeId));
        saverRep.save(saver);
    }

    public List<SubTopic> getSubtopicList(long id) {
        //List<SubTopic> list = subtopicRep.findById(id).stream().toList();
        return subtopicRep.findByUsersId(id);
    }

    public void setWordToSubtopic(long id, long parseLong) {
        Saver saver = saverRep.findByUserId(id);
        saver.setWordToSubtopic(parseLong);
        saverRep.save(saver);
    }
    public Theme getLastCreatedTheme(long id){
        return themeRep.findTopByUsersIdOrderByIdDesc(id);
    }
    public long checkIdParentTheme(long chatId){
        if(saverRep.findByUserId(chatId).getIdParentTheme().equals("")){
            return 0;
        }
        else return Long.parseLong(saverRep.findByUserId(chatId).getIdParentTheme());
    }

    public void startEditingWord(long chatId){
        Saver saver = saverRep.findByUserId(chatId);
        saver.setEditingWord(true);

        saverRep.save(saver);
    }
    public boolean isEditingWord(long chatId){
        return saverRep.findByUserId(chatId).isEditingWord();
    }
    public void editRusWord(long chatId, String newRusTranslation){

        Word word = editWordRep.findByUser(userInt.findById(chatId)).stream().findFirst().get().getWord();
        word.setRusTranslation(newRusTranslation);

        editWordRep.delete(editWordRep.findByUser(userInt.findById(chatId)).stream().findFirst().get());
        wordInt.save(word);
        saverRep.save(setDefault(saverRep.findByUserId(chatId)));
    }
    public void editEngWord(long chatId,String newEngTranslation){

        Word word = editWordRep.findByUser(userInt.findById(chatId)).stream().findFirst().get().getWord();
        word.setEngTranslation(newEngTranslation);
        editWordRep.delete(editWordRep.findByUser(userInt.findById(chatId)).stream().findFirst().get());

        wordInt.save(word);
        saverRep.save(setDefault(saverRep.findByUserId(chatId)));
    }
    public void editDescriptionWord(long chatId,String newDescription){
        Word word = editWordRep.findByUser(userInt.findById(chatId)).stream().findFirst().get().getWord();
        word.setDescription(newDescription);
        editWordRep.delete(editWordRep.findByUser(userInt.findById(chatId)).stream().findFirst().get());

        wordInt.save(word);
        saverRep.save(setDefault(saverRep.findByUserId(chatId)));
    }
    public  void startEditingTheme(long chatID){

    }
    public boolean getStatusCreatWithoutTheme(long chatID){
        return saverRep.findByUserId(chatID).isEditWordWithoutTheme();
    }

    public void setCreatWordWithoutTheme(long chatId){
        Saver saver = saverRep.findByUserId(chatId);
        saver.setEditWordWithoutTheme(true);
        saverRep.save(saver);
    }
    public void setEditWordDefault(long chatId, long wordId){
        EditWord editWord = new EditWord();
        EditWordKey editWordKey = new EditWordKey();
        editWordKey.setUserId(chatId);
        editWordKey.setWordId(wordId);
        editWord.setId(editWordKey);
        editWord.setEditEng(false);
        editWord.setEditRus(false);
        editWord.setEditDescription(false);
        editWordRep.save(editWord);
    }
    public void setEditWordRus(long userId, long wordId){
        EditWord editWord = new EditWord();
        EditWordKey editWordKey = new EditWordKey();

        editWordKey.setUserId(userId);
        editWordKey.setWordId(wordId);

        editWord.setId(editWordKey);
        editWord.setUser(userInt.findById(userId));
        editWord.setWord(wordInt.findById(wordId));

        editWord.setEditRus(true);
        editWordRep.save(editWord);
    }
    public void setEditWordEng(long userId, long wordId){
        EditWord editWord = new EditWord();
        EditWordKey editWordKey = new EditWordKey();

        editWordKey.setUserId(userId);
        editWordKey.setWordId(wordId);

        editWord.setId(editWordKey);
        editWord.setUser(userInt.findById(userId));
        editWord.setWord(wordInt.findById(wordId));

        editWord.setEditEng(true);
        editWordRep.save(editWord);
    }
    public void setEditWordDescription(long userId, long wordId){
        EditWord editWord = new EditWord();
        EditWordKey editWordKey = new EditWordKey();

        editWordKey.setUserId(userId);
        editWordKey.setWordId(wordId);

        editWord.setId(editWordKey);
        editWord.setUser(userInt.findById(userId));
        editWord.setWord(wordInt.findById(wordId));

        editWord.setEditDescription(true);
        editWordRep.save(editWord);
    }
    public boolean getEditWordStatus(long chatId,String typeEdit){
        User user = userInt.findById(chatId);
        //Word word = editWordRep.findByUser(user).stream().findFirst().get().getWord();
        return switch (typeEdit) {
            case "eng" -> editWordRep.findByUser(user).stream().findFirst().get().isEditEng();
            case "rus" -> editWordRep.findByUser(user).stream().findFirst().get().isEditRus();
            case "description" -> editWordRep.findByUser(user).stream().findFirst().get().isEditDescription();
            default -> false;
        };
    }
}
