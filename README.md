# EliteGPUs

EliteGPUs es una aplicacion nativa para Android desarrollada en Kotlin que permite la gestion, publicacion y visualizacion de tarjetas graficas (GPUs) en un catalogo digital interactivo sincronizado en tiempo real con Firebase Firestore.

## Caracteristicas Principales

- Catalogo en tiempo real: Visualizacion de productos con actualizacion automatica mediante instantaneas (snapshots) de Firestore.
- Captura de evidencia fotografica: Integracion con la camara del dispositivo para tomar fotos de las tarjetas graficas y convertirlas a formato Base64 optimizado para almacenamiento directo en la base de datos.
- Notificaciones locales: Disparo de alertas en la barra de estado del sistema cuando se publica un nuevo modelo en el catalogo.
- Control de administracion y roles: Panel adaptativo que permite publicar o eliminar productos segun los permisos asignados.
- Borrado sincronizado: Eliminacion inmediata de registros tanto de la vista local como de la coleccion en la nube de Firebase.
- Soporte para mensajeria en la nube: Estructura preparada para la recepcion de notificaciones push a traves de Firebase Cloud Messaging (FCM).

## Arquitectura y Tecnologias

- Lenguaje: Kotlin
- Plataforma: Android SDK (Min SDK 24, Target SDK 34)
- Base de datos en la nube: Cloud Firestore
- Procesamiento de imagenes: Glide y decodificacion de Bitmap en Base64
- Componentes de interfaz: Material Design Components, CardView, RecyclerView y ScrollView
- Notificaciones: NotificationChannel y NotificationCompat

## Estructura de Archivos Clave

- LoginActivity.kt: Controla el acceso directo y la configuracion del rol de Administrador en SharedPreferences.
- MainActivity.kt: Gestiona la vista principal, la conexion en tiempo real con la coleccion Gpus y el contenedor del catalogo.
- AddGpuActivity.kt: Permite la captura de imagen con la camara, la compresion binaria y el registro del documento en Firestore.
- GpuAdapter.kt: Vincula la lista de tarjetas graficas con la interfaz de usuario, decodifica las imagenes en memoria y gestiona la eliminacion directa de cada registro.
- GpuModel.kt: Clase de datos que define los atributos de cada tarjeta grafica (id, nombre, precio e imagenBase64).
- FCMService.kt: Servicio en segundo plano para procesar notificaciones remotas procedentes de Firebase.

## Configuracion y Requisitos

1. Clonar el repositorio en la maquina local.
2. Abrir el proyecto en Android Studio.
3. Descargar el archivo google-services.json desde la consola de Firebase correspondiente a este paquete e incluirlo dentro del directorio app/.
4. Configurar las reglas de Firestore Database en modo de lectura y escritura para pruebas o produccion:

rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}

5. Sincronizar el proyecto con los archivos de Gradle y compilar en un dispositivo fisico o emulador con conexion a internet.
