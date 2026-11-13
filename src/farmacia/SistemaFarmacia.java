package farmacia;

/**
 * Sistema de gestión de fórmulas médicas de una farmacia.
 *
 *   listaGeneral        (Lista) -> registro completo de todas las fórmulas
 *   colaPendientes      (Cola)  -> fórmulas por dispensar, en orden de llegada (FIFO)
 *   historialProcesados (Pila)  -> fórmulas dispensadas; la cima es la última (LIFO)
 *
 * Las tres estructuras guardan REFERENCIAS al mismo objeto FormulaMedica: si se
 * cambia el estado de una fórmula, el cambio se ve en todas.
 */
public class SistemaFarmacia {

    private final Lista listaGeneral = new Lista();
    private final Cola colaPendientes = new Cola();
    private final Pila historialProcesados = new Pila();

    /**
     * 1. Registrar: la fórmula entra a la lista general y a la cola de pendientes.
     */
    public FormulaMedica registrar(String numero, String documento, String paciente,
                                   String medicamento, int cantidad) throws Exception {
        FormulaMedica formula = new FormulaMedica(numero, documento, paciente, medicamento, cantidad);
        if (listaGeneral.contiene(formula)) {   // equals compara por número de fórmula
            throw new Exception("Ya existe una fórmula con el número " + formula.getNumeroFormula());
        }
        listaGeneral.agregar(formula);
        colaPendientes.encolar(formula);
        return formula;
    }

    /**
     * 4. Procesar (dispensar): sale la fórmula del frente de la cola y va a la cima del historial.
     */
    public FormulaMedica procesarSiguiente() throws Exception {
        if (colaPendientes.esVacia()) {
            throw new Exception("No hay fórmulas pendientes por dispensar");
        }
        FormulaMedica procesado = (FormulaMedica) colaPendientes.desencolar();
        procesado.setEstado(FormulaMedica.DISPENSADA);
        historialProcesados.apilar(procesado);
        return procesado;
    }

    /**
     * 8. Deshacer: la última fórmula dispensada (cima de la pila) vuelve a la cola
     * de pendientes. Según la regla del ejercicio se encola, así que queda al final.
     */
    public FormulaMedica deshacerUltimoProcesamiento() throws Exception {
        if (historialProcesados.esVacia()) {
            throw new Exception("No hay procesamientos para deshacer");
        }
        FormulaMedica ultimo = (FormulaMedica) historialProcesados.desapilar();
        ultimo.setEstado(FormulaMedica.PENDIENTE);
        colaPendientes.encolar(ultimo);
        return ultimo;
    }

    /* ---------------- consultas ---------------- */

    public String mostrarTodas(boolean haciaAtras) {
        return haciaAtras ? listaGeneral.mostrarAtras() : listaGeneral.mostrarAdelante();
    }

    public String mostrarPendientes() {
        return colaPendientes.mostrar();
    }

    public String mostrarHistorial() {
        return historialProcesados.mostrar();
    }

    public int totalRegistradas() {
        return listaGeneral.cuentaElementos();
    }

    public int totalPendientes() {
        return colaPendientes.tamanio();
    }

    public int totalDispensadas() {
        return historialProcesados.tamanio();
    }
}
