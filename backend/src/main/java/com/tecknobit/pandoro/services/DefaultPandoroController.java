package com.tecknobit.pandoro.services;

import com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandoro.services.users.repository.PandoroUsersRepository;
import com.tecknobit.pandoro.services.users.service.PandoroUsersService;

/**
 * The {@code DefaultPandoroController} class is useful to give the base behavior of the <b>Pandoro's controllers</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class DefaultPandoroController extends EquinoxController<PandoroUser, PandoroUsersRepository, PandoroUsersService> {
}
