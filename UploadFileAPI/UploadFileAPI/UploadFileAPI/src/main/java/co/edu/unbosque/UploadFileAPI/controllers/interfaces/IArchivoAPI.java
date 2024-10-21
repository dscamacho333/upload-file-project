package co.edu.unbosque.UploadFileAPI.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/archivo")
public interface IArchivoAPI {

    @PostMapping("/crear")
    ResponseEntity<?> crear(@RequestParam MultipartFile archivo);

    @GetMapping("/buscar/{nombre}")
    ResponseEntity<?> buscarPorNombre(@PathVariable String nombre);

    @GetMapping("/buscar-subcadena")
    ResponseEntity<?> buscarSubcadena(@RequestParam String nombre, @RequestParam String subcadena, @RequestParam String algoritmo);


}
