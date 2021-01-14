package com.example.demo.exception;

public class ClientException extends RuntimeException{

    private ClientErrors error;

    private ClientException(ClientErrors error) {
        this.error = error;
    }

    public ClientErrors getError() {
        return error;
    }


    public enum ClientErrors {
        USER_NOT_FOUND,
        BAD_CREDENTIALS,
        BAD_REQUEST,
        USER_HAS_NO_ACCOUNT_FOR_CURRENCY,
        ACCOUNT_HAS_NOT_ENOUGH_AMOUNT_FOR_PAYMENT,
        PAYMENT_COULD_NOT_BE_PROCESSED,
        CLIENT_WITH_SAME_EMAIL_ALREADY_EXISTS,
        CLIENT_WITH_NO_LASTNAME_OR_FIRSTNAME,
        CLIENT_DOES_NOT_EXIST
    }

    public static ClientException clientWithSameEmailAlreadyExists() {
        return new ClientException(ClientErrors.CLIENT_WITH_SAME_EMAIL_ALREADY_EXISTS);
    }

    public static ClientException clientWithNoLastNameOrFirstName() {
        return new ClientException(ClientErrors.CLIENT_WITH_NO_LASTNAME_OR_FIRSTNAME);
    }

    public static ClientException clientDoesNotExist(){
        return new ClientException(ClientErrors.CLIENT_DOES_NOT_EXIST);
    }
}
