package co.edu.unbosque.UploadFileAPI.exceptions;

public class ArchivoVacioException extends RuntimeException{

    public ArchivoVacioException(String mensaje){
        super(mensaje);
    }
}
