package com.playtomic.tests.wallet.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MovementType {
  CREDIT("C"),
  DEBIT("D");

  private final String type;
}
