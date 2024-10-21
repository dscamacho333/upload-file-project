package co.edu.unbosque.UploadFileAPI.exceptions;

public class ArchivoNotFoundException extends RuntimeException{

    public ArchivoNotFoundException(String mensaje){
        super(mensaje);
    }
}
