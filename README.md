# Farmacia - Gestión de fórmulas médicas con Lista, Cola y Pila

Actividad de Aprendizaje de la Unidad 2 de **Estructuras de Datos** — Ingeniería de Software, Universidad de Cartagena.
**Caso asignado: 14. Farmacia** — entidad `FormulaMedica`, identificador: número de fórmula.

Autor: Carlos Andrés Núñez Vargas

## Descripción

Una farmacia recibe fórmulas médicas que deben dispensarse en orden de llegada. El sistema:

| Estructura | Variable | Uso en la farmacia |
| --- | --- | --- |
| **Lista** doblemente enlazada | `listaGeneral` | Registro completo de todas las fórmulas (pendientes, dispensadas y canceladas) |
| **Cola** (FIFO) | `colaPendientes` | Fórmulas por dispensar, atendidas en orden de llegada |
| **Pila** (LIFO) | `historialProcesados` | Fórmulas dispensadas; la cima es la última, para poder deshacerla |

Las tres estructuras están implementadas desde cero con la clase `Nodo` (referencias `izquierda` y `derecha`), tomando como base los ejemplos del tutor. No se usan colecciones de Java (`ArrayList`, `LinkedList`, `Queue`, `Stack`).

## Estructura del proyecto

```
src/farmacia/
├── Nodo.java              Nodo doblemente enlazado (dato, izquierda, derecha)
├── Lista.java             Lista doblemente enlazada
├── Cola.java              Cola FIFO
├── Pila.java              Pila LIFO
├── FormulaMedica.java     Entidad del caso (6 atributos, validaciones, equals por número)
├── SistemaFarmacia.java   Lógica del sistema + menú de consola (main)
└── PruebasFarmacia.java   38 casos de prueba sin librerías externas
```

## Cómo ejecutar

Requiere Java 11 o superior.

```bash
# Compilar
javac -encoding UTF-8 -d out src/farmacia/*.java

# Ejecutar el sistema (menú)
java -cp out farmacia.SistemaFarmacia

# Ejecutar las pruebas
java -cp out farmacia.PruebasFarmacia
```

Al iniciar, el programa pregunta si se cargan 5 fórmulas de ejemplo (F-101 a F-105).

### Menú

```
 1. Registrar fórmula médica
 2. Ver todas las fórmulas registradas
 3. Ver fórmulas pendientes
 4. Dispensar siguiente fórmula
 5. Ver historial de fórmulas dispensadas
 6. Buscar fórmula por número
 7. Cancelar fórmula pendiente
 8. Deshacer última dispensación
 9. Ver cantidad de fórmulas
10. Salir
```

## Reglas implementadas

```java
// Registro
listaGeneral.agregar(formula);
colaPendientes.encolar(formula);

// Procesamiento (dispensar)
FormulaMedica procesado = (FormulaMedica) colaPendientes.desencolar();
procesado.setEstado(FormulaMedica.DISPENSADA);
historialProcesados.apilar(procesado);

// Deshacer
FormulaMedica ultimo = (FormulaMedica) historialProcesados.desapilar();
ultimo.setEstado(FormulaMedica.PENDIENTE);
colaPendientes.encolar(ultimo);
```

**Cancelar pendiente** usa una **cola auxiliar**: se desencolan todas las fórmulas, la que se cancela se aparta y las demás pasan a la auxiliar; luego se devuelven a la cola original en el mismo orden. La fórmula cancelada sigue en la lista general con estado `CANCELADA`.

## Validaciones

- Número de fórmula obligatorio y único (se normaliza: `f-101` = `F-101`).
- Documento del paciente de 5 a 12 dígitos; nombre y medicamento obligatorios; cantidad mayor que cero.
- No se puede dispensar si la cola está vacía ni deshacer si el historial está vacío.
- Solo se cancelan fórmulas pendientes: una dispensada debe deshacerse primero.
- Todas las estructuras rechazan datos `null` y posiciones fuera de rango.

## Complejidad de las operaciones principales

| Operación | Estructura | Complejidad |
| --- | --- | --- |
| Registrar (agregar + encolar) | Lista + Cola | O(n) por la verificación de duplicados; agregar y encolar son O(1) |
| Dispensar (desencolar + apilar) | Cola + Pila | O(1) |
| Deshacer (desapilar + encolar) | Pila + Cola | O(1) |
| Buscar por número | Lista | O(n) |
| Cancelar con cola auxiliar | Cola | O(n) |
| Acceder por índice | Lista | O(n/2): recorre desde el extremo más cercano |
