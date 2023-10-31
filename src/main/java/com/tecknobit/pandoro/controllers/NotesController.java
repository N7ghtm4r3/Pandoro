package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.NotesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tecknobit.pandoro.controllers.NotesController.NOTES_ENDPOINT;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;

@RestController
@RequestMapping(path = BASE_ENDPOINT + NOTES_ENDPOINT)
public class NotesController extends PandoroController {

    public static final String NOTES_ENDPOINT = "notes";

    private final NotesHelper notesHelper;

    @Autowired
    public NotesController(NotesHelper notesHelper) {
        this.notesHelper = notesHelper;
    }

}
