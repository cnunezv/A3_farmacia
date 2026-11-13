package farmacia;

/**
 * Casos de prueba de las estructuras y del sistema, sin librerías externas.
 * Cada prueba imprime [OK] o [FALLA]; al final se muestra el resumen.
 *
 * Ejecutar:  java -cp out farmacia.PruebasFarmacia
 */
public class PruebasFarmacia {

    private static int aprobadas;
    private static int fallidas;

    public static void main(String[] args) {
        aprobadas = 0;
        fallidas = 0;
        try {
            probarLista();
            probarCola();
            probarPila();
            probarSistema();
        } catch (Exception e) {
            fallidas++;
            System.out.println("[FALLA] Excepción inesperada: " + e.getMessage());
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Resultado: " + aprobadas + " pruebas OK, " + fallidas + " fallidas.");
        System.exit(fallidas == 0 ? 0 : 1);
    }

    private static void probarLista() throws Exception {
        System.out.println("=== LISTA ===");
        Lista l = new Lista();
        l.agregar("B");
        l.agregarAlInicio("A");
        l.agregar("D");
        l.agregarEnPosicion(2, "C");
        verificar("agregar / agregarAlInicio / agregarEnPosicion -> A, B, C, D",
                l.cuentaElementos() == 4 && l.buscarDato(0).equals("A") && l.buscarDato(2).equals("C")
                        && l.buscarDato(3).equals("D"));
        verificar("mostrarAtras recorre con las referencias izquierda (D primero)",
                l.mostrarAtras().startsWith("3. D"));
        verificar("buscarDato(Object) y contiene", l.buscarDato("C") == 2 && l.contiene("A") && !l.contiene("Z"));
        verificar("eliminarEnPosicion(1) quita B y reconecta A <-> C",
                l.eliminarEnPosicion(1).equals("B") && l.mostrarAdelante().equals("0. A\n1. C\n2. D\n")
                        && l.mostrarAtras().equals("2. D\n1. C\n0. A\n"));
        verificar("eliminarPrimero y eliminarUltimo",
                l.eliminarPrimero().equals("A") && l.eliminarUltimo().equals("D") && l.cuentaElementos() == 1);
        l.limpiar();
        verificar("limpiar deja la lista vacía", l.esVacia() && l.cuentaElementos() == 0);
        verificar("buscarDato en lista vacía lanza excepción", lanza(() -> l.buscarDato(0)));
        verificar("agregar null lanza excepción", lanza(() -> l.agregar(null)));
        l.agregar("X");
        verificar("índice fuera de rango lanza excepción", lanza(() -> l.buscarDato(5)));
    }

    private static void probarCola() throws Exception {
        System.out.println("=== COLA ===");
        Cola c = new Cola();
        c.encolar("1");
        c.encolar("2");
        c.encolar("3");
        verificar("FIFO: peek es el primero en llegar", c.peek().equals("1") && c.tamanio() == 3);
        verificar("FIFO: desencolar respeta el orden de llegada",
                c.desencolar().equals("1") && c.desencolar().equals("2") && c.tamanio() == 1);
        verificar("contiene", c.contiene("3") && !c.contiene("1"));
        c.desencolar();
        verificar("cola vacía tras desencolar todo", c.esVacia());
        verificar("desencolar cola vacía lanza excepción", lanza(c::desencolar));
        c.encolar("4");
        c.limpiar();
        verificar("limpiar", c.esVacia() && c.mostrar().equals("(cola vacía)"));
    }

    private static void probarPila() throws Exception {
        System.out.println("=== PILA ===");
        Pila p = new Pila();
        p.apilar("A");
        p.apilar("B");
        p.apilar("C");
        verificar("LIFO: peek es el último apilado", p.peek().equals("C") && p.tamanio() == 3);
        verificar("buscar: C en la cima (1), A en el fondo (3), Z no está (-1)",
                p.buscar("C") == 1 && p.buscar("A") == 3 && p.buscar("Z") == -1);
        verificar("LIFO: desapilar sale en orden inverso",
                p.desapilar().equals("C") && p.desapilar().equals("B") && p.peek().equals("A"));
        p.desapilar();
        verificar("desapilar pila vacía lanza excepción", lanza(p::desapilar));
        p.apilar("D");
        p.limpiar();
        verificar("limpiar", p.esVacia() && !p.contiene("D"));
    }

    private static void probarSistema() throws Exception {
        System.out.println("=== SISTEMA FARMACIA ===");
        SistemaFarmacia s = new SistemaFarmacia();
        s.cargarDatosEjemplo();   // F-101 .. F-105
        verificar("Registro: 5 en la lista y 5 en la cola", s.totalRegistradas() == 5 && s.totalPendientes() == 5);
        verificar("Registro duplicado (f-101 en minúsculas) lanza excepción",
                lanza(() -> s.registrar("f-101", "123456", "X", "Y", 1)));
        verificar("Registro con cantidad 0 lanza excepción",
                lanza(() -> s.registrar("F-200", "123456", "X", "Y", 0)));
        verificar("Registro con documento inválido lanza excepción",
                lanza(() -> s.registrar("F-200", "12ab", "X", "Y", 1)));

        FormulaMedica p1 = s.procesarSiguiente();
        FormulaMedica p2 = s.procesarSiguiente();
        verificar("Procesar respeta el orden de llegada (F-101 y luego F-102)",
                p1.getNumeroFormula().equals("F-101") && p2.getNumeroFormula().equals("F-102"));
        verificar("Procesada queda DISPENSADA y en la pila", p2.getEstado().equals(FormulaMedica.DISPENSADA)
                && s.totalDispensadas() == 2 && s.totalPendientes() == 3);

        FormulaMedica deshecha = s.deshacerUltimoProcesamiento();
        verificar("Deshacer devuelve la última dispensada (F-102) a la cola como PENDIENTE",
                deshecha.getNumeroFormula().equals("F-102") && deshecha.getEstado().equals(FormulaMedica.PENDIENTE)
                        && s.totalDispensadas() == 1 && s.totalPendientes() == 4);
        verificar("Tras deshacer, F-102 queda al final de la cola",
                s.mostrarPendientes().trim().endsWith("PENDIENTE") && s.mostrarPendientes().contains("4. Fórmula F-102"));

        FormulaMedica cancelada = s.cancelarPendiente("F-104");
        verificar("Cancelar con cola auxiliar: F-104 sale y el resto conserva su orden",
                cancelada.getEstado().equals(FormulaMedica.CANCELADA) && s.totalPendientes() == 3
                        && s.mostrarPendientes().contains("1. Fórmula F-103")
                        && s.mostrarPendientes().contains("2. Fórmula F-105")
                        && s.mostrarPendientes().contains("3. Fórmula F-102"));
        verificar("La cancelada sigue en la lista general", s.totalRegistradas() == 5
                && s.buscarPorNumero("F-104").getEstado().equals(FormulaMedica.CANCELADA));
        verificar("Cancelar una dispensada lanza excepción", lanza(() -> s.cancelarPendiente("F-101")));
        verificar("Cancelar dos veces lanza excepción", lanza(() -> s.cancelarPendiente("F-104")));
        verificar("Cancelar un número inexistente lanza excepción", lanza(() -> s.cancelarPendiente("F-999")));

        verificar("Buscar por número ignora mayúsculas y espacios (' f-103 ')",
                s.buscarPorNumero(" f-103 ").getNombrePaciente().equals("Camila Herrera"));
        verificar("Buscar inexistente lanza excepción", lanza(() -> s.buscarPorNumero("F-999")));
        verificar("Reporte cuadra: 5 = 3 pendientes + 1 dispensada + 1 cancelada",
                s.reporte().contains("-> OK"));

        SistemaFarmacia vacio = new SistemaFarmacia();
        verificar("Procesar sin pendientes lanza excepción", lanza(vacio::procesarSiguiente));
        verificar("Deshacer sin historial lanza excepción", lanza(vacio::deshacerUltimoProcesamiento));
    }

    /* ---------------- utilidades ---------------- */

    private interface Accion {
        void ejecutar() throws Exception;
    }

    private static boolean lanza(Accion accion) {
        try {
            accion.ejecutar();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            aprobadas++;
            System.out.println("[OK]    " + descripcion);
        } else {
            fallidas++;
            System.out.println("[FALLA] " + descripcion);
        }
    }
}
