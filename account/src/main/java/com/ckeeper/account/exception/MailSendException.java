package com.ckeeper.account.exception;

public class MailSendException extends AuthCodeException {
    public MailSendException(String message) {
        super(message);
    }
}
