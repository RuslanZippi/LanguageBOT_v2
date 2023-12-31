package app.bot;
/*
нужно исправить баг с удалением связей у пользователя
 */

import app.dao.SubTopic;
import app.dao.Theme;
import app.dao.Word;
import app.service.DataBaseService;
import app.service.TranslationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class LanguageBot extends TelegramLongPollingBot {

    Logger loggerFactory = LoggerFactory.getLogger("Logger");

    public LanguageBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private TranslationService translationService;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatID = update.getMessage().getChatId();
            //dataBaseService.checkUser(chatID);
            loggerFactory.atInfo().log(String.valueOf(chatID));
            if (chatID !=0) {
                String textMessage = update.getMessage().getText();
                switch (textMessage) {
                    case "/start":
                        startMenu(chatID);
                        break;
                    case "\uD83D\uDCC3Мои слова\uD83D\uDCC3":
                        dataBaseService.stopSaveWord(chatID);
                        getWordMenu(chatID);
                        break;
                    case "\uD83D\uDCDAМои темы\uD83D\uDCDA":
                        dataBaseService.stopSaveWord(chatID);
                        getThemeMenu(chatID);
                        break;
                    case "\uD83C\uDDF7\uD83C\uDDFAВывести список слов на русском\uD83C\uDDF7\uD83C\uDDFA":
                        dataBaseService.stopSaveWord(chatID);
                        getRusList(chatID);
                        break;
                    case "\uD83C\uDDFA\uD83C\uDDF8Вывести список слов на английском\uD83C\uDDFA\uD83C\uDDF8":
                        dataBaseService.stopSaveWord(chatID);
                        getEngList(chatID);
                        break;
                    case "\uD83D\uDD27Редактирование слов\uD83D\uDD27":
                        sendMessage(chatID, "Какое слово отредактировать?");
                        sendWordList(chatID);
                        dataBaseService.startEditingWord(chatID);
                        break;
                    case "\uD83D\uDCDDДобавить новое слово\uD83D\uDCDD":
                        dataBaseService.stopSaveWord(chatID);
                        createNewWord(chatID);
                        break;
                    case "\uD83D\uDDC2Вывести список тем\uD83D\uDDC2":
                        dataBaseService.stopSaveWord(chatID);
                        getThemeList(chatID);
                        break;
//                    case "\uD83D\uDCC1Вывести список подтем\uD83D\uDCC1":
//                        dataBaseService.stopSaveWord(chatID);
//                        getSubtopicList(chatID);
//                        break;
                    case "\uD83D\uDCDDСоздать новую тему\uD83D\uDCDD":
                        dataBaseService.stopSaveWord(chatID);
                        createNewTheme(chatID);
                        break;
//                    case "\uD83D\uDCDDСоздать новую подтему\uD83D\uDCDD":
//                        dataBaseService.stopSaveWord(chatID);
//                        createNewSubtopic(chatID);
//                        break;
                    case "\uD83C\uDFE0Назад\uD83C\uDFE0":
                        dataBaseService.stopSaveWord(chatID);
                        startMenu(chatID);
                        break;
                    case "\uD83D\uDDC2Отобразить слова по темам\uD83D\uDDC2":
                        dataBaseService.stopSaveWord(chatID);
                        getListWordToTheme(chatID);
                        break;
//                    case "\uD83D\uDCC1Отобразить слова по подтемам\uD83D\uDCC1":
//                        break;
                    default:
                        if (dataBaseService.isCreatedWord(chatID)) {
                            if (dataBaseService.ifCreatedTheme(chatID)) {
                                saveNewTheme(chatID, textMessage);
                                sendMessage(chatID, "Введите слово на русском");
                            } else if (dataBaseService.ifCreatedSubtopic(chatID)) {
                                saveNewSubtopic(chatID, textMessage);
                                sendMessage(chatID, "Введите слово на русском");
                            } else if (dataBaseService.getStatusCreatWithoutTheme(chatID)) {
                                if (dataBaseService.ifExitsRus(chatID)) {
                                    patternCheck(chatID, textMessage, "rus");
                                } else if (dataBaseService.ifExitsEng(chatID)) {
                                    patternCheck(chatID, textMessage, "eng");
                                }
                            } else if (dataBaseService.getStatusCreateWordWithTheme(chatID)) {
                                if (dataBaseService.ifExitsRus(chatID)) {
                                    patternCheck(chatID, textMessage, "rus");
                                } else if (dataBaseService.ifExitsEng(chatID)) {
                                    patternCheck(chatID, textMessage, "eng");
                                }
                            }
                        }

                        if (dataBaseService.ifCreatedTheme(chatID)) {
                            saveNewTheme(chatID, textMessage);
                        }
                        if (dataBaseService.ifCreatedSubtopic(chatID)) {
                            if (dataBaseService.checkIdParentTheme(chatID) != 0) {
                                saveNewSubtopic(chatID, textMessage);
                            }
                        }
                        if (dataBaseService.isEditingWord(chatID)) {
                            editWord(chatID, textMessage);
                        }
                        break;
                }
            }
        }
        if (update.hasCallbackQuery()) {
            long id = update.getCallbackQuery().getFrom().getId();
            String callback = update.getCallbackQuery().getData();
            System.out.println(callback);
            if (dataBaseService.ifCreatedSubtopic(id)) {
                callback = callback.split("theme")[1];
                saveIdParentTheme(id, Long.parseLong(callback));
                sendMessage(id, "Введите название подтемы");
            }
            if (dataBaseService.isCreatedWord(id)) {
                switch (callback) {
                    case "-10":
                        sendQuestionOne(id);
                        break;
                    case "-20":
                        sendQuestionThree(id);
                        break;
//                    case "-30":
//                        sendQuestionThree(id);
//                        break;
                }

                if (callback.startsWith("theme")) {
                    System.out.println("CallBack:" + callback);
                    callback = callback.split("theme")[1];
                    if (!callback.equals("-1")) {
                        dataBaseService.setWordToTheme(id, Long.parseLong(callback));
                        sendMessage(id, "Введите слово на русском");
                    } else {
                        createNewTheme(id);
                        System.out.println(dataBaseService.getLastCreatedTheme(id).getId());
                    }
                }
//                if (callback.startsWith("subtopic")) {
//
//                    callback = callback.split("subtopic")[1];
//                    if (!callback.equals("-1")) {
//                        dataBaseService.setWordToSubtopic(id, Long.parseLong(callback));
//                    } else {
//                        createNewSubtopic(id);
//                    }
//                    //sendMessage(id,"Введите слово на русском");
//                }
            }
            if (dataBaseService.isEditingWord(id)) {
                if (callback.startsWith("edit")) {
                    callback = callback.split("edit")[1];
                    if (callback.split("_").length == 2) {
                        String answerOption = callback.split("_")[0];
                        System.out.println(answerOption);
                        switch (answerOption) {
                            case "1":
                                sendMessage(id, "Введите новый перевод на английском");
                                dataBaseService.setEditWordEng(id, Long.parseLong(callback.split("_")[1]));
                                break;
                            case "2":
                                sendMessage(id, "Введите новый перевод на русском");
                                dataBaseService.setEditWordRus(id, Long.parseLong(callback.split("_")[1]));
                                break;
                            case "3":
                                sendMessage(id, "Введите новое описание");
                                dataBaseService.setEditWordDescription(id, Long.parseLong(callback.split("_")[1]));
                                break;
                        }
                    }
                    if (callback.split("_").length == 3) {
                        System.out.println("length = 3");
                        System.out.println(callback);
                        String answerOption = callback.split("_")[1];
                        switch (answerOption) {
                            case "1":
                                System.out.println(answerOption);
                                break;
                            case "2":
                                break;
                            case "3":
                                break;
                        }
                    }
                } else {
                    String finalCallback = callback;
                    Word word = dataBaseService.getListWords(id).stream().filter(x -> x.getId() == Long.parseLong(finalCallback)).findFirst().get();
                    //String text = "Выбранное слов: \n" + word.getEngTranslation() + " -  " + word.getRusTranslation() + "\n" + "Описание: " + word.getDescription();
                    sendMessageChoice(id, word);
                }
            }
            if (callback.startsWith("listWordOfTheme")) {
                System.out.println("Выбрана тема для вывода списка слов");
                getWordsToTheme(id, callback.split("listWordOfTheme")[1]);
            }
        }
    }

    private void getWordsToTheme(long id, String listWordOfTheme) {
        List<Word> wordList = dataBaseService.getThemeList(id).stream().filter(x -> x.getId() == Long.parseLong(listWordOfTheme)).findFirst().get().getWords();
        StringBuilder builder = new StringBuilder();
        String text;
        int x = 1;
        for (Word w : wordList) {
            builder.append(x + ") ");
            builder.append(w.getEngTranslation() + " - " + w.getRusTranslation() + "\n");
            x++;
        }
        if(wordList.size()==0){
         text = " У вас нет тем";
        }
        else {
            text = builder.toString();
        }
        sendMessage(id, text);
    }

    private void getListWordToTheme(long chatID) {
        List<Theme> themeList = dataBaseService.getThemeList(chatID);
        StringBuilder builder = new StringBuilder();
        builder.append("Ваши темы: \n");
        int x = 1;
        for (Theme t : themeList) {
            builder.append(x + ") " + t.getName() + "(слов - " + getAllCountWord(t) + " )");
            builder.append("\n");
            x++;
        }
        sendMessage(chatID, builder.toString());
        showWordToTheme(chatID, themeList);
    }

    private void showWordToTheme(long chatId, List<Theme> themeList) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button;

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        String text = "listWordOfTheme";
        for (int x = 0; x < themeList.size(); x++) {
            if (x > 1 && x % 8 == 1) {
                button = new InlineKeyboardButton();
                System.out.println("9");
                list.add(buttonList);
                buttonList = new ArrayList<>();
                button.setText(String.valueOf(x + 1));
                button.setCallbackData(text + themeList.get(x).getId());
                buttonList.add(button);
            } else {
                System.out.println("<9");
                button = new InlineKeyboardButton();
                button.setText(String.valueOf(x + 1));
                button.setCallbackData(text + themeList.get(x).getId());
                buttonList.add(button);
                //list.add(buttonList);
            }
        }
        list.add(buttonList);
        markup.setKeyboard(list);
        try {
            execute(SendMessage.builder()
                    .text("К какой теме вывести список слов?")
                    .replyMarkup(markup)
                    .chatId(chatId)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private int getAllCountWord(Theme theme) {
        int countWord = 0;

        countWord += theme.getWords().size();
        return countWord;
    }

    private void editWord(long chatID, String textMessage) {
        if (dataBaseService.getEditWordStatus(chatID, "eng")) {
            if (Pattern.matches("[a-zA-Z]*", textMessage)) {
                dataBaseService.editEngWord(chatID, textMessage);
                //System.out.println("editRus");
                sendMessage(chatID, "Перевод изменен");
            } else {
                sendMessage(chatID, "неверный ввод");
            }

        } else if (dataBaseService.getEditWordStatus(chatID, "rus")) {
            if (Pattern.matches("[а-яА-ЯёЁ]*", textMessage)) {
                dataBaseService.editRusWord(chatID, textMessage);
                sendMessage(chatID, "Перевод изменен");
            } else {
                sendMessage(chatID, "неверный ввод");
            }
        } else if (dataBaseService.getEditWordStatus(chatID, "description")) {
            if (dataBaseService.getEditWordStatus(chatID, "description")) {
                if (Pattern.matches("[а-яА-ЯёЁ]*", textMessage)) {
                    dataBaseService.editDescriptionWord(chatID, textMessage);
                    sendMessage(chatID, "Описание изменено");
                } else {
                    sendMessage(chatID, "неверный ввод");
                }
            }
        }
    }

    private void sendQuestionThree(long id) {
        sendMessage(id, "Введите слово на русском");
        dataBaseService.setCreatWordWithoutTheme(id);
    }

    private void sendMessageChoice(long chatId, Word word) {
        String text = "Выбранное слов: \n" + word.getEngTranslation() + " -  " + word.getRusTranslation() + "\n" + "Описание: " + word.getDescription();
        try {
            execute(SendMessage.builder().chatId(chatId).text(text).replyMarkup(getNewInlineKeyBoarMarkupForEditWord(word.getId())).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getNewInlineKeyBoarMarkupForEditWord(long wordId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button;

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        int x = 1;
        for (String s : List.of("Изменить английский перевод", "Изменить русский перевод", "Изменить описание")) {
            button = new InlineKeyboardButton();
            button.setText(s);
            button.setCallbackData("edit" + x + "_" + wordId);
            buttonList.add(button);
            list.add(buttonList);
            buttonList = new ArrayList<>();
            x++;
        }
        markup.setKeyboard(list);
        return markup;
    }

    private void sendQuestionTwo(long id) {
        sendThemeOrSubtopicList(id, dataBaseService.getSubtopicNameList(id), "subtopic");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText("К какой подтеме добавить слово");
        sendMessage.setReplyMarkup(createInlineKeyboardSubtopic(dataBaseService.getSubtopicList(id)));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendQuestionOne(long id) {
        sendThemeOrSubtopicList(id, dataBaseService.getThemeNameList(id), "theme");
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(id);
        sendMessage.setText("К какой теме добавить слово");
        sendMessage.setReplyMarkup(createInlineKeyboardTheme(id, dataBaseService.getThemeList(id)));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void saveIdParentTheme(long chatID, long parentThemeId) {
        dataBaseService.saveIdParentTheme(chatID, parentThemeId);
    }

    private @NotNull InlineKeyboardMarkup createInlineKeyboardSubtopic(List<SubTopic> buttonsName) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        InlineKeyboardButton button;
        List<InlineKeyboardButton> buttonsSubtopicList = new ArrayList<>();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        int x = 1;
        for (SubTopic t : buttonsName) {
            if (buttonsSubtopicList.size() % 8 == 1) {
                list.add(buttonsSubtopicList);
                buttonsSubtopicList = new ArrayList<>();
            }
            button = new InlineKeyboardButton();
            String text = "subtopic" + t.getId();
            button.setText(String.valueOf(x));
            button.setCallbackData(text);
            buttonsSubtopicList.add(button);
            x++;

        }
        List<InlineKeyboardButton> buttonChoice = new ArrayList<>();
        button = new InlineKeyboardButton();
        String text = "Создать новую новую подтему";
        button.setText(text);
        button.setCallbackData("subtopic-1");
        buttonChoice.add(button);


        list.add(buttonsSubtopicList);
        list.add(buttonChoice);
        markup.setKeyboard(list);
        return markup;
    }

    /**
     * Метод создает встроенную клавиатуру для выбора темы
     *
     * @param chatId      id пользователя
     * @param buttonsName список названий тем для кнопок клавиатуры
     * @return Возвращает готовую клавиатуру
     */
    private @NotNull InlineKeyboardMarkup createInlineKeyboardTheme(long chatId, List<Theme> buttonsName) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button;
        List<InlineKeyboardButton> buttonsThemeList = new ArrayList<>();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        int x = 1;
        for (Theme t : buttonsName) {
            if (x % 8 == 1 && x > 1) {
                list.add(buttonsThemeList);
                buttonsThemeList = new ArrayList<>();
            }
            button = new InlineKeyboardButton();
            String text = "theme" + t.getId();
            button.setText(String.valueOf(x));
            button.setCallbackData(text);
            buttonsThemeList.add(button);
            x++;

        }
        List<InlineKeyboardButton> buttonChoice = new ArrayList<>();
        button = new InlineKeyboardButton();
        String newTheme = "Создать новую новую тему";
        button.setText(newTheme);
        button.setCallbackData("theme-1");
        buttonChoice.add(button);

        //List<List<InlineKeyboardButton>> list = new ArrayList<>();
        list.add(buttonsThemeList);
        list.add(buttonChoice);
        markup.setKeyboard(list);
        return markup;

    }

    /**
     * Начало создания подтемы, в методы идет проверка на существование тем, для выбор к какой теме отнести новуб подтему
     *
     * @param chatID id пользователя
     */
    @Deprecated
    private void createNewSubtopic(long chatID) {
        dataBaseService.createNewSubtopic(chatID);
        SendMessage message = new SendMessage();
        message.setChatId(chatID);

        if (dataBaseService.getCountTheme(chatID) == 0) {
            sendMessage(chatID, "Для создания подтемы, необходима хотя бы одна тема. У вас нет тем");
        } else {
            message.setReplyMarkup(createInlineKeyboardTheme(chatID, dataBaseService.getThemeList(chatID)));
            sendThemeOrSubtopicList(chatID, dataBaseService.getThemeNameList(chatID), "theme");
            message.setText("К какой теме создать подтему?");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    @Deprecated
    private void saveNewSubtopic(long chatId, String subtopicName) {
        dataBaseService.saveNewSubtopic(chatId, subtopicName);
        sendMessage(chatId, "Новая подтема добавлена");
    }

    /**
     * Метод сохраняет новую тему
     *
     * @param chatId    id пользователя
     * @param themeName название новой темы
     */

    private void saveNewTheme(long chatId, String themeName) {
        dataBaseService.saveTheme(chatId, themeName);
        sendMessage(chatId, "Новая тема добавлена");
    }

    /**
     * Начало создания новой темы
     *
     * @param chatID id пользователя
     */
    private void createNewTheme(long chatID) {
        dataBaseService.createNewTheme(chatID);
        sendMessage(chatID, "Введите название темы");
    }

    @Override
    public String getBotUsername() {
        return "testBot";
    }

    /**
     * Метод отправляет нужное текстовое сообщения
     *
     * @param chatID id пользователя
     * @param text   текст для отправки
     */
    private void sendMessage(long chatID, String text) {
        var chatIdStr = String.valueOf(chatID);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Создаются две кнопки, после команты /start
     *
     * @param chatID id пользователя
     */
    private void startMenu(long chatID) {
        dataBaseService.checkUser(chatID);
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow rowMainMenu = new KeyboardRow();

        rowMainMenu.add("\uD83D\uDCC3Мои слова\uD83D\uDCC3");
        rowMainMenu.add("\uD83D\uDCDAМои темы\uD83D\uDCDA");


        List<KeyboardRow> list = new ArrayList<>();

        list.add(rowMainMenu);
        markup.setKeyboard(list);

        String text = "Начинаем учить слова";

        SendMessage message = new SendMessage();

        markup.setOneTimeKeyboard(true);
        markup.setResizeKeyboard(true);

        message.setReplyMarkup(markup);
        message.setText(text);
        message.setChatId(chatID);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    /**
     * Метод для проверки вводимого слова на русские и английские буквы
     *
     * @param chatId id пользователя
     * @param word   вводимое слово
     */
    private void patternCheck(long chatId, String word, String needLang) {
        if (needLang.equals("rus")) {
            if (Pattern.matches("[а-яА-Яё]*", word)) {
                saveRusWord(chatId, word);
            } else {
                sendMessage(chatId, "Неверный ввод \n" + "Введите слово на русском");
            }
        }
        if (needLang.equals("eng")) {
            if (Pattern.matches("[a-zA-Z]*", word)) {
                saveEngWord(chatId, word);
            } else {
                sendMessage(chatId, "Неверный ввод \n" + "Введите слово на английском");
            }
        }
    }

    /**
     * Создание меню "слово"
     *
     * @param chatId id пользователя
     */
    private void getWordMenu(long chatId) {
        ArrayList<String> listOfButtonNames = new ArrayList<>();


        listOfButtonNames.add("\uD83C\uDDF7\uD83C\uDDFAВывести список слов на русском\uD83C\uDDF7\uD83C\uDDFA");
        listOfButtonNames.add("\uD83C\uDDFA\uD83C\uDDF8Вывести список слов на английском\uD83C\uDDFA\uD83C\uDDF8");
        listOfButtonNames.add("\uD83D\uDDC2Отобразить слова по темам\uD83D\uDDC2");
        //listOfButtonNames.add("\uD83D\uDCC1Отобразить слова по подтемам\uD83D\uDCC1");
        listOfButtonNames.add("\uD83D\uDCDDДобавить новое слово\uD83D\uDCDD");
        listOfButtonNames.add("\uD83D\uDD27Редактирование слов\uD83D\uDD27");
        listOfButtonNames.add("\uD83C\uDFE0Назад\uD83C\uDFE0");

        ReplyKeyboardMarkup markup = createMarkup(listOfButtonNames);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        long wordCount = dataBaseService.getCountWord(chatId);
        String text = "У вас: " + wordCount + " слов";
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Создание кнопок с нужными названиями
     *
     * @param list список названий
     * @return возвращает готовый объект кнопок
     */
    private ReplyKeyboardMarkup createMarkup(ArrayList<String> list) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();

        List<KeyboardRow> buttons = new ArrayList<>();


        KeyboardRow wordMenu;
        KeyboardRow wordMenuTwoColumn = new KeyboardRow();
        for (String s : list) {
            if (s.equals("\uD83D\uDDC2Отобразить слова по темам\uD83D\uDDC2") || s.equals("\uD83D\uDCC1Отобразить слова по подтемам\uD83D\uDCC1")) {
                wordMenuTwoColumn.add(s);
            } else {
                if (wordMenuTwoColumn.size() > 0) {
                    buttons.add(wordMenuTwoColumn);
                    wordMenuTwoColumn = new KeyboardRow();
                }
                wordMenu = new KeyboardRow();
                wordMenu.add(s);
                buttons.add(wordMenu);
            }
        }
        markup.setKeyboard(buttons);
        return markup;
    }

    /**
     * Создание меню для "Тем"
     *
     * @param chatID id пользователя
     */
    private void getThemeMenu(long chatID) {
        ArrayList<String> listOfButtonNames = new ArrayList<>();


        listOfButtonNames.add("\uD83D\uDDC2Вывести список тем\uD83D\uDDC2");
        //listOfButtonNames.add("\uD83D\uDCC1Вывести список подтем\uD83D\uDCC1");
        listOfButtonNames.add("\uD83D\uDCDDСоздать новую тему\uD83D\uDCDD");
        //listOfButtonNames.add("\uD83D\uDCDDСоздать новую подтему\uD83D\uDCDD");
        listOfButtonNames.add("\uD83C\uDFE0Назад\uD83C\uDFE0");

        ReplyKeyboardMarkup markup = createMarkup(listOfButtonNames);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        long countTheme = dataBaseService.getCountTheme(chatID);
        long countSubtopic = dataBaseService.getCountSubtopic(chatID);
        String text = "У вас: " + countTheme + " тем(а/ы)\n";
//                + "У вас: " + countSubtopic + " подтем(а/ы)";
        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        message.setText(text);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    /**
     * Получение русских слов из бд, передача их метод для отправки
     *
     * @param chatID id пользователя
     */
    private void getRusList(long chatID) {
        List<String> wordList = dataBaseService.getRusWordList(chatID);
        sendWordList(chatID, wordList);
    }

    private void sendWordList(long chatId) {

        List<Word> wordsList = dataBaseService.getListWords(chatId);
        int listSize = wordsList.size();
        SendMessage message = new SendMessage();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button;
        List<InlineKeyboardButton> buttonsList = new ArrayList<>();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        for (int x = 0; x < listSize; x++) {
            builder.append((x + 1) + ") " + wordsList.get(x).getEngTranslation() + " - " + wordsList.get(x).getRusTranslation());
            builder.append("\n");
            if (x % 8 == 1 && x != 1) {
                list.add(buttonsList);
                buttonsList = new ArrayList<>();
            }
            button = new InlineKeyboardButton();
            button.setText(String.valueOf((x + 1)));
            button.setCallbackData(String.valueOf(wordsList.get(x).getId()));
            buttonsList.add(button);
        }
        list.add(buttonsList);
        markup.setKeyboard(list);
        message.setText(builder.toString());
        message.setReplyMarkup(markup);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для отправки списка слов пользователю
     *
     * @param chatID   id пользователя
     * @param wordList список слов
     */
    private void sendWordList(long chatID, List<String> wordList) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        String text;
        StringBuilder builder = new StringBuilder();
        if (wordList.size() == 0) {
            text = "У вас нет слов";
        } else {
            builder.append("Ваши слова:\n");
            for (int x = 0; x < wordList.size(); x++) {
                builder.append((x + 1) + ") " + wordList.get(x) + "\n");
            }
            text = builder.toString();
        }
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение английских слов из бд, передача их в метод для отправки
     *
     * @param chatID id пользователя
     */
    private void getEngList(long chatID) {
        List<String> wordList = dataBaseService.getEngWordList(chatID);
        sendWordList(chatID, wordList);
    }

    /**
     * Получаение списка тем из бд, передача их в метод для отправки пользователю
     *
     * @param chatId id пользователя
     */
    private void getThemeList(long chatId) {
        sendThemeOrSubtopicList(chatId, dataBaseService.getThemeNameList(chatId), "theme");
    }

    /**
     * Получение списка подтем из бд, передача из в метод для отправки пользователю
     *
     * @param chatId id пользователя
     */
    private void getSubtopicList(long chatId) {
        sendThemeOrSubtopicList(chatId, dataBaseService.getSubtopicNameList(chatId), "subtopic");
    }

    /**
     * Метод, который отправляет список тем или подтем пользоввателю.
     *
     * @param chatId          id пользователя
     * @param nameList        список тем или подтем
     * @param themeOrSubtopic критерий выбора (тема или подтема)
     */
    private void sendThemeOrSubtopicList(long chatId, List<String> nameList, String themeOrSubtopic) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String text;
        StringBuilder builder = new StringBuilder();
        if (themeOrSubtopic.equals("theme")) {
            builder.append("Ваши темы: \n");
            for (int x = 0; x < nameList.size(); x++) {
                builder.append((x + 1) + ")" + nameList.get(x) + "\n");
            }
        } else {
            builder.append("Ваши подтемы: \n");
            for (int x = 0; x < nameList.size(); x++) {
                builder.append((x + 1) + ")" + nameList.get(x) + "\n");
            }
        }
        message.setText(builder.toString());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Стартовый метод сохранения нового слова в бд
     *
     * @param chatId id пользователя
     */
    private void createNewWord(long chatId) {
        List<String> buttonName = new ArrayList<>();
        SendMessage sendMessage = new SendMessage();
        String text = "Добавить слово в тему ?";
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        buttonName.add("Сохранить в тему");
        //buttonName.add("Сохранить в подтему");
        buttonName.add("Создать слово без темы");
        sendMessage.setReplyMarkup(createInlineKeyboardForNewWord(chatId, buttonName));
        try {
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        dataBaseService.createNewWord(chatId);
    }

    private InlineKeyboardMarkup createInlineKeyboardForNewWord(long chatId, List<String> buttonName) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button;


        List<InlineKeyboardButton> list;
        List<List<InlineKeyboardButton>> listList = new ArrayList<>();
        int x = -10;
        for (String s : buttonName) {
            list = new ArrayList<>();
            button = new InlineKeyboardButton();
            button.setText(s);
            button.setCallbackData(String.valueOf(x));
            x -= 10;
            list.add(button);
            listList.add(list);
        }
        markup.setKeyboard(listList);
        return markup;
    }

    /**
     * Сохранения русского слова во временное хранилище
     *
     * @param chatId  id пользователя
     * @param rusWord русское слово
     */
    private void saveRusWord(long chatId, String rusWord) {
        dataBaseService.writeWord(chatId, rusWord, "rus");
        sendMessage(chatId, "Введите слово на английском");
    }

    /**
     * Сохранение английского слова во временное хранилище
     *
     * @param chatId  id пользователя
     * @param engWord английское слово
     */
    private void saveEngWord(long chatId, String engWord) {
        dataBaseService.writeWord(chatId, engWord, "eng");
        saveWord(chatId);
    }

    /**
     * Финальное сохранение слова в бд
     *
     * @param chatId id пользователя
     */
    private void saveWord(long chatId) {
        dataBaseService.saveWord(chatId);

        sendMessage(chatId, "слово добавлено");
    }
}
