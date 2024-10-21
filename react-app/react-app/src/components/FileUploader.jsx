import React, { useState } from "react";
import axios from "axios";

function FileUploader() {
  const [archivo, setArchivo] = useState(null); // Guardar el archivo
  const [preview, setPreview] = useState(""); // Previsualizar el contenido del archivo
  const [mensaje, setMensaje] = useState(""); // Mensaje de éxito o error
  const [subcadena, setSubcadena] = useState(""); // Subcadena a buscar
  const [resultado, setResultado] = useState(""); // Resultado de la búsqueda
  const [algoritmo, setAlgoritmo] = useState("kmp"); // Algoritmo seleccionado (por defecto KMP)

  // Manejador para cuando el usuario selecciona un archivo
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file && file.type === "text/plain") {
      setArchivo(file);
      const reader = new FileReader();
      reader.onload = (event) => {
        setPreview(event.target.result); // Previsualizar el contenido del archivo
      };
      reader.readAsText(file); // Leer el archivo como texto
    } else {
      setMensaje("Por favor, selecciona un archivo .txt");
    }
  };

  // Función para manejar la carga del archivo
  const handleUpload = async () => {
    if (!archivo) {
      setMensaje("No has seleccionado ningún archivo.");
      return;
    }

    const formData = new FormData();
    formData.append("archivo", archivo); // Adjuntar el archivo en el formData

    try {
      await axios.post("http://localhost:8080/api/archivo/crear", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      setMensaje("Archivo subido exitosamente");
    } catch (error) {
      setMensaje("Error al subir el archivo");
      console.error(error); // Log del error para depuración
    }
  };

  // Manejador para buscar subcadenas
  const handleSearch = async () => {
    if (!archivo) {
      setMensaje("Por favor, sube un archivo primero.");
      return;
    }
    if (!subcadena) {
      setMensaje("Por favor, ingresa una subcadena para buscar.");
      return;
    }

    // Registros para depuración
    console.log("Nombre del archivo:", archivo.name);
    console.log("Subcadena a buscar:", subcadena);
    console.log("Contenido del archivo:", preview);

    try {
      const response = await axios.get("http://localhost:8080/api/archivo/buscar-subcadena", {
        params: {
          nombre: archivo.name,
          subcadena,
          algoritmo // Usar el algoritmo seleccionado
        }
      });
      
      console.log("Respuesta de la API:", response.data); // Log de respuesta de la API
      setResultado(response.data.contenido); // Asignar contenido subrayado a resultado
      setMensaje(`Se encontraron ${response.data.numeroCoincidencias} coincidencias.`);
    } catch (error) {
      setMensaje("Error al buscar la subcadena. " + error.response?.data?.message || "Error desconocido.");
      console.error(error); // Log del error para depuración
    }
  };

  return (
    <>
    
        <>
        <nav className="navbar">
          <h2>¡ B I E N V E N I D O !</h2>
        </nav>
      </>

    <div className="file-uploader">
      <h2>Subir archivo .txt</h2>
      <input type="file" accept=".txt" onChange={handleFileChange} />
      {preview && (
        <div>
          <h3>Previsualización del archivo:</h3>
          <pre>{preview}</pre>
        </div>
      )}
      <button onClick={handleUpload}>Subir archivo</button>
      {mensaje && <p>{mensaje}</p>}

      {/* Input para buscar subcadenas */}
      <input
        type="text"
        placeholder="Buscar subcadena"
        value={subcadena}
        onChange={(e) => setSubcadena(e.target.value)} />

      {/* Select para elegir el algoritmo */}
      <select value={algoritmo} onChange={(e) => setAlgoritmo(e.target.value)}>
        <option value="kmp">KMP</option>
        <option value="bm">Boyer-Moore (BM)</option>
      </select>

      <button onClick={handleSearch}>Buscar</button>

      {/* Mostrar el resultado de la búsqueda */}
      {resultado && (
        <div>
          <h3>Resultado de la búsqueda:</h3>
          <div dangerouslySetInnerHTML={{ __html: resultado }} />
        </div>
      )}
    </div><div class="circles">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
        <div class="circle circle-4"></div>
        <div class="circle circle-5"></div>
        <div class="circle circle-6"></div>
        <div class="circle circle-7"></div>
        <div class="circle circle-8"></div>
        <div class="circle circle-9"></div>
        <div class="circle circle-10"></div>
      </div>
      
      
      
      
      </>

  );
}

export default FileUploader;
