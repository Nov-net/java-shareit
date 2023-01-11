package ru.practicum.shareit.exception;

public class UnknownState extends RuntimeException {
    public UnknownState(String message) {
        super(message);
    }
}
