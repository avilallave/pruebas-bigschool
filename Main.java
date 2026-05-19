import java.util.Scanner;

public class Main {

    private enum Idioma {
        ES, EN, FR
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Calculadora calc = new Calculadora();
        boolean salir = false;

        Idioma idioma = seleccionarIdioma(sc);

        mostrarCabecera(idioma);

        while (!salir) {
            mostrarMenu(idioma);
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                case "2":
                case "3":
                case "4":
                    operar(sc, calc, opcion, idioma);
                    break;
                case "5":
                    operarFactorial(sc, calc, idioma);
                    break;
                case "6":
                    salir = true;
                    mostrarDespedida(idioma);
                    break;
                default:
                    mostrarError(texto(idioma, "opcion_invalida"));
            }
        }

        sc.close();
    }

    private static Idioma seleccionarIdioma(Scanner sc) {
        while (true) {
            System.out.println();
            System.out.println("Choose language / Choisissez la langue / Elige idioma:");
            System.out.println("  [1] Español");
            System.out.println("  [2] English");
            System.out.println("  [3] Français");
            System.out.print("  > ");

            String opcion = sc.nextLine().trim();
            switch (opcion) {
                case "1": return Idioma.ES;
                case "2": return Idioma.EN;
                case "3": return Idioma.FR;
                default:
                    System.out.println("Invalid option / Option invalide / Opción inválida");
            }
        }
    }

    private static void mostrarCabecera(Idioma idioma) {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║                                          ║");
        System.out.println("  ║            " + centrar(texto(idioma, "titulo"), 28) + "║");
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

    private static void mostrarMenu(Idioma idioma) {
        System.out.println();
        System.out.println("  ┌──────────────────────────────────────────┐");
        System.out.println("  │" + centrar(texto(idioma, "menu_titulo"), 42) + "│");
        System.out.println("  ├──────────────────────────────────────────┤");
        System.out.println("  │   [1]  ➕  " + ajustar(texto(idioma, "sumar"), 30) + "│");
        System.out.println("  │   [2]  ➖  " + ajustar(texto(idioma, "restar"), 30) + "│");
        System.out.println("  │   [3]  ✖️  " + ajustar(texto(idioma, "multiplicar"), 30) + "│");
        System.out.println("  │   [4]  ➗  " + ajustar(texto(idioma, "dividir"), 30) + "│");
        System.out.println("  │   [5]  ❗  " + ajustar(texto(idioma, "factorial"), 30) + "│");
        System.out.println("  │   [6]  🚪  " + ajustar(texto(idioma, "salir"), 30) + "│");
        System.out.println("  └──────────────────────────────────────────┘");
        System.out.print("   ▶ " + texto(idioma, "prompt_opcion"));
    }

    private static void mostrarDespedida(Idioma idioma) {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║" + centrar(texto(idioma, "despedida"), 42) + "║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println();
    }

    private static void mostrarResultado(double a, String simbolo, double b, double resultado, Idioma idioma) {
        String linea = "  " + a + " " + simbolo + " " + b + " = " + resultado;
        int ancho = Math.max(linea.length() + 4, 44);
        String borde = repetir("─", ancho - 2);
        System.out.println();
        System.out.println("  ┌" + borde + "┐");
        System.out.println("  │" + centrar(texto(idioma, "resultado"), ancho - 2) + "│");
        System.out.println("  ├" + borde + "┤");
        System.out.println("  │" + centrar(linea.trim(), ancho - 2) + "│");
        System.out.println("  └" + borde + "┘");
    }

    private static void mostrarError(String mensaje) {
        System.out.println();
        System.out.println("  ⚠  " + mensaje);
    }

    private static void operar(Scanner sc, Calculadora calc, String opcion, Idioma idioma) {
        double a = leerNumero(sc, texto(idioma, "primer_numero"), idioma);
        double b = leerNumero(sc, texto(idioma, "segundo_numero"), idioma);

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
            mostrarResultado(a, simbolo, b, resultado, idioma);
        } catch (ArithmeticException e) {
            mostrarError(texto(idioma, "error") + e.getMessage());
        }
    }

    private static void operarFactorial(Scanner sc, Calculadora calc, Idioma idioma) {
        int n = leerEntero(sc, texto(idioma, "numero_entero"), idioma);
        try {
            long resultado = calc.factorial(n);
            mostrarResultadoFactorial(n, resultado, idioma);
        } catch (ArithmeticException | IllegalArgumentException e) {
            mostrarError(texto(idioma, "error") + e.getMessage());
        }
    }

    private static void mostrarResultadoFactorial(int n, long resultado, Idioma idioma) {
        String linea = "  " + n + "! = " + resultado;
        int ancho = Math.max(linea.length() + 4, 44);
        String borde = repetir("─", ancho - 2);
        System.out.println();
        System.out.println("  ┌" + borde + "┐");
        System.out.println("  │" + centrar(texto(idioma, "resultado"), ancho - 2) + "│");
        System.out.println("  ├" + borde + "┤");
        System.out.println("  │" + centrar(linea.trim(), ancho - 2) + "│");
        System.out.println("  └" + borde + "┘");
    }

    private static int leerEntero(Scanner sc, String mensaje, Idioma idioma) {
        while (true) {
            System.out.print("   ▶ " + mensaje);
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                mostrarError(texto(idioma, "entero_invalido"));
            }
        }
    }

    private static double leerNumero(Scanner sc, String mensaje, Idioma idioma) {
        while (true) {
            System.out.print("   ▶ " + mensaje);
            String linea = sc.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                mostrarError(texto(idioma, "numero_invalido"));
            }
        }
    }

    private static String texto(Idioma idioma, String clave) {
        switch (idioma) {
            case EN:
                switch (clave) {
                    case "titulo": return "C A L C U L A T O R";
                    case "menu_titulo": return "SELECT AN OPTION";
                    case "sumar": return "Add";
                    case "restar": return "Subtract";
                    case "multiplicar": return "Multiply";
                    case "dividir": return "Divide";
                    case "factorial": return "Factorial (n!)";
                    case "salir": return "Exit";
                    case "prompt_opcion": return "Option: ";
                    case "despedida": return "See you later! Exiting... 👋";
                    case "resultado": return "RESULT";
                    case "opcion_invalida": return "Invalid option. Try again.";
                    case "primer_numero": return "Enter the first number: ";
                    case "segundo_numero": return "Enter the second number: ";
                    case "numero_entero": return "Enter a non-negative integer: ";
                    case "numero_invalido": return "Invalid value, enter a number.";
                    case "entero_invalido": return "Invalid value, enter an integer.";
                    case "error": return "Error: ";
                    default: return clave;
                }
            case FR:
                switch (clave) {
                    case "titulo": return "C A L C U L A T R I C E";
                    case "menu_titulo": return "SÉLECTIONNEZ UNE OPTION";
                    case "sumar": return "Additionner";
                    case "restar": return "Soustraire";
                    case "multiplicar": return "Multiplier";
                    case "dividir": return "Diviser";
                    case "factorial": return "Factorielle (n!)";
                    case "salir": return "Quitter";
                    case "prompt_opcion": return "Option : ";
                    case "despedida": return "À bientôt ! Fermeture... 👋";
                    case "resultado": return "RÉSULTAT";
                    case "opcion_invalida": return "Option invalide. Réessayez.";
                    case "primer_numero": return "Entrez le premier nombre : ";
                    case "segundo_numero": return "Entrez le deuxième nombre : ";
                    case "numero_entero": return "Entrez un entier non négatif : ";
                    case "numero_invalido": return "Valeur invalide, entrez un nombre.";
                    case "entero_invalido": return "Valeur invalide, entrez un entier.";
                    case "error": return "Erreur : ";
                    default: return clave;
                }
            case ES:
            default:
                switch (clave) {
                    case "titulo": return "C A L C U L A D O R A";
                    case "menu_titulo": return "SELECCIONA UNA OPCIÓN";
                    case "sumar": return "Sumar";
                    case "restar": return "Restar";
                    case "multiplicar": return "Multiplicar";
                    case "dividir": return "Dividir";
                    case "factorial": return "Factorial (n!)";
                    case "salir": return "Salir";
                    case "prompt_opcion": return "Opción: ";
                    case "despedida": return "¡Hasta luego! Saliendo... 👋";
                    case "resultado": return "RESULTADO";
                    case "opcion_invalida": return "Opción no válida. Inténtalo de nuevo.";
                    case "primer_numero": return "Introduce el primer número: ";
                    case "segundo_numero": return "Introduce el segundo número: ";
                    case "numero_entero": return "Introduce un número entero no negativo: ";
                    case "numero_invalido": return "Valor no válido, introduce un número.";
                    case "entero_invalido": return "Valor no válido, introduce un número entero.";
                    case "error": return "Error: ";
                    default: return clave;
                }
        }
    }

    private static String ajustar(String texto, int ancho) {
        if (texto.length() >= ancho) return texto.substring(0, ancho);
        return texto + repetir(" ", ancho - texto.length());
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
