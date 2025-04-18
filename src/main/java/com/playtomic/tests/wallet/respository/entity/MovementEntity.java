package com.playtomic.tests.wallet.respository.entity;

import com.playtomic.tests.wallet.util.converters.MovementStatusConverter;
import com.playtomic.tests.wallet.util.converters.MovementTypeConverter;
import com.playtomic.tests.wallet.util.enums.MovementStatus;
import com.playtomic.tests.wallet.util.enums.MovementType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movement")
public class MovementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private BigDecimal amount;

  @Convert(converter = MovementStatusConverter.class)
  private MovementStatus status;

  @Convert(converter = MovementTypeConverter.class)
  private MovementType type;

  private String externalId;

  @ManyToOne
  @JoinColumn(name = "wallet_id")
  private WalletEntity wallet;
}
