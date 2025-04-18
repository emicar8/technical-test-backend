package com.playtomic.tests.wallet.respository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
@Table(name = "balance")
public class BalanceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private BigDecimal amount;

  private BigDecimal availableAmount;

  private Long userId;

  private Instant createdDate;

  private Instant updatedDate;

  @ManyToOne
  @JoinColumn(name = "currency_id", nullable = false)
  private CurrencyEntity currency;

  @Version private Long version;
}
