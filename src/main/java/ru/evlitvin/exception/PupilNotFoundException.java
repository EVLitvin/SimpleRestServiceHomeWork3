package ru.evlitvin.exception;

public class PupilNotFoundException extends RuntimeException {

    public PupilNotFoundException(String message) {
        super(message);
    }
}
