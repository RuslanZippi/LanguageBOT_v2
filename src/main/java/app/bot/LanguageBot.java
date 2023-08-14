package app.bot;

import app.service.DataBaseService;
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

    @Override
    public void onUpdateReceived(Update update) {
        long chatID = update.getMessage().getChatId();
        if (update.hasMessage()) {
            loggerFactory.atInfo().log(String.valueOf(chatID));
            if (chatID == 759230168) {

                String textMessage = update.getMessage().getText();
                switch (textMessage){
                    case "/start":
                        startMenu(chatID);
                        break;
                    case "\uD83D\uDCC3Мои слова\uD83D\uDCC3":
                        int count = dataBaseService.getCountWord(chatID);
                        sendMessage(chatID,"Количество слов: "+ count);
                        break;
                    default:
                        String text = "TEST_text";
                        sendMessage(chatID, text);
                        break;
                }
                //System.out.println(update.getMessage().getText());
                //String text = "TEST_text";
               // sendMessage(chatID, text);
            }
        } else {
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
        KeyboardRow row  = new KeyboardRow();

        row.add("\uD83D\uDCC3Мои слова\uD83D\uDCC3");

        List<KeyboardRow> list = new ArrayList<>();

        list.add(row);
        markup.setKeyboard(list);

        String text = "Начинаем учить слова.";

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
}
