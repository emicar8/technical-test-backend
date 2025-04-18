package com.playtomic.tests.wallet.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundWalletRequest {

  @DecimalMin(value = "0.00", inclusive = false)
  @Digits(integer = 36, fraction = 2)
  private BigDecimal amount;

  @NotEmpty private String cardNumber;
}
