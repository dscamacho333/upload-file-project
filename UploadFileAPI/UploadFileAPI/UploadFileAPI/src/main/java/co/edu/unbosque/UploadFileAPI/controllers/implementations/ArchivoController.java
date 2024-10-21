package co.edu.unbosque.UploadFileAPI.controllers.implementations;

import co.edu.unbosque.UploadFileAPI.controllers.interfaces.IArchivoAPI;
import co.edu.unbosque.UploadFileAPI.dtos.ArchivoDTO;
import co.edu.unbosque.UploadFileAPI.service.ServicioArchivo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class ArchivoController implements IArchivoAPI {

    private final ServicioArchivo servicioArchivo;

    public ArchivoController(ServicioArchivo servicioArchivo) {
        this.servicioArchivo = servicioArchivo;
    }

    @Override
    public ResponseEntity<?> crear(MultipartFile archivo) {
        try{
            ArchivoDTO archivoDTO = new ArchivoDTO();
            archivoDTO.setNombre(archivo.getOriginalFilename());
            archivoDTO.setContenido(new String(archivo.getBytes()));
            System.out.println(archivoDTO.getNombre());
            System.out.println(archivoDTO.getContenido());
            servicioArchivo.crearArchivo(archivoDTO);
            return new ResponseEntity<>(archivoDTO, HttpStatus.OK);
        }catch(IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> buscarPorNombre(String nombre) {
        System.out.println(nombre);
        return (new ResponseEntity<>(servicioArchivo.buscarArchivo(nombre), HttpStatus.OK));
    }

    @Override
    public ResponseEntity<?> buscarSubcadena(String nombre, String subcadena, String algoritmo) {
        ArchivoDTO archivoDTO = servicioArchivo.buscarArchivo(nombre);
        System.out.println(nombre);
        System.out.println(subcadena);
        System.out.println(algoritmo);
        archivoDTO = servicioArchivo.buscarYSubrayar(archivoDTO, subcadena, algoritmo);
        System.out.println("Archivo encontrado: " + archivoDTO.getNombre() + "Coincidencias: " + archivoDTO.getNumeroCoincidencias());
        return new ResponseEntity<>(archivoDTO, HttpStatus.OK);
    }
}
