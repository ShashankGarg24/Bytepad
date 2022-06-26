package com.Bytepad.server.exceptions;


public class EmptyField extends Exception{

    public EmptyField(){
        super("Field can't be empty!");
    }
}
