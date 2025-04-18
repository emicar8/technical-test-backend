package com.playtomic.tests.wallet.api.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletResponseDto {

  private UUID id;

  private BigDecimal balance;

  private BigDecimal availableBalance;

  private Long userId;

  private Instant createdDate;

  private Instant updatedDate;

  private String currency;
}
