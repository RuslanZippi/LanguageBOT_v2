package app.service;

import app.dao.User;
import app.dao.repository.UserInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBaseService {

    @Autowired
    private UserInt userInt;

    public void saveUser(User user){
        userInt.save(user);
    }
}
