package com.tecknobit.pandoro.services;

import com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController;
import com.tecknobit.pandoro.services.users.models.PandoroUser;
import com.tecknobit.pandoro.services.users.repository.PandoroUsersRepository;
import com.tecknobit.pandoro.services.users.service.PandoroUsersHelper;

public class DefaultPandoroController extends EquinoxController<PandoroUser, PandoroUsersRepository, PandoroUsersHelper> {
}
