package app.bot;

import app.dao.Word;
import app.service.DataBaseService;
import app.service.SpamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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

    @Override
    public void onUpdateReceived(Update update) {
        //long chatID = update.getMessage().getChatId();
        if (update.hasMessage()) {
            long chatID = update.getMessage().getChatId();
            loggerFactory.atInfo().log(String.valueOf(chatID));
            if (chatID == 759230168) {

                String textMessage = update.getMessage().getText();
                switch (textMessage){
                    case "/start":
                        startMenu(chatID);
                        break;
                    case "\uD83D\uDCC3Мои слова\uD83D\uDCC3":
                        getWorld(chatID);
                        break;
                    case "\uD83D\uDCDAМои темы\uD83D\uDCDA":
                        break;
                    default:
                        String text = "TEST_text";
                        System.out.println(update.getMessage().getText());
                        sendMessage(chatID, text);
                        break;
                }
            }
        }
        if (update.hasCallbackQuery()){
            String callBack = update.getCallbackQuery().getData();
            //AnswerCallbackQuery.builder().build().
            switch (callBack){
                case "rus":
                    //sendListOfWords();
                    break;
                case "eng":
                    break;
                case "new":
                    break;
                default:
                    break;
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

    private void startMenu(long chatID){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow rowMainMenu  = new KeyboardRow();

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

    private void getWorld(long chatId){
        ArrayList<String> listOfButtonNames = new ArrayList<>();



        listOfButtonNames.add("Вывести список слов на русском");
        listOfButtonNames.add("rus");
        listOfButtonNames.add("Вывести список слов на английском");
        listOfButtonNames.add("eng");
        listOfButtonNames.add("Добавить новое слово");
        listOfButtonNames.add("new");
        InlineKeyboardMarkup markup = createMarkup(listOfButtonNames);

        int wordCount = dataBaseService.getCountWord(chatId);
        String text = "У вас: "+ wordCount;
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        //sendMessage(chatId,text);
    }

    private InlineKeyboardMarkup createMarkup(ArrayList<String> list){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        InlineKeyboardButton button;
        int size = list.size();
        for (int x = 0; x < size; x++){
            button = new InlineKeyboardButton();
            button.setText(list.get(x));
            button.setCallbackData(list.get(x+1));
            x++;
            buttons.add(button);
        }
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(buttons);
        markup.setKeyboard(lists);
        return markup;
    }

    private void sendListOfWords(long chatId){
        CallbackQuery callbackQuery = new CallbackQuery();

        List<Word> list = dataBaseService.getListWords(chatId);
        StringBuilder builder = new StringBuilder();
        for (Word w : list){
            builder.append(w.getName());
            builder.append("\n");
        }


    }
}
