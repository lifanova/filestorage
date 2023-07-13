package ru.netology.filestorage.exception;

public class FileStorageError extends RuntimeException {
    public FileStorageError(String message) {
        super(message);
    }
}
