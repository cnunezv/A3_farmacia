package farmacia;

/**
 * Lista doblemente enlazada de objetos.
 *
 *   primero                                ultimo
 *     |                                      |
 *   [ A ] <-> [ B ] <-> [ C ] <-> [ D ]  -> null
 *
 * Cada nodo conoce a su vecino de la izquierda (anterior) y de la derecha
 * (siguiente), por eso la lista se puede recorrer en los dos sentidos.
 * Los índices van de 0 a cuenta - 1.
 */
public class Lista {

    private int cuenta;
    private Nodo primero;
    private Nodo ultimo;

    public Lista() {
        cuenta = 0;
        primero = null;
        ultimo = null;
    }

    public boolean esVacia() {
        return cuenta == 0;
    }

    public int cuentaElementos() {
        return cuenta;
    }

    /** Agrega el dato al final de la lista. O(1) gracias a la referencia "ultimo". */
    public void agregar(Object dato) throws Exception {
        validarDato(dato);
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setDerecha(nuevo);    // el último actual apunta al nuevo
            nuevo.setIzquierda(ultimo);  // el nuevo apunta hacia atrás al último actual
            ultimo = nuevo;              // el nuevo pasa a ser el último
        }
        cuenta++;
    }

    /** Dato que está en la posición indicada. */
    public Object buscarDato(int indice) throws Exception {
        return buscar(indice).getDato();
    }

    /** Posición de la primera aparición del dato (usa equals). Lanza excepción si no está. */
    public int buscarDato(Object dato) throws Exception {
        int indice = indiceDe(dato);
        if (indice == -1) {
            throw new Exception("El elemento no está en la lista");
        }
        return indice;
    }

    /** true si algún elemento es igual (equals) al dato. */
    public boolean contiene(Object dato) {
        return indiceDe(dato) != -1;
    }

    /** Recorre de primero a último (siguiendo las referencias "derecha"). */
    public String mostrarAdelante() {
        if (esVacia()) {
            return "(lista vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = primero;
        int i = 0;
        while (actual != null) {
            sb.append(i).append(". ").append(actual.getDato()).append('\n');
            actual = actual.getDerecha();
            i++;
        }
        return sb.toString();
    }

    /** Recorre de último a primero (siguiendo las referencias "izquierda"). */
    public String mostrarAtras() {
        if (esVacia()) {
            return "(lista vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = ultimo;
        int i = cuenta - 1;
        while (actual != null) {
            sb.append(i).append(". ").append(actual.getDato()).append('\n');
            actual = actual.getIzquierda();
            i--;
        }
        return sb.toString();
    }

    /* ---------------- métodos auxiliares ---------------- */

    /**
     * Nodo de la posición indicada. Como la lista es doble, si el índice está en
     * la primera mitad se recorre desde "primero" hacia la derecha, y si está en
     * la segunda mitad desde "ultimo" hacia la izquierda: a lo sumo n/2 pasos.
     */
    private Nodo buscar(int indice) throws Exception {
        if (esVacia()) {
            throw new Exception("La lista está vacía");
        }
        if (indice < 0 || indice >= cuenta) {
            throw new Exception("La posición " + indice + " está fuera del rango 0 - " + (cuenta - 1));
        }
        Nodo actual;
        if (indice < cuenta / 2) {
            actual = primero;
            for (int i = 0; i < indice; i++) {
                actual = actual.getDerecha();
            }
        } else {
            actual = ultimo;
            for (int i = cuenta - 1; i > indice; i--) {
                actual = actual.getIzquierda();
            }
        }
        return actual;
    }

    /** Posición del dato o -1 si no está. Recorrido secuencial O(n). */
    private int indiceDe(Object dato) {
        Nodo actual = primero;
        int i = 0;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return i;
            }
            actual = actual.getDerecha();
            i++;
        }
        return -1;
    }

    private void validarDato(Object dato) throws Exception {
        if (dato == null) {
            throw new Exception("El dato no puede ser nulo");
        }
    }
}
