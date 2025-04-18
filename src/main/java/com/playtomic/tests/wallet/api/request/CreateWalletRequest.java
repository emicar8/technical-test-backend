package com.playtomic.tests.wallet.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequest {

  private Long userId;

  private String currency;
}
