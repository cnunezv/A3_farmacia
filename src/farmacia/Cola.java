package farmacia;

/**
 * Cola FIFO (First In, First Out): el primero en llegar es el primero en ser atendido.
 *
 *   frente                              fin
 *     |                                  |
 *   [ A ] <-> [ B ] <-> [ C ] <-> [ D ]
 *   sale por aquí               entra por aquí
 *
 * Se encola por el "fin" y se desencola por el "frente"; ambas operaciones son O(1).
 */
public class Cola {

    private int cuenta;
    private Nodo frente;   // primer elemento (el próximo en salir)
    private Nodo fin;      // último elemento (el que llegó de último)

    public Cola() {
        cuenta = 0;
        frente = null;
        fin = null;
    }

    public boolean esVacia() {
        return cuenta == 0;
    }

    public int tamanio() {
        return cuenta;
    }

    /** Encolar: agrega el dato al final de la cola. */
    public void encolar(Object dato) throws Exception {
        if (dato == null) {
            throw new Exception("El dato no puede ser nulo");
        }
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.setDerecha(nuevo);
            nuevo.setIzquierda(fin);
            fin = nuevo;
        }
        cuenta++;
    }

    /** Desencolar: quita y retorna el dato del frente. */
    public Object desencolar() throws Exception {
        if (esVacia()) {
            throw new Exception("La cola está vacía");
        }
        Object dato = frente.getDato();
        frente = frente.getDerecha();
        if (frente != null) {
            frente.setIzquierda(null);
        } else {
            fin = null;   // la cola quedó vacía
        }
        cuenta--;
        return dato;
    }

    /** Consulta el dato del frente sin quitarlo. */
    public Object peek() throws Exception {
        if (esVacia()) {
            throw new Exception("La cola está vacía");
        }
        return frente.getDato();
    }

    /** true si algún elemento de la cola es igual (equals) al dato. Solo recorre, no modifica. */
    public boolean contiene(Object dato) {
        Nodo actual = frente;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getDerecha();
        }
        return false;
    }

    public void limpiar() {
        frente = null;
        fin = null;
        cuenta = 0;
    }

    /** Elementos desde el frente hasta el fin, numerados en orden de atención. */
    public String mostrar() {
        if (esVacia()) {
            return "(cola vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = frente;
        int turno = 1;
        while (actual != null) {
            sb.append(turno).append(". ").append(actual.getDato());
            if (actual == frente) {
                sb.append("   <- frente (siguiente en atender)");
            }
            sb.append('\n');
            actual = actual.getDerecha();
            turno++;
        }
        return sb.toString();
    }
}
