package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.NotesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/notes" // TODO: 29/10/2023 INSERT THE CORRECT PATH
)
public class NotesController {

    private final NotesHelper notesHelper;

    @Autowired
    public NotesController(NotesHelper notesHelper) {
        this.notesHelper = notesHelper;
    }

}
