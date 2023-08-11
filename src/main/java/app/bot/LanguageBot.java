package app.bot;

import app.dao.User;
import app.service.DataBaseService;
import app.service.SpamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
       // String text = update.getMessage().getText();
        if (update.hasMessage()) {
            System.out.println(update.getMessage().getText());
            String text = "TEST_text";
            loggerFactory.atInfo().log(String.valueOf(chatID));
            sendMessage(chatID,text);
            User user = new User();
            user.setId((int) chatID);
            user.setUserId(111);

            dataBaseService.saveUser(user);

            spamService.sendSpam(this);
        } else {
            System.out.println("FALSE");
        }
    }

    @Override
    public String getBotUsername() {
        return "testBot";
    }


    private void sendMessage(long chatID, String text){
        var chatIdStr = String.valueOf(chatID);
        var sendMessage = new SendMessage(chatIdStr,text);
        try{
        execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
