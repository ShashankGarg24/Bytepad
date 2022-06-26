package com.Bytepad.server.exceptions;


public class UsernameAlreadyExist extends Exception {
    public UsernameAlreadyExist() {

        super(" username already exists!");
    }
}
