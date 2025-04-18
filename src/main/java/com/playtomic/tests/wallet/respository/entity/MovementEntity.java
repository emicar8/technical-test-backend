package com.playtomic.tests.wallet.respository.entity;

import com.playtomic.tests.wallet.util.enums.MovementStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "movement")
public class MovementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private MovementStatus status;

  @ManyToOne
  @JoinColumn(name = "wallet_id")
  private WalletEntity wallet;
}
