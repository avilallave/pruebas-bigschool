import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Calculadora calc = new Calculadora();
        boolean salir = false;

        System.out.println("=== Calculadora ===");

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
                    salir = true;
                    System.out.println("Saliendo... ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("Selecciona una operación:");
        System.out.println("  1) Sumar");
        System.out.println("  2) Restar");
        System.out.println("  3) Multiplicar");
        System.out.println("  4) Dividir");
        System.out.println("  5) Salir");
        System.out.print("Opción: ");
    }

    private static void operar(Scanner sc, Calculadora calc, String opcion) {
        double a = leerNumero(sc, "Introduce el primer número: ");
        double b = leerNumero(sc, "Introduce el segundo número: ");

        try {
            double resultado;
            String simbolo;
            switch (opcion) {
                case "1": resultado = calc.sumar(a, b);       simbolo = "+"; break;
                case "2": resultado = calc.restar(a, b);      simbolo = "-"; break;
                case "3": resultado = calc.multiplicar(a, b); simbolo = "*"; break;
                case "4": resultado = calc.dividir(a, b);     simbolo = "/"; break;
                default:  return;
            }
            System.out.println("Resultado: " + a + " " + simbolo + " " + b + " = " + resultado);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static double leerNumero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido, introduce un número.");
            }
        }
    }
}
