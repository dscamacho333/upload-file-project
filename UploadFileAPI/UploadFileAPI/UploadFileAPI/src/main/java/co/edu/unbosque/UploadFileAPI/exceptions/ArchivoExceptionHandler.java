package co.edu.unbosque.UploadFileAPI.exceptions;

import co.edu.unbosque.UploadFileAPI.entities.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ArchivoExceptionHandler {

    @ExceptionHandler(ArchivoException.class)
    public ResponseEntity<BaseResponse> archivoExceptionHandler(ArchivoException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(new BaseResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(ArchivoNotFoundException.class)
    public ResponseEntity<BaseResponse> archivoNotFoundExceptionHandler(ArchivoNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ArchivoVacioException.class)
    public ResponseEntity<BaseResponse> archivoVacioExceptionHandler(ArchivoVacioException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(new BaseResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

}
