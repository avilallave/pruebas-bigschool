import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Calculadora calc = new Calculadora();
        boolean salir = false;

        mostrarCabecera();

        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                case "2":
                case "3":
                case "4":
                    operar(sc, calc, opcion);
                    break;
                case "5":
                    operarFactorial(sc, calc);
                    break;
                case "6":
                    salir = true;
                    mostrarDespedida();
                    break;
                default:
                    mostrarError("Opción no válida. Inténtalo de nuevo.");
            }
        }

        sc.close();
    }

    private static void mostrarCabecera() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║                                          ║");
        System.out.println("  ║            C A L C U L A D O R A         ║");
        System.out.println("  ║                                          ║");
        System.out.println("  ║              ┌───┬───┬───┬───┐           ║");
        System.out.println("  ║              │ 7 │ 8 │ 9 │ ÷ │           ║");
        System.out.println("  ║              ├───┼───┼───┼───┤           ║");
        System.out.println("  ║              │ 4 │ 5 │ 6 │ × │           ║");
        System.out.println("  ║              ├───┼───┼───┼───┤           ║");
        System.out.println("  ║              │ 1 │ 2 │ 3 │ - │           ║");
        System.out.println("  ║              ├───┼───┼───┼───┤           ║");
        System.out.println("  ║              │ 0 │ . │ = │ + │           ║");
        System.out.println("  ║              └───┴───┴───┴───┘           ║");
        System.out.println("  ║                                          ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("  ┌──────────────────────────────────────────┐");
        System.out.println("  │           SELECCIONA UNA OPCIÓN          │");
        System.out.println("  ├──────────────────────────────────────────┤");
        System.out.println("  │   [1]  ➕  Sumar                          │");
        System.out.println("  │   [2]  ➖  Restar                         │");
        System.out.println("  │   [3]  ✖️  Multiplicar                    │");
        System.out.println("  │   [4]  ➗  Dividir                        │");
        System.out.println("  │   [5]  ❗  Factorial (n!)                 │");
        System.out.println("  │   [6]  🚪  Salir                          │");
        System.out.println("  └──────────────────────────────────────────┘");
        System.out.print("   ▶ Opción: ");
    }

    private static void mostrarDespedida() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║       ¡Hasta luego! Saliendo...  👋     ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println();
    }

    private static void mostrarResultado(double a, String simbolo, double b, double resultado) {
        String linea = "  " + a + " " + simbolo + " " + b + " = " + resultado;
        int ancho = Math.max(linea.length() + 4, 44);
        String borde = repetir("─", ancho - 2);
        System.out.println();
        System.out.println("  ┌" + borde + "┐");
        System.out.println("  │" + centrar("RESULTADO", ancho - 2) + "│");
        System.out.println("  ├" + borde + "┤");
        System.out.println("  │" + centrar(linea.trim(), ancho - 2) + "│");
        System.out.println("  └" + borde + "┘");
    }

    private static void mostrarError(String mensaje) {
        System.out.println();
        System.out.println("  ⚠  " + mensaje);
    }

    private static void operar(Scanner sc, Calculadora calc, String opcion) {
        double a = leerNumero(sc, "   ▶ Introduce el primer número: ");
        double b = leerNumero(sc, "   ▶ Introduce el segundo número: ");

        try {
            double resultado;
            String simbolo;
            switch (opcion) {
                case "1": resultado = calc.sumar(a, b);       simbolo = "+"; break;
                case "2": resultado = calc.restar(a, b);      simbolo = "-"; break;
                case "3": resultado = calc.multiplicar(a, b); simbolo = "×"; break;
                case "4": resultado = calc.dividir(a, b);     simbolo = "÷"; break;
                default:  return;
            }
            mostrarResultado(a, simbolo, b, resultado);
        } catch (ArithmeticException e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    private static void operarFactorial(Scanner sc, Calculadora calc) {
        int n = leerEntero(sc, "   ▶ Introduce un número entero no negativo: ");
        try {
            long resultado = calc.factorial(n);
            mostrarResultadoFactorial(n, resultado);
        } catch (ArithmeticException | IllegalArgumentException e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    private static void mostrarResultadoFactorial(int n, long resultado) {
        String linea = "  " + n + "! = " + resultado;
        int ancho = Math.max(linea.length() + 4, 44);
        String borde = repetir("─", ancho - 2);
        System.out.println();
        System.out.println("  ┌" + borde + "┐");
        System.out.println("  │" + centrar("RESULTADO", ancho - 2) + "│");
        System.out.println("  ├" + borde + "┤");
        System.out.println("  │" + centrar(linea.trim(), ancho - 2) + "│");
        System.out.println("  └" + borde + "┘");
    }

    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                mostrarError("Valor no válido, introduce un número entero.");
            }
        }
    }

    private static double leerNumero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                mostrarError("Valor no válido, introduce un número.");
            }
        }
    }

    private static String repetir(String s, int veces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < veces; i++) sb.append(s);
        return sb.toString();
    }

    private static String centrar(String texto, int ancho) {
        if (texto.length() >= ancho) return texto.substring(0, ancho);
        int total = ancho - texto.length();
        int izq = total / 2;
        int der = total - izq;
        return repetir(" ", izq) + texto + repetir(" ", der);
    }
}
