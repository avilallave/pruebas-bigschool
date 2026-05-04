import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CalculadoraTest {

    private final Calculadora calc = new Calculadora();
    private static final double DELTA = 1e-9;

    @Test
    public void sumar_dadosDosNumeros_devuelveLaSuma() {
        // Given
        double a = 3;
        double b = 5;

        // When
        double resultado = calc.sumar(a, b);

        // Then
        assertEquals(8.0, resultado, DELTA);
    }

    @Test
    public void restar_dadosDosNumeros_devuelveLaDiferencia() {
        // Given
        double a = 5;
        double b = 3;

        // When
        double resultado = calc.restar(a, b);

        // Then
        assertEquals(2.0, resultado, DELTA);
    }

    @Test
    public void multiplicar_dadosDosNumeros_devuelveElProducto() {
        // Given
        double a = 3;
        double b = 5;

        // When
        double resultado = calc.multiplicar(a, b);

        // Then
        assertEquals(15.0, resultado, DELTA);
    }

    @Test
    public void dividir_dadosDosNumerosNoNulos_devuelveElCociente() {
        // Given
        double a = 10;
        double b = 5;

        // When
        double resultado = calc.dividir(a, b);

        // Then
        assertEquals(2.0, resultado, DELTA);
    }

    @Test
    public void dividir_cuandoDivisorEsCero_lanzaArithmeticException() {
        // Given
        double a = 1;
        double b = 0;

        // When / Then
        assertThrows(ArithmeticException.class, () -> calc.dividir(a, b));
    }

    // ---------- Tests adicionales ----------

    @Test
    public void sumar_dadosDosNumerosNegativos_devuelveLaSumaNegativa() {
        // Given
        double a = -4;
        double b = -6;

        // When
        double resultado = calc.sumar(a, b);

        // Then
        assertEquals(-10.0, resultado, DELTA);
    }

    @Test
    public void sumar_dadoCeroYUnNumero_devuelveElMismoNumero() {
        // Given (elemento neutro)
        double a = 0;
        double b = 7.5;

        // When
        double resultado = calc.sumar(a, b);

        // Then
        assertEquals(7.5, resultado, DELTA);
    }

    @Test
    public void sumar_esConmutativa() {
        // Given
        double a = 12.3;
        double b = -4.56;

        // When
        double resultado1 = calc.sumar(a, b);
        double resultado2 = calc.sumar(b, a);

        // Then
        assertEquals(resultado1, resultado2, DELTA);
    }

    @Test
    public void restar_dadoMinuendoMenorQueSustraendo_devuelveResultadoNegativo() {
        // Given
        double a = 3;
        double b = 10;

        // When
        double resultado = calc.restar(a, b);

        // Then
        assertEquals(-7.0, resultado, DELTA);
    }

    @Test
    public void restar_dadoNumeroMenosCero_devuelveElMismoNumero() {
        // Given
        double a = 9.81;
        double b = 0;

        // When
        double resultado = calc.restar(a, b);

        // Then
        assertEquals(9.81, resultado, DELTA);
    }

    @Test
    public void multiplicar_dadoNumeroPorCero_devuelveCero() {
        // Given
        double a = 123.45;
        double b = 0;

        // When
        double resultado = calc.multiplicar(a, b);

        // Then
        assertEquals(0.0, resultado, DELTA);
    }

    @Test
    public void multiplicar_dadoNumeroPorUno_devuelveElMismoNumero() {
        // Given (elemento neutro)
        double a = -42.5;
        double b = 1;

        // When
        double resultado = calc.multiplicar(a, b);

        // Then
        assertEquals(-42.5, resultado, DELTA);
    }

    @Test
    public void multiplicar_dadosDosNegativos_devuelveResultadoPositivo() {
        // Given (regla de los signos)
        double a = -3;
        double b = -4;

        // When
        double resultado = calc.multiplicar(a, b);

        // Then
        assertEquals(12.0, resultado, DELTA);
    }

    @Test
    public void multiplicar_dadosUnPositivoYUnNegativo_devuelveResultadoNegativo() {
        // Given
        double a = 6;
        double b = -2.5;

        // When
        double resultado = calc.multiplicar(a, b);

        // Then
        assertEquals(-15.0, resultado, DELTA);
    }

    @Test
    public void dividir_dadoDividendoCero_devuelveCero() {
        // Given
        double a = 0;
        double b = 5;

        // When
        double resultado = calc.dividir(a, b);

        // Then
        assertEquals(0.0, resultado, DELTA);
    }

    @Test
    public void dividir_dadoNumeroEntreUno_devuelveElMismoNumero() {
        // Given
        double a = 7.25;
        double b = 1;

        // When
        double resultado = calc.dividir(a, b);

        // Then
        assertEquals(7.25, resultado, DELTA);
    }

    @Test
    public void dividir_dadosDosNegativos_devuelveResultadoPositivo() {
        // Given
        double a = -10;
        double b = -4;

        // When
        double resultado = calc.dividir(a, b);

        // Then
        assertEquals(2.5, resultado, DELTA);
    }

    @Test
    public void dividir_cuandoDivisorEsCero_mensajeContieneInformacion() {
        // Given
        double a = 5;
        double b = 0;

        // When
        ArithmeticException ex = assertThrows(
                ArithmeticException.class,
                () -> calc.dividir(a, b));

        // Then
        assertTrue(
                "El mensaje debería mencionar la división entre 0",
                ex.getMessage() != null && ex.getMessage().contains("0"));
    }
}