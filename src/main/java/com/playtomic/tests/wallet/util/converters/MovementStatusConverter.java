package com.playtomic.tests.wallet.util.converters;

import com.playtomic.tests.wallet.util.enums.MovementStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

@Converter(autoApply = true)
public class MovementStatusConverter implements AttributeConverter<MovementStatus, String> {

  @Override
  public String convertToDatabaseColumn(MovementStatus movementStatus) {
    if (movementStatus == null) {
      return null;
    }
    return movementStatus.getCode();
  }

  @Override
  public MovementStatus convertToEntityAttribute(String s) {
    if (s == null) {
      return null;
    }
    return Arrays.stream(MovementStatus.values())
        .filter(status -> (status.getCode().equals(s)))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
