package farmacia;

/**
 * Pila LIFO (Last In, First Out): el último en entrar es el primero en salir.
 *
 *   [ D ]  <- cima (último apilado, primero en salir)
 *     |  izquierda = el de abajo
 *   [ C ]
 *     |
 *   [ B ]
 *     |
 *   [ A ]  <- fondo
 *
 * Cada nodo apunta con "izquierda" al nodo de abajo y con "derecha" al de arriba.
 * Apilar y desapilar trabajan solo en la cima: O(1).
 */
public class Pila {

    private int cuenta;
    private Nodo cima;   // el tope de la pila

    public Pila() {
        cuenta = 0;
        cima = null;
    }

    public boolean esVacia() {
        return cuenta == 0;
    }

    public int tamanio() {
        return cuenta;
    }

    /** Apilar (push): pone el dato encima de la cima. */
    public void apilar(Object dato) throws Exception {
        if (dato == null) {
            throw new Exception("El dato no puede ser nulo");
        }
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            cima = nuevo;
        } else {
            nuevo.setIzquierda(cima);   // el nuevo queda encima de la cima actual
            cima.setDerecha(nuevo);
            cima = nuevo;
        }
        cuenta++;
    }

    /** Desapilar (pop): quita y retorna el dato de la cima. */
    public Object desapilar() throws Exception {
        if (esVacia()) {
            throw new Exception("La pila está vacía");
        }
        Object dato = cima.getDato();
        cima = cima.getIzquierda();   // baja al nodo de abajo
        if (cima != null) {
            cima.setDerecha(null);
        }
        cuenta--;
        return dato;
    }

    /** Consulta el dato de la cima sin quitarlo. */
    public Object peek() throws Exception {
        if (esVacia()) {
            throw new Exception("La pila está vacía");
        }
        return cima.getDato();
    }

    /**
     * Distancia del dato a la cima: 1 si está en la cima, 2 si está justo debajo, etc.
     * Retorna -1 si no está. Recorre desde la cima hacia abajo sin modificar la pila.
     */
    public int buscar(Object dato) {
        Nodo actual = cima;
        int distancia = 1;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return distancia;
            }
            actual = actual.getIzquierda();
            distancia++;
        }
        return -1;
    }

    public boolean contiene(Object dato) {
        return buscar(dato) != -1;
    }

    public void limpiar() {
        cima = null;
        cuenta = 0;
    }

    /** Elementos desde la cima hasta el fondo. */
    public String mostrar() {
        if (esVacia()) {
            return "(pila vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = cima;
        int nivel = 1;
        while (actual != null) {
            sb.append(nivel).append(". ").append(actual.getDato());
            if (actual == cima) {
                sb.append("   <- cima (último procesado)");
            }
            sb.append('\n');
            actual = actual.getIzquierda();
            nivel++;
        }
        return sb.toString();
    }
}
