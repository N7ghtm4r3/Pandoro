package com.tecknobit.pandoro.controllers;

import com.tecknobit.equinox.environment.controllers.EquinoxController;
import com.tecknobit.pandoro.services.repositories.UsersRepository;
import com.tecknobit.pandorocore.records.users.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.tecknobit.equinox.inputs.InputValidator.DEFAULT_LANGUAGE;

/**
 * The {@code PandoroController} class is useful to give the base behavior of the <b>Pandoro's controllers</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see EquinoxController
 */
public abstract class PandoroController extends EquinoxController {

    /**
     * {@code usersRepository} instance for the user repository
     */
    @Autowired
    protected UsersRepository usersRepository;

    /**
     * {@code me} user representing the user who made a request on the server
     */
    protected User me;

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isMe(String id, String token) {
        Optional<User> query = Optional.ofNullable(usersRepository.getAuthorizedUser(id, token));
        me = query.orElse(null);
        boolean isMe = me != null && me.getToken().equals(token);
        if (!isMe) {
            me = null;
            mantis.changeCurrentLocale(DEFAULT_LANGUAGE);
        } else
            mantis.changeCurrentLocale(me.getLanguage());
        return isMe;
    }

}
