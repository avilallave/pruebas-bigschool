import org.junit.Test;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BankAccountTest {

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final BigDecimal TEN = new BigDecimal("10.00");
    private static final BigDecimal HUNDRED = new BigDecimal("100.00");

    // ---------------- Construcción ----------------

    @Test
    public void crear_dadoTitularYSaldoValidos_creaCuentaActiva() {
        // Given
        String holder = "Andrés";
        BigDecimal saldoInicial = HUNDRED;

        // When
        BankAccount cuenta = new BankAccount(holder, saldoInicial);

        // Then
        assertEquals("Andrés", cuenta.getHolder());
        assertEquals(0, cuenta.getBalance().compareTo(HUNDRED));
        assertEquals(BankAccount.Status.ACTIVE, cuenta.getStatus());
        assertNotNull(cuenta.getId());
        assertNotNull(cuenta.getCreatedAt());
    }

    @Test
    public void crear_dadoTitularNulo_lanzaIllegalArgumentException() {
        // Given
        String holder = null;

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount(holder, ZERO));
    }

    @Test
    public void crear_dadoTitularEnBlanco_lanzaIllegalArgumentException() {
        // Given
        String holder = "   ";

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount(holder, ZERO));
    }

    @Test
    public void crear_dadoSaldoInicialNulo_lanzaIllegalArgumentException() {
        // Given
        BigDecimal saldoInicial = null;

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("Ana", saldoInicial));
    }

    @Test
    public void crear_dadoSaldoInicialNegativo_lanzaIllegalArgumentException() {
        // Given
        BigDecimal saldoInicial = new BigDecimal("-0.01");

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("Ana", saldoInicial));
    }

    @Test
    public void crear_dosCuentas_tienenIdsDistintos() {
        // Given
        BankAccount a = new BankAccount("Ana", ZERO);
        BankAccount b = new BankAccount("Bea", ZERO);

        // When / Then
        assertNotEquals(a.getId(), b.getId());
    }

    // ---------------- Depósitos ----------------

    @Test
    public void depositar_dadoImportePositivo_aumentaElSaldo() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When
        cuenta.deposit(new BigDecimal("50"));

        // Then
        assertEquals(0, cuenta.getBalance().compareTo(new BigDecimal("150.00")));
    }

    @Test
    public void depositar_dadoImporteNulo_lanzaIllegalArgumentException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> cuenta.deposit(null));
    }

    @Test
    public void depositar_dadoImporteCero_lanzaIllegalArgumentException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> cuenta.deposit(ZERO));
    }

    @Test
    public void depositar_dadoImporteNegativo_lanzaIllegalArgumentException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> cuenta.deposit(new BigDecimal("-1")));
    }

    @Test
    public void depositar_cuandoCuentaBloqueada_lanzaIllegalStateException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);
        cuenta.block();

        // When / Then
        assertThrows(IllegalStateException.class,
                () -> cuenta.deposit(TEN));
    }

    @Test
    public void depositar_cuandoCuentaCerrada_lanzaIllegalStateException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", ZERO);
        cuenta.close();

        // When / Then
        assertThrows(IllegalStateException.class,
                () -> cuenta.deposit(TEN));
    }

    // ---------------- Retiros ----------------

    @Test
    public void retirar_dadoImporteValido_disminuyeElSaldo() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When
        cuenta.withdraw(new BigDecimal("30"));

        // Then
        assertEquals(0, cuenta.getBalance().compareTo(new BigDecimal("70.00")));
    }

    @Test
    public void retirar_importeIgualAlSaldo_dejaSaldoCero() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When
        cuenta.withdraw(HUNDRED);

        // Then
        assertEquals(0, cuenta.getBalance().compareTo(ZERO));
    }

    @Test
    public void retirar_dadoImporteSuperiorAlSaldo_lanzaIllegalStateException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", TEN);

        // When / Then
        assertThrows(IllegalStateException.class,
                () -> cuenta.withdraw(new BigDecimal("10.01")));
    }

    @Test
    public void retirar_dadoImporteNoPositivo_lanzaIllegalArgumentException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> cuenta.withdraw(ZERO));
    }

    @Test
    public void retirar_cuandoCuentaBloqueada_lanzaIllegalStateException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);
        cuenta.block();

        // When / Then
        assertThrows(IllegalStateException.class,
                () -> cuenta.withdraw(TEN));
    }

    // ---------------- Transferencias ----------------

    @Test
    public void transferir_entreCuentasActivas_actualizaAmbosSaldos() {
        // Given
        BankAccount origen = new BankAccount("Ana", HUNDRED);
        BankAccount destino = new BankAccount("Bea", TEN);

        // When
        origen.transferTo(destino, new BigDecimal("40"));

        // Then
        assertEquals(0, origen.getBalance().compareTo(new BigDecimal("60.00")));
        assertEquals(0, destino.getBalance().compareTo(new BigDecimal("50.00")));
    }

    @Test
    public void transferir_dadoDestinoNulo_lanzaIllegalArgumentException() {
        // Given
        BankAccount origen = new BankAccount("Ana", HUNDRED);

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> origen.transferTo(null, TEN));
    }

    @Test
    public void transferir_aLaMismaCuenta_lanzaIllegalArgumentException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> cuenta.transferTo(cuenta, TEN));
    }

    @Test
    public void transferir_sinSaldoSuficiente_noModificaSaldos() {
        // Given
        BankAccount origen = new BankAccount("Ana", TEN);
        BankAccount destino = new BankAccount("Bea", TEN);

        // When
        assertThrows(IllegalStateException.class,
                () -> origen.transferTo(destino, new BigDecimal("20")));

        // Then (estado intacto: atomicidad)
        assertEquals(0, origen.getBalance().compareTo(TEN));
        assertEquals(0, destino.getBalance().compareTo(TEN));
    }

    @Test
    public void transferir_aDestinoBloqueado_noModificaSaldos() {
        // Given
        BankAccount origen = new BankAccount("Ana", HUNDRED);
        BankAccount destino = new BankAccount("Bea", TEN);
        destino.block();

        // When
        assertThrows(IllegalStateException.class,
                () -> origen.transferTo(destino, new BigDecimal("20")));

        // Then (no se debita el origen si el destino no admite ingreso)
        assertEquals(0, origen.getBalance().compareTo(HUNDRED));
        assertEquals(0, destino.getBalance().compareTo(TEN));
    }

    @Test
    public void transferir_desdeCuentaBloqueada_lanzaIllegalStateException() {
        // Given
        BankAccount origen = new BankAccount("Ana", HUNDRED);
        BankAccount destino = new BankAccount("Bea", TEN);
        origen.block();

        // When / Then
        assertThrows(IllegalStateException.class,
                () -> origen.transferTo(destino, TEN));
    }

    // ---------------- Cambios de estado ----------------

    @Test
    public void bloquear_cuentaActiva_pasaABlocked() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When
        cuenta.block();

        // Then
        assertEquals(BankAccount.Status.BLOCKED, cuenta.getStatus());
    }

    @Test
    public void bloquear_cuentaCerrada_lanzaIllegalStateException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", ZERO);
        cuenta.close();

        // When / Then
        assertThrows(IllegalStateException.class, cuenta::block);
    }

    @Test
    public void desbloquear_cuentaBloqueada_pasaAActive() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);
        cuenta.block();

        // When
        cuenta.unblock();

        // Then
        assertEquals(BankAccount.Status.ACTIVE, cuenta.getStatus());
    }

    @Test
    public void desbloquear_cuentaActiva_lanzaIllegalStateException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When / Then
        assertThrows(IllegalStateException.class, cuenta::unblock);
    }

    @Test
    public void cerrar_cuentaConSaldoCero_pasaAClosed() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", ZERO);

        // When
        cuenta.close();

        // Then
        assertEquals(BankAccount.Status.CLOSED, cuenta.getStatus());
    }

    @Test
    public void cerrar_cuentaConSaldo_lanzaIllegalStateException() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", TEN);

        // When / Then
        assertThrows(IllegalStateException.class, cuenta::close);
    }

    @Test
    public void cerrar_cuentaYaCerrada_esIdempotente() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", ZERO);
        cuenta.close();

        // When
        cuenta.close(); // segunda llamada

        // Then
        assertEquals(BankAccount.Status.CLOSED, cuenta.getStatus());
    }

    // ---------------- Otros ----------------

    @Test
    public void getBalance_devuelveSaldoConDosDecimales() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", new BigDecimal("10.005"));

        // When
        BigDecimal saldo = cuenta.getBalance();

        // Then (HALF_EVEN: 10.005 -> 10.00)
        assertEquals(2, saldo.scale());
        assertTrue("Esperado 10.00 ó 10.01, fue " + saldo,
                saldo.compareTo(new BigDecimal("10.00")) == 0
                        || saldo.compareTo(new BigDecimal("10.01")) == 0);
    }

    @Test
    public void toString_contieneInformacionRelevante() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", HUNDRED);

        // When
        String texto = cuenta.toString();

        // Then
        assertTrue(texto.contains("Ana"));
        assertTrue(texto.contains("ACTIVE"));
        assertTrue(texto.contains(cuenta.getId()));
    }

    @Test
    public void getStatus_inicialmente_esActive() {
        // Given
        BankAccount cuenta = new BankAccount("Ana", ZERO);

        // When
        BankAccount.Status estado = cuenta.getStatus();

        // Then
        assertSame(BankAccount.Status.ACTIVE, estado);
    }
}
