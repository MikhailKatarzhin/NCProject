package ncp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Table
@Entity
@Getter
@RequiredArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long balance = 0L;

    public boolean debitingFunds(Long requiredFunds) {
        if (requiredFunds < 0) throw new IllegalArgumentException("Required funds must be zero or positive");
        if (balance < requiredFunds) return false;
        balance -= requiredFunds;
        return true;
    }

    public Long replenishmentFunds(Long incomingFunds) {
        if (incomingFunds < 1) throw new IllegalArgumentException("Incoming funds must be positive");
        balance += incomingFunds;
        return balance;
    }
}
