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
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButton;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonCommands;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
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
            ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
            SendMessage message = new SendMessage();
            message.setText("Menu");
            message.setChatId(String.valueOf(chatID));


            KeyboardRow row_1 = new KeyboardRow();
            KeyboardRow row_2 = new KeyboardRow();
            KeyboardRow row_3 = new KeyboardRow();
            KeyboardRow row_4 = new KeyboardRow();
            row_1.add("B1");
            row_2.add("B2");
            row_3.add("B3");
            row_4.add("B4");

            List<KeyboardRow> list = new ArrayList<>();
            list.add(row_1);
            list.add(row_2);
            List<KeyboardRow> list2 = new ArrayList<>();
            //list2.add(row_3);
            //list2.add(row_4);


            markup.setKeyboard(list);
            String checker  = update.getMessage().getText();
            if(checker.equals("delete")){
                markup.setResizeKeyboard(true);
                //markup.setKeyboard(list2);
                message.setText("Меню удалено \uD83D\uDC3D");
                message.setReplyMarkup(markup);

            }
            else{
                message.setReplyMarkup(markup);
            }
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            //spamService.sendSpam(this);
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
