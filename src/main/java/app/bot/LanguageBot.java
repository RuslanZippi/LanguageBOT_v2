package app.bot;

import app.service.DataBaseService;
import app.service.SaverService;
import app.service.SpamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class LanguageBot extends TelegramLongPollingBot {

    Logger loggerFactory = LoggerFactory.getLogger("Logger");

    public LanguageBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private SpamService spamService;

    @Autowired
    private SaverService saverService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatID = update.getMessage().getChatId();
            loggerFactory.atInfo().log(String.valueOf(chatID));
            if (chatID == 759230168) {
//270122424
                String textMessage = update.getMessage().getText();
                boolean checker = dataBaseService.isCreated(chatID);
                if (checker){

                }
                switch (textMessage) {
                    case "/start":
                        startMenu(chatID);
                        break;
                    case "\uD83D\uDCC3Мои слова\uD83D\uDCC3":
                        getWorldMenu(chatID);
                        break;
                    case "\uD83D\uDCDAМои темы\uD83D\uDCDA":
                        getThemeMenu(chatID);
                        break;
                    case "\uD83C\uDDF7\uD83C\uDDFAВывести список слов на русском\uD83C\uDDF7\uD83C\uDDFA":
                        getRusList(chatID);
                        break;
                    case "\uD83C\uDDFA\uD83C\uDDF8Вывести список слов на английском\uD83C\uDDFA\uD83C\uDDF8":
                        getEngList(chatID);
                        break;
                    case "\uD83D\uDCDDДобавить новое слово\uD83D\uDCDD":
                        //saverService.setSaveWord(true);

                        sendMessage(chatID, "Введите слово на русском");
                        //createNewWord(chatID);
                        break;
                    case "\uD83D\uDDC2Вывести список тем\uD83D\uDDC2":
                        getThemeList(chatID);
                        break;
                    case "\uD83D\uDCC1Вывести список подтем\uD83D\uDCC1":
                        getSubtopicList(chatID);
                        break;
                    case "\uD83D\uDCDDСоздать новую тему\uD83D\uDCDD":
                        break;
                    case "\uD83D\uDCDDСоздать новую подтему\uD83D\uDCDD":
                        break;
                    case "\uD83C\uDFE0Назад\uD83C\uDFE0":
                        startMenu(chatID);
                        break;
                    default:
                        String text = "TEST_text";
                        System.out.println(update.getMessage().getText());
                        sendMessage(chatID, text);
                        break;
                }
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "testBot";
    }

    private void sendMessage(long chatID, String text) {
        var chatIdStr = String.valueOf(chatID);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

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

    private void getWorldMenu(long chatId) {
        ArrayList<String> listOfButtonNames = new ArrayList<>();


        listOfButtonNames.add("\uD83C\uDDF7\uD83C\uDDFAВывести список слов на русском\uD83C\uDDF7\uD83C\uDDFA");
        listOfButtonNames.add("\uD83C\uDDFA\uD83C\uDDF8Вывести список слов на английском\uD83C\uDDFA\uD83C\uDDF8");
        listOfButtonNames.add("\uD83D\uDCDDДобавить новое слово\uD83D\uDCDD");
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

    private ReplyKeyboardMarkup createMarkup(ArrayList<String> list) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();

        List<KeyboardRow> buttons = new ArrayList<>();

        KeyboardRow wordMenu;
        for (String s : list) {
            wordMenu = new KeyboardRow();
            wordMenu.add(s);
            buttons.add(wordMenu);
        }
        markup.setKeyboard(buttons);
        return markup;
    }

    private void getThemeMenu(long chatID) {
        ArrayList<String> listOfButtonNames = new ArrayList<>();


        listOfButtonNames.add("\uD83D\uDDC2Вывести список тем\uD83D\uDDC2");
        listOfButtonNames.add("\uD83D\uDCC1Вывести список подтем\uD83D\uDCC1");
        listOfButtonNames.add("\uD83D\uDCDDСоздать новую тему\uD83D\uDCDD");
        listOfButtonNames.add("\uD83D\uDCDDСоздать новую подтему\uD83D\uDCDD");
        listOfButtonNames.add("\uD83C\uDFE0Назад\uD83C\uDFE0");

        ReplyKeyboardMarkup markup = createMarkup(listOfButtonNames);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        long countTheme = dataBaseService.getCountTheme(chatID);
        long countSubtopic = dataBaseService.getCountSubtopic(chatID);
        String text = "У вас: " + countTheme + " тем(а/ы)\n" +
                "У вас: " + countSubtopic + " подтем(а/ы)";
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

    private void getRusList(long chatID) {
        List<String> wordList = dataBaseService.getRusWordList(chatID);
        sendWordList(chatID, wordList);
    }

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

    private void getEngList(long chatID) {
        List<String> wordList = dataBaseService.getEngWordList(chatID);
        sendWordList(chatID, wordList);
    }

    private void getThemeList(long chatId) {
        sendThemeOrSubtopicList(chatId, dataBaseService.getThemeNameList(chatId), "theme");
    }

    private void getSubtopicList(long chatId) {
        sendThemeOrSubtopicList(chatId, dataBaseService.getSubtopicNameList(chatId), "subtopic");
    }

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

    private void createNewWord(long chatId) {
        dataBaseService.createNewWord(chatId);
    }
}
