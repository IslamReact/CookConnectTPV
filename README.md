# TPV (Terminal Punto de Venta) para Restaurantes y Bares

Este proyecto consiste en el desarrollo de una Terminal Punto de Venta (TPV) destinada a comercios con bajo volumen de ingresos, especialmente diseñada para restaurantes y bares que cuentan con poco conocimiento en el ámbito tecnológico o que están dando sus primeros pasos en este sector. La aplicación se basa en Android y proporciona una serie de funcionalidades clave para la gestión eficiente de estos establecimientos.

## Descripción del Proyecto

La TPV está diseñada para facilitar la gestión integral de los locales de restauración, ofreciendo una interfaz intuitiva y fácil de usar para los usuarios. Entre las funcionalidades principales se incluyen:

- **Login**: Los usuarios pueden acceder a la aplicación según su puesto de trabajo, ya sea camarero o cocinero.
  
- **Gestión de Pedidos**: Los camareros pueden tomar pedidos de los clientes y enviarlos directamente a la cocina para su preparación. Los cocineros pueden visualizar los pedidos pendientes y marcarlos como completados.

- **Control de Inventario**: Los camareros pueden verificar el inventario de productos disponibles y notificar a la cocina sobre la agotación de existencias. Además, tienen la capacidad de agregar nuevos productos al inventario.

- **Generación de Facturas**: La aplicación permite generar facturas para los pedidos realizados, manteniendo un historial ordenado por fecha para facilitar la gestión contable.

- **Gestión de Reservas**: Se incluye la funcionalidad para gestionar las reservas de mesas, lo que permite a los usuarios planificar y organizar mejor el espacio del establecimiento.

## Tecnologías Utilizadas

- **Lenguaje Kotlin**: Se utiliza Kotlin para el desarrollo de la aplicación Android, aprovechando su sintaxis concisa y su interoperabilidad con Java.

- **Base de Datos Firestore**: Google Firebase proporciona la base de datos Firestore, que ofrece transacciones ACID (Atomicidad, Consistencia, Aislamiento y Durabilidad), garantizando la integridad y la coherencia de los datos.

## Funcionamiento

### Cocinero
- Inicio de sesión según su puesto.
- Visualización de pedidos pendientes, ordenados por prioridad o tiempo estimado de preparación.
- Marcar pedidos como completados y emitir alertas a los camareros cuando estén listos.

### Camarero
- Inicio de sesión según su puesto.
- Gestión de inventario, notificando la agotación de existencias.
- Toma de pedidos y envío a la cocina.
- Historial de facturas ordenado por fecha.
- Gestión de reservas de mesas.

## Opciones Adicionales

La aplicación también cuenta con un menú hamburguesa que ofrece opciones como ajustes, que permiten personalizar varios aspectos como el volumen de las notificaciones o activar el modo noche. También se incluye una opción para cerrar sesión, ideal para los cambios de usuario entre los turnos de mañana y tarde.

## Instrucciones de Uso

1. **Descargar Repositorio**: Clonar o descargar el repositorio desde GitHub.
2. **Configuración del Entorno**: Configurar el entorno de desarrollo con Android Studio y asegurarse de tener instaladas las dependencias necesarias.
3. **Compilación y Ejecución**: Compilar el proyecto y ejecutar la aplicación en un dispositivo Android o un emulador.
4. **Inicio de Sesión**: Iniciar sesión como camarero o cocinero según el rol del usuario.
5. **Utilizar las Funcionalidades**: Explorar y utilizar las diferentes funcionalidades de la TPV según el puesto de trabajo.

## Contribución

Las contribuciones son bienvenidas. Si deseas mejorar la aplicación o agregar nuevas funcionalidades, no dudes en abrir un pull request en GitHub.

## Licencia

Este proyecto está bajo la Licencia MIT. Para más detalles, consulta el archivo `LICENSE` en el repositorio.

## Contacto

Para cualquier pregunta o sugerencia, puedes ponerte en contacto con el equipo de desarrollo a través de [correo electrónico](mailto:equipo@example.com) o [abrir un issue](https://github.com/usuario/repo/issues) en GitHub.

---
