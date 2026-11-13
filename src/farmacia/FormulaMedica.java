package farmacia;

/**
 * Fórmula médica que llega a la farmacia para ser dispensada.
 * El identificador principal es el número de fórmula: dos fórmulas son iguales
 * (equals) si tienen el mismo número, sin importar los demás datos.
 */
public class FormulaMedica {

    public static final String PENDIENTE = "PENDIENTE";
    public static final String DISPENSADA = "DISPENSADA";
    public static final String CANCELADA = "CANCELADA";

    private String numeroFormula;
    private String documentoPaciente;
    private String nombrePaciente;
    private String medicamento;
    private int cantidad;
    private String estado;

    public FormulaMedica(String numeroFormula, String documentoPaciente, String nombrePaciente,
                         String medicamento, int cantidad) throws Exception {
        setNumeroFormula(numeroFormula);
        setDocumentoPaciente(documentoPaciente);
        setNombrePaciente(nombrePaciente);
        setMedicamento(medicamento);
        setCantidad(cantidad);
        this.estado = PENDIENTE;   // toda fórmula nueva empieza pendiente
    }

    /** Constructor privado: crea una fórmula que solo tiene número. */
    private FormulaMedica(String numeroFormula) {
        this.numeroFormula = normalizarNumero(numeroFormula);
    }

    /**
     * Objeto "sonda" para buscar: como equals compara solo por número, basta con
     * una fórmula que tenga el número buscado para usar contiene() o buscarDato().
     */
    public static FormulaMedica conNumero(String numeroFormula) {
        return new FormulaMedica(numeroFormula);
    }

    /** " f-001 " -> "F-001": así el usuario puede escribir el número en minúsculas. */
    public static String normalizarNumero(String numero) {
        return numero == null ? "" : numero.trim().toUpperCase();
    }

    /* ---------------- getters ---------------- */

    public String getNumeroFormula() {
        return numeroFormula;
    }

    public String getDocumentoPaciente() {
        return documentoPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getEstado() {
        return estado;
    }

    /* ---------------- setters (con validación) ---------------- */

    public void setNumeroFormula(String numeroFormula) throws Exception {
        String numero = normalizarNumero(numeroFormula);
        if (numero.isEmpty()) {
            throw new Exception("El número de fórmula es obligatorio");
        }
        this.numeroFormula = numero;
    }

    public void setDocumentoPaciente(String documentoPaciente) throws Exception {
        if (documentoPaciente == null || !documentoPaciente.trim().matches("\\d{5,12}")) {
            throw new Exception("El documento del paciente debe tener entre 5 y 12 dígitos");
        }
        this.documentoPaciente = documentoPaciente.trim();
    }

    public void setNombrePaciente(String nombrePaciente) throws Exception {
        this.nombrePaciente = textoObligatorio(nombrePaciente, "El nombre del paciente");
    }

    public void setMedicamento(String medicamento) throws Exception {
        this.medicamento = textoObligatorio(medicamento, "El medicamento");
    }

    public void setCantidad(int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor que cero");
        }
        this.cantidad = cantidad;
    }

    public void setEstado(String estado) throws Exception {
        if (!PENDIENTE.equals(estado) && !DISPENSADA.equals(estado) && !CANCELADA.equals(estado)) {
            throw new Exception("Estado no válido: " + estado);
        }
        this.estado = estado;
    }

    private static String textoObligatorio(String texto, String campo) throws Exception {
        if (texto == null || texto.trim().isEmpty()) {
            throw new Exception(campo + " es obligatorio");
        }
        return texto.trim();
    }

    /* ---------------- equals, hashCode y toString ---------------- */

    /** Dos fórmulas son la misma si tienen el mismo número de fórmula. */
    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof FormulaMedica)) {
            return false;
        }
        FormulaMedica f = (FormulaMedica) otro;
        return numeroFormula.equals(f.numeroFormula);
    }

    @Override
    public int hashCode() {
        return numeroFormula.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Fórmula %-6s | %-10s | %-17s | %-24s | x%-3d | %s",
                numeroFormula, documentoPaciente, nombrePaciente, medicamento, cantidad, estado);
    }
}
