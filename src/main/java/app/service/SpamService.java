package app.service;

import app.dao.User;
import app.dao.repository.UserInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class SpamService {

    @Autowired
    private UserInt userInt;

    public void sendSpam(TelegramLongPollingBot bot){
        List<User> list = (List<User>) userInt.findAll();

        SendMessage sendMessage = new SendMessage();
        for (User u :list){
            sendMessage.setChatId(String.valueOf(u.getId()));
            sendMessage.setText("SPAAMM");
            try {

                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        //bot.execute(sendMessage)

    }
}
