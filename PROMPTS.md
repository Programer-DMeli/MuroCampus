# Registro de Prompts y Criterio Técnico (PROMPTS.md)

Este documento registra los prompts de mejora utilizados durante el desarrollo de la aplicación y el análisis técnico correspondiente.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
| :--- | :--- | :--- | :--- |
| Implementa un botón **Eliminar** en el detalle de publicación de MuroCampus usando `@DELETE("posts/{id}")` y `Response<Unit>`. Valida la respuesta HTTP y actualiza la lista tras eliminar. Modifica API, Repository, ViewModel y View, respetando la arquitectura MVVM + Repository. | Se añadió `@DELETE` con `Response<Unit>` en Retrofit, validación `isSuccessful` en el Repositorio envuelto en `Result`, acción de borrado en el ViewModel con recarga de lista, y botón Eliminar en la pantalla de detalle. | Se mantuvo la arquitectura en capas (MVVM + Repository) evitando acoplar Retrofit directamente a la UI, manejando la respuesta HTTP como un `Result<Unit>`. | Mantiene la consistencia con el manejo de errores centralizado y el principio de responsabilidad única de la arquitectura. |
| *[Esperando prompt 2]* | *[Resumen de la respuesta]* | *[Lo que adaptaste]* | *[Tu criterio técnico]* |
| *[Esperando prompt 3]* | *[Resumen de la respuesta]* | *[Lo que adaptaste]* | *[Tu criterio técnico]* |
