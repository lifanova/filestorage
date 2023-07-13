package ru.netology.filestorage.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.filestorage.exception.ErrorInputData;
import ru.netology.filestorage.exception.FileStorageError;
import ru.netology.filestorage.exception.UnauthorizedError;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final String INPUT_MSG = "Ошибка ввода данных";
    private static final String UNAUTHORIZED_MSG = "Ошибка авторизации";
    private static final String SERVER_MSG = "Ошибка работы сервиса";
    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<String> handleErrorInputData(ErrorInputData e) {
        return new ResponseEntity<>(String.format("%s: %s", INPUT_MSG, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedError.class)
    public ResponseEntity<String> handleUnauthorizedError(UnauthorizedError e) {
        return new ResponseEntity<>(String.format("%s: %s", UNAUTHORIZED_MSG, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageError.class)
    public ResponseEntity<String> handleFileStorageError(FileStorageError e) {
        return new ResponseEntity<>(String.format("%s: %s", SERVER_MSG, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
