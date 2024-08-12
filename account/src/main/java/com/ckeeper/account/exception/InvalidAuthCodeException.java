package com.ckeeper.account.exception;

public class InvalidAuthCodeException extends AuthCodeException{
    public InvalidAuthCodeException() {
        super("Invalid authentication code");
    }
}
