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

    /**
     * 6. Buscar en la lista general por número de fórmula.
     * Se usa una fórmula "sonda" que solo tiene el número: como equals compara por
     * número, la lista encuentra la fórmula real y se obtiene con buscarDato(indice).
     */
    public FormulaMedica buscarPorNumero(String numero) throws Exception {
        FormulaMedica sonda = FormulaMedica.conNumero(numero);
        if (sonda.getNumeroFormula().isEmpty()) {
            throw new Exception("Escriba un número de fórmula");
        }
        if (!listaGeneral.contiene(sonda)) {
            throw new Exception("No existe una fórmula con el número " + sonda.getNumeroFormula());
        }
        int indice = listaGeneral.buscarDato(sonda);
        return (FormulaMedica) listaGeneral.buscarDato(indice);
    }

    /** Posición de la fórmula en la lista general (0 = la primera registrada). */
    public int posicionEnLista(FormulaMedica formula) throws Exception {
        return listaGeneral.buscarDato(formula);
    }

    /** Explica en qué estructura está la fórmula ahora mismo. */
    public String ubicacion(FormulaMedica formula) {
        if (colaPendientes.contiene(formula)) {
            return "En la cola de pendientes (esperando ser dispensada)";
        }
        int distancia = historialProcesados.buscar(formula);
        if (distancia != -1) {
            return "En el historial de dispensadas, posición " + distancia + " desde la cima"
                    + (distancia == 1 ? " (es la última dispensada)" : "");
        }
        return "Solo en la lista general (fue cancelada)";
    }

    /**
     * 7. Cancelar una fórmula pendiente usando una COLA AUXILIAR.
     * Una cola solo permite sacar por el frente, así que:
     *   1) se desencolan todas las fórmulas;
     *   2) la que se quiere cancelar se aparta; las demás pasan a la cola auxiliar;
     *   3) se devuelven de la auxiliar a la cola original, conservando su orden.
     * La fórmula sigue en la lista general, con estado CANCELADA.
     */
    public FormulaMedica cancelarPendiente(String numero) throws Exception {
        FormulaMedica formula = buscarPorNumero(numero);   // valida que exista
        if (!colaPendientes.contiene(formula)) {
            if (FormulaMedica.DISPENSADA.equals(formula.getEstado())) {
                throw new Exception("La fórmula " + formula.getNumeroFormula()
                        + " ya fue dispensada; para cancelarla primero deshaga el procesamiento");
            }
            throw new Exception("La fórmula " + formula.getNumeroFormula() + " ya estaba cancelada");
        }

        Cola auxiliar = new Cola();
        FormulaMedica cancelada = null;
        while (!colaPendientes.esVacia()) {
            FormulaMedica actual = (FormulaMedica) colaPendientes.desencolar();
            if (actual.equals(formula)) {
                cancelada = actual;              // se aparta: no vuelve a la cola
            } else {
                auxiliar.encolar(actual);
            }
        }
        while (!auxiliar.esVacia()) {
            colaPendientes.encolar(auxiliar.desencolar());   // mismo orden de llegada
        }
        cancelada.setEstado(FormulaMedica.CANCELADA);
        return cancelada;
    }

    /** 9. Reporte de cantidades por estructura y por estado. */
    public String reporte() throws Exception {
        int canceladas = 0;
        int unidadesDispensadas = 0;
        for (int i = 0; i < listaGeneral.cuentaElementos(); i++) {
            FormulaMedica f = (FormulaMedica) listaGeneral.buscarDato(i);
            if (FormulaMedica.CANCELADA.equals(f.getEstado())) {
                canceladas++;
            } else if (FormulaMedica.DISPENSADA.equals(f.getEstado())) {
                unidadesDispensadas += f.getCantidad();
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fórmulas registradas (lista general) : ").append(totalRegistradas()).append('\n');
        sb.append("Pendientes por dispensar (cola)      : ").append(totalPendientes()).append('\n');
        sb.append("Dispensadas (pila de historial)      : ").append(totalDispensadas()).append('\n');
        sb.append("Canceladas                           : ").append(canceladas).append('\n');
        sb.append("Unidades de medicamento dispensadas  : ").append(unidadesDispensadas).append('\n');
        boolean cuadra = totalRegistradas() == totalPendientes() + totalDispensadas() + canceladas;
        sb.append("Control: registradas = pendientes + dispensadas + canceladas -> ")
          .append(cuadra ? "OK" : "NO CUADRA");
        if (!colaPendientes.esVacia()) {
            sb.append('\n').append("Siguiente por dispensar : ")
              .append(((FormulaMedica) colaPendientes.peek()).getNumeroFormula());
        }
        if (!historialProcesados.esVacia()) {
            sb.append('\n').append("Última dispensada       : ")
              .append(((FormulaMedica) historialProcesados.peek()).getNumeroFormula());
        }
        return sb.toString();
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
