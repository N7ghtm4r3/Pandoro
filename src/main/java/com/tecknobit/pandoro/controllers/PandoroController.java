package com.tecknobit.pandoro.controllers;

public abstract class PandoroController {

    public static final String BASE_ENDPOINT = "/api/v1/";

    /**
     * - SUCCESSFUL
     * { "success" : true }
     * - FAILED
     * {
     * "success" : true,
     * "error" : "error_message"
     * }
     */

    protected String decrypt() {
        return "";
    }

    protected String encrypt() {
        return "";
    }


}
