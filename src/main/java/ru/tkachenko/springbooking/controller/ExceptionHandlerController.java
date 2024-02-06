package ru.tkachenko.springbooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tkachenko.springbooking.dto.ErrorResponse;
import ru.tkachenko.springbooking.exception.BookingDateException;
import ru.tkachenko.springbooking.exception.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import ru.tkachenko.springbooking.exception.HotelException;
import ru.tkachenko.springbooking.exception.UserException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException e) {
        log.error("Ошибка при попытке получить сущность", e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getLocalizedMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> notValid(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessage = String.join(";", errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> notFound(UserException e) {
        log.error("Ошибка при регистрации пользователя", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getLocalizedMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Отсутствует параметр запроса: " + parameterName + "!"));
    }

    @ExceptionHandler(BookingDateException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(BookingDateException e) {
        log.error("Ошибка при бронировании", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getLocalizedMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> notAccess(AccessDeniedException e) {
        log.error("Access denied: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Вы не авторизированы, либо у Вас нет доступа!"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Внутренняя ошибка сервера", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Произошла внутренняя ошибка сервера. Пожалуйста, повторите запрос позже."));
    }

    @ExceptionHandler(HotelException.class)
    public ResponseEntity<ErrorResponse> notFound(HotelException e) {
        log.error("Ошибка при работе с Hotel", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getLocalizedMessage()));
    }
}
