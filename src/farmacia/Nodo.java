package farmacia;

/**
 * Nodo doblemente enlazado: guarda un dato y dos referencias a sus vecinos.
 *
 *   izquierda <- [ dato ] -> derecha
 *
 * Lo usan la Lista, la Cola y la Pila. Basado en el ejemplo del tutor
 * (Estructuras de Datos, UdeC).
 */
public class Nodo {

    private Object dato;
    private Nodo izquierda;   // vecino anterior
    private Nodo derecha;     // vecino siguiente

    public Nodo() {
    }

    public Nodo(Object dato) {
        this.dato = dato;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public Nodo getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(Nodo vecino) {
        this.izquierda = vecino;
    }

    public Nodo getDerecha() {
        return derecha;
    }

    public void setDerecha(Nodo vecino) {
        this.derecha = vecino;
    }
}
