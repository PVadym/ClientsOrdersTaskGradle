package ua.com.cashup.application.service;

import ua.com.cashup.application.entity.User;

/**
 * Created by Вадим on 06.10.2017.
 */
public interface UserService {

    User findByUsername(String username);

}
