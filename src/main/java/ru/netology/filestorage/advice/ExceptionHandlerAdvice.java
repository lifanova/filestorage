package ru.netology.filestorage.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.filestorage.exception.ErrorInputData;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final String INPUT_MSG = "Ошибка ввода данных";
    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<String> handleErrorInputData(ErrorInputData e) {
        return new ResponseEntity<>(String.format("%s: %s", INPUT_MSG, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
