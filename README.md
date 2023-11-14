
Leishmaniapp Android application API 27 

# Manual de usuario :iphone:

**Table of Contents**

1. [Descripción](#Descripción)
2. [Tecnologías usadas](#Tecnologíasusadas)
3. [Uso](#uso)
4. [Estado del Proyecto](#estado-del-proyecto)



# Descripción

Bienvenido al manual de usuario de LeishmaniApp. Una aplicación de apoyo diagnóstico de la Leishmaniasis y otras enfermedades parasitarias.
LeishmaniApp se compone de tres elementos principales que trabajan conjuntamente para conformar una herramienta de diagnóstico para especialistas
como parte de un proyecto marco denominado "_inteligencia artificial para apoyo en el diagnóstico de leishmaniasis cutánea_".
LeishmaniApp cuenta con un modelo de detección de mácrofagos alojado en una arquitectura de procesamiento en nube AWS, al cual se conecta a
una aplicación móvil que permite al usuario tomar desde el dispositivo imágenes de microscopía para ser analizadas.
La aplicación también cuenta con un módulo de gestión de pacientes a quienes se le realicen análisis y con un módulo de exportación e importación
de modo que la información recolectada en la base de datos de la aplicación pueda ser distribuida a quien interese (laboratorios y/o especialistas).
# Tecnologías usadas
El stack tecnológico usado en este proyecto es

***Aplicación***
* Android versión 8.1 API 27
* Jetpack Compose
* Jetpack Room (SQLite3)
* Jetpack Startup
* Jetpack LiveData
* Jetpack ViewModel
* Jetpack WorkManager
* Jetpack Navigation
* Jetpack Hilt
* Jetpack CameraX
* KotlinX
* AWS Amplify
  
***Arquitectura en nube***
* DynamoDB
* Bucket S3
*Lambda
*Cognito
*API gateway
*SQS/SNS
*Step functions

  
***Módelo de detección de macrófagos***
* OpenCV
* Python 3.9

# Uso
A continuación, se describe un tutorial de las funciones de la aplicación y su navegabilidad

***Inicio***

1. Presione en comenzar

![inicio](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/e1e615be-f127-438f-b21f-1afff6d412bf)

2. Autenticarse con las credenciales dadas por el INS (Instituto Nacional de Salud)
   

![autenticación](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/bb52f7ef-5d56-4555-a7de-b6926eeaa92f)

3. Seleccionar la enfermedad a diagnosticar

![selección enfermedad](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/30bbe753-74c9-4fe5-8268-3b461714030b)

4. Menú principal de la aplicación con la enfermedad seleccionada y sus módulos

![Menú principal](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/aabb6a1c-4d2d-4a9d-9342-e1b51eba6d6f)

***Pacientes*** :frowning_person: :frowning_man:
1. Cuando se elige la opción de pacientes, la aplicación redirige a la lista de pacientes regitsrados en la aplicación.
   En la barra de búsqueda filtre el paciente que requiera por su número de cédula
   Si desea agregar un nuevo paciente, presione el icono de __más__ junto a la barra de búsqueda


      _Si la lista de pacientes está vacía, le dará la opción de agregar un paciente_
   
![lista pacientes](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/43017a52-9394-4d46-880e-8f34a976aca2)

3. Para agregar un paciente debe ingresar su nombre, tipo de documento de identidad y número de documento de identidad,
 luego presione en _crear paciente_


   ![agregar paciente](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/2e74a425-c497-48a0-9747-7ee2a58972c3)

4. Para ver la lista de informes de diagnóstico asociados a un paciente, seleccionelo de la lista de pacientes. También podrá iniciar un diagnóstico desde allí
   
   ![sin Diagnosticos](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/a01f0934-2df4-4f00-8cc3-76b2250b2b13)
  

***Diagnóstico*** :camera:
1. Luego de haber presionado en el menú principal _iniciar diagnóstico_, la aplicación se dirige a lista de pacientes para asociar un paciente
   al análisis y abre la cámara para tomar la primera fotografía a analizar
   
![cámara](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/9aa61ebe-7888-487e-8160-fd30f0fadec2)

3. Cuando la fotografía ha sido tomada, se observa una pantalla con la imagen,
   el nombre del paciente asociado y el número de campo de la imagen. Aquí puede

   - Repetir la imagen: Puede volver a repetir la fotografía
   - Analizar la imagen: Enviar la imagén a procesar
   - Ver los resultados: Tabla de resultados del análisis de la fotografía por el modelo y el especialista
   - Finalizar toma de imágenes: Finalizar la toma de imágenes
     
  ![imagen tomada](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/21009f05-a37f-486a-8d1a-09ad51b73c53)
  - Tabla de resultados
      Debe ingresar los resultados del especialista y esperar a que el módelo de sus resultados. De lo contrario, no podrá continuar con el diagnóstico
    
   ![tabla de resultados](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/34f723df-f4c1-4097-8b5e-96869a9e6cab)

4. Una vez el modelo haya respondido, se marcará una _X_ con el centro de masa de/los macrófagos detectados.
   Podrá editar las respuestas del módelo de diagnóstico al presionar _editar la imagen_

   ![imagen analizada](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/1ffcc2bc-c0c4-4667-bc6d-13a47ffa1e61)

5. Podrá desmarcar los macrófagos encontrados por el módelo y eliminarlos o descartas los cambios
   ![borrar macrófagos](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/4706e001-3f15-4323-888f-811eeacb0e28)




5. Podrá tomar cuantas imágenes decida hasta que presione finalizar, donde podrá ver un resumen de las imágenes tomadas para ese análisis de diagnóstico

6. Cuando haya finalizado el diagnóstico definitivamente, podrá realizar observaciones a cerca del análisis hecho
   ![observaciones](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/4cf27434-1db7-4be2-8894-92046ac11393)
  
7. Luego de ingresar las observaciones y aceptar los cambios, podrá visualizar el informe d e diagnóstico final
   Aquí, podrá compartir el informe a través de formato PDF a otras aplicaciones

   ![informe de diagnóstico](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/1f8d8b66-1b24-4c15-83de-46d64928058b)




***Pendientes***
1. Si el análisis es realizado bajo malas condiciones de internet, la aplicación permitirá seguir tomando fotografías
 e irá encolando las imágenes par analizarlas cuando detecte internet

![sin internet](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/aee20749-fcce-492c-8f29-0dfa4978fadd)

2. Al finalizar el diagnóstico, la aplicación solicitará procesar las imagénes en segundo plano 

![procesar en segundo plano](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/99557790-ef5d-4bf9-a5b3-437ae17f9e9d)

3. Para verificar el estado del diagnóstico, en el menú principal ingresar a la sección de pendientes. Aquí, se visualiza la lista de diagnósticos,
su estado y un botón de sincronizar que permite actualizar los cambios si se ha detectado internet y se han analizado imágenes


![pendientes](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/dffe59d2-6c28-481e-a625-f71bd2e6446f)

1. Para acceder a los detalles de cada diagnóstico en la lista de pendientes, seleccionar uno
Dentro del detalle, podrá verificar las imágenes analizadas y editarlas

![detalle del diagnóstico](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/bf674c7d-d7c3-443d-9938-df05e07af1be)



***Exportación e importación***

1. Aquí podrá exportar la base de datos de la aplicación como archivo .SQLite o importar una
   ![image](https://github.com/leishmaniapp/leishmaniapp-android/assets/70526846/062cd69d-4053-4103-b113-ce30c9107038)

 # Estado del proyecto 

:construction: _Proyecto en construcción_ :construction:

Como se menciona en la descripción, esta es una aplicación bajo un proyecto marco del instituto nacional de salud que sigue en construcción.
Se proyecta la detección de parasitos de Leishmaniasis y agregar otras enfermedades parasitarias a la aplicación
- El presente proyecto der grado garantiza la extensibilidad de la arquitectura de la aplicación para trabajos futuros



