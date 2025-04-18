package com.playtomic.tests.wallet.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequest {

  @NotNull
  private Long userId;

  @Size(min = 3, max = 3)
  private String currency;
}
