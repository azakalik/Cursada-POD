# Ejercicio 1 - Programación Secuencial
- Descargar el paquete project-concurrency.zip del módulo en el campus.
- Descomprimir e incorporar al IDE.
- Se debe implementar el servicio GenericService en la clase GenericServiceImpl
asegurándose de que les corran los test de unidad ubicados en
GenericServiceTest.

# Ejercicio 2 - Threads
- Incorporar al proyecto concurrency un Test (ThreadTest) que se encargue
de instanciar un GenericService pasárselo a un Thread que realice 5
visitas e imprima en pantalla el conteo de visitas final.
- En la misma clase, escribir dos nuevos test que realicen el mismo proceso
usando un Runnable y una Lambda respectivamente

# Ejercicio 3 - Executor
- Agregar al proyecto currency un Test (ExecutorTest).
- Generar un test que utilizando un CachedThreadPool y un GenericService genere
un Future que registre 5 visitas y retorne el conteo. El test debe verificar que el
resultado sea el esperado.
- Copiar la clase del ejercicio 2 y cambiar para que al terminar de ejecutar los
Threads se hagan las assertions para comprobar que se hayan generado las 5
visitas