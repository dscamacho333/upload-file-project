package co.edu.unbosque.UploadFileAPI.service;

import co.edu.unbosque.UploadFileAPI.configurations.ModelMapperConfiguration;
import co.edu.unbosque.UploadFileAPI.dtos.ArchivoDTO;
import co.edu.unbosque.UploadFileAPI.entities.Archivo;
import co.edu.unbosque.UploadFileAPI.exceptions.ArchivoException;
import co.edu.unbosque.UploadFileAPI.exceptions.ArchivoNotFoundException;
import co.edu.unbosque.UploadFileAPI.exceptions.ArchivoVacioException;
import co.edu.unbosque.UploadFileAPI.repository.ArchivoRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioArchivo {

    private final ModelMapperConfiguration modelMapper;
    private final ArchivoRepository archivoRepository;

    public ServicioArchivo(ModelMapperConfiguration modelMapper, ArchivoRepository archivoRepository) {
        this.modelMapper = modelMapper;
        this.archivoRepository = archivoRepository;
    }

    public ArchivoDTO crearArchivo(ArchivoDTO archivoDTO) {
        try {
            if (archivoDTO.getContenido() == null || archivoDTO.getContenido().isEmpty() || archivoDTO.getContenido().isBlank()){
                throw new ArchivoVacioException("No se puede cargar el archivo porque está vacío.");
            }

            Archivo archivo = archivoRepository.save(modelMapper.modelMapper().map(archivoDTO, Archivo.class));
            return modelMapper.modelMapper().map(archivo, ArchivoDTO.class);
        }catch (PersistenceException e){
            throw new ArchivoException("Error al crear el archivo.");
        }
    }

    public ArchivoDTO buscarArchivo(String nombreArchivo) {
        try {
            Optional<Archivo> optionalArchivo = archivoRepository.findByNombre(nombreArchivo);

            if (optionalArchivo.isEmpty()) {
                throw new ArchivoNotFoundException("Archivo no encontrado.");
            }
            return modelMapper.modelMapper().map(optionalArchivo.get(), ArchivoDTO.class);
        } catch (PersistenceException e) {
            throw new ArchivoNotFoundException("Error al buscar el archivo.");
        }
    }

    public ArchivoDTO buscarYSubrayar(ArchivoDTO archivoDTO, String subcadena, String algoritmo) {

        if (algoritmo.equalsIgnoreCase("kmp")) {
           archivoDTO = buscarKMP(archivoDTO, subcadena);
        } else if (algoritmo.equalsIgnoreCase("bm")) {
           archivoDTO = buscarBM(archivoDTO, subcadena);
        } else {
            throw new IllegalArgumentException("Algoritmo no soportado: " + algoritmo);
        }

        return archivoDTO;
    }

    public ArchivoDTO buscarKMP(ArchivoDTO archivoDTO, String subcadena) {
        List<Integer> indices = kmpSearch(archivoDTO.getContenido(), subcadena);
        if (indices.isEmpty()) {
            archivoDTO.setNumeroCoincidencias(0);
            archivoDTO.setContenido(archivoDTO.getContenido());
            return archivoDTO;
        } else {
            String textoSubrayado = subrayarCoincidencias(archivoDTO.getContenido(), subcadena, indices);
            archivoDTO.setNumeroCoincidencias(indices.size());
            archivoDTO.setContenido(textoSubrayado);
            return archivoDTO;
        }
    }

    private List<Integer> kmpSearch(String texto, String subcadena) {
        int n = texto.length();
        int m = subcadena.length();
        List<Integer> indices = new ArrayList<>();
        int[] lps = calcularLPS(subcadena);

        int i = 0, j = 0;
        while (i < n) {
            if (subcadena.charAt(j) == texto.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                indices.add(i - j);  // Coincidencia encontrada
                j = lps[j - 1];
            } else if (i < n && subcadena.charAt(j) != texto.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return indices;
    }

    private int[] calcularLPS(String subcadena) {
        int m = subcadena.length();
        int[] lps = new int[m];
        int len = 0;
        lps[0] = 0;
        int i = 1;
        while (i < m) {
            if (subcadena.charAt(i) == subcadena.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    private String subrayarCoincidencias(String texto, String subcadena, List<Integer> indices) {
        StringBuilder resultado = new StringBuilder(texto);
        int offset = 0;
        for (int indice : indices) {
            int inicio = indice + offset;
            int fin = inicio + subcadena.length();
            resultado.insert(inicio, "<mark>");
            resultado.insert(fin + 6, "</mark>");  // +3 por los caracteres de <u>
            offset += 13;  // Ajuste por los caracteres de <u> y </u>
        }
        return resultado.toString();
    }

    public ArchivoDTO buscarBM(ArchivoDTO archivoDTO, String subcadena) {
        List<Integer> indices = bmSearch(archivoDTO.getContenido(), subcadena);
        if (indices.isEmpty()) {
            archivoDTO.setContenido(archivoDTO.getContenido());
            archivoDTO.setNumeroCoincidencias(0);
            return archivoDTO;
        } else {
            String textoSubrayado = subrayarCoincidencias(archivoDTO.getContenido(), subcadena, indices);
            archivoDTO.setContenido(textoSubrayado);
            archivoDTO.setNumeroCoincidencias(indices.size());
            return archivoDTO;
        }
    }

    private List<Integer> bmSearch(String texto, String subcadena) {
        int n = texto.length();
        int m = subcadena.length();
        List<Integer> indices = new ArrayList<>();
        int[] badCharTable = crearBadCharTable(subcadena);

        int shift = 0;
        while (shift <= (n - m)) {
            int j = m - 1;
            while (j >= 0 && subcadena.charAt(j) == texto.charAt(shift + j)) {
                j--;
            }
            if (j < 0) {
                indices.add(shift);  // Coincidencia encontrada
                shift += (shift + m < n) ? m - badCharTable[texto.charAt(shift + m)] : 1;
            } else {
                shift += Math.max(1, j - badCharTable[texto.charAt(shift + j)]);
            }
        }
        return indices;
    }

    private int[] crearBadCharTable(String subcadena) {
        final int TAM = 256;
        int[] badCharTable = new int[TAM];
        Arrays.fill(badCharTable, -1);
        for (int i = 0; i < subcadena.length(); i++) {
            badCharTable[subcadena.charAt(i)] = i;
        }
        return badCharTable;
    }

}
