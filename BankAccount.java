import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

/**
 * Representa una cuenta bancaria con estados y validaciones mínimas
 * propias de un sistema de banca.
 */
public class BankAccount {

    /** Estados posibles de una cuenta bancaria. */
    public enum Status {
        /** Cuenta operativa: admite ingresos y retiros. */
        ACTIVE,
        /** Cuenta bloqueada temporalmente: no admite operaciones. */
        BLOCKED,
        /** Cuenta cerrada de forma definitiva: no admite operaciones. */
        CLOSED
    }

    /** Importe mínimo para abrir la cuenta. */
    public static final BigDecimal MIN_OPENING_BALANCE = BigDecimal.ZERO;

    /** Saldo mínimo permitido (no se permite descubierto). */
    public static final BigDecimal MIN_BALANCE = BigDecimal.ZERO;

    private final String id;
    private final String holder;
    private final Instant createdAt;
    private BigDecimal balance;
    private Status status;

    public BankAccount(String holder, BigDecimal initialBalance) {
        if (holder == null || holder.trim().isEmpty()) {
            throw new IllegalArgumentException("El titular es obligatorio");
        }
        if (initialBalance == null) {
            throw new IllegalArgumentException("El saldo inicial no puede ser nulo");
        }
        if (initialBalance.compareTo(MIN_OPENING_BALANCE) < 0) {
            throw new IllegalArgumentException(
                    "El saldo inicial debe ser >= " + MIN_OPENING_BALANCE);
        }

        this.id = UUID.randomUUID().toString();
        this.holder = holder.trim();
        this.createdAt = Instant.now();
        this.balance = scale(initialBalance);
        this.status = Status.ACTIVE;
    }

    // ---------------- Operaciones ----------------

    /** Ingresa una cantidad positiva en la cuenta. */
    public void deposit(BigDecimal amount) {
        ensureActive();
        validatePositiveAmount(amount);
        this.balance = scale(this.balance.add(amount));
    }

    /** Retira una cantidad positiva si hay saldo suficiente. */
    public void withdraw(BigDecimal amount) {
        ensureActive();
        validatePositiveAmount(amount);
        BigDecimal newBalance = this.balance.subtract(amount);
        if (newBalance.compareTo(MIN_BALANCE) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        this.balance = scale(newBalance);
    }

    /** Transfiere una cantidad a otra cuenta. */
    public void transferTo(BankAccount target, BigDecimal amount) {
        if (target == null) {
            throw new IllegalArgumentException("La cuenta destino es obligatoria");
        }
        if (target == this) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta");
        }
        // El orden importa: si el destino no admite el ingreso, no debemos restar.
        ensureActive();
        validatePositiveAmount(amount);
        if (this.balance.subtract(amount).compareTo(MIN_BALANCE) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        target.deposit(amount); // valida que el destino esté activo
        this.balance = scale(this.balance.subtract(amount));
    }

    // ---------------- Cambios de estado ----------------

    public void block() {
        if (this.status == Status.CLOSED) {
            throw new IllegalStateException("Una cuenta cerrada no puede bloquearse");
        }
        this.status = Status.BLOCKED;
    }

    public void unblock() {
        if (this.status != Status.BLOCKED) {
            throw new IllegalStateException("Solo se pueden desbloquear cuentas bloqueadas");
        }
        this.status = Status.ACTIVE;
    }

    public void close() {
        if (this.status == Status.CLOSED) {
            return;
        }
        if (this.balance.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException(
                    "Para cerrar la cuenta el saldo debe ser 0");
        }
        this.status = Status.CLOSED;
    }

    // ---------------- Validaciones ----------------

    private void ensureActive() {
        if (this.status != Status.ACTIVE) {
            throw new IllegalStateException(
                    "La cuenta no está activa (estado=" + this.status + ")");
        }
    }

    private static void validatePositiveAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("El importe no puede ser nulo");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser positivo");
        }
    }

    private static BigDecimal scale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }

    // ---------------- Getters ----------------

    public String getId() {
        return id;
    }

    public String getHolder() {
        return holder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "BankAccount{id=" + id
                + ", holder='" + holder + '\''
                + ", balance=" + balance
                + ", status=" + status + '}';
    }
}
