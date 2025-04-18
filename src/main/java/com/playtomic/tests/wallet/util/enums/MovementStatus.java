package com.playtomic.tests.wallet.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MovementStatus {
  PENDING("P"),
  APPROVED("A"),
  DECLINED("D"),
  REVERSED("R");

  private final String code;
}
