package app.bot;

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

    public LanguageBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatID = update.getMessage().getChatId();
       // String text = update.getMessage().getText();
        if (update.hasMessage()) {
            System.out.println(update.getMessage().getText());
            String text = update.getMessage().getText();

            sendMessage(chatID,text);

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
