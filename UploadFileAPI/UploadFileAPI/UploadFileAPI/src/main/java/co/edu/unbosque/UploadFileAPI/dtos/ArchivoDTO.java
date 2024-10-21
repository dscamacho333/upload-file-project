package co.edu.unbosque.UploadFileAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchivoDTO {

    private Long id;
    private String nombre;
    private String contenido;
    private int numeroCoincidencias;

}
