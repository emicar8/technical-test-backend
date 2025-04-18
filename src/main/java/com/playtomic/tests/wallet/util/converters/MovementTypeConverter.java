package com.playtomic.tests.wallet.util.converters;

import com.playtomic.tests.wallet.util.enums.MovementType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

@Converter
public class MovementTypeConverter implements AttributeConverter<MovementType, String> {
  @Override
  public String convertToDatabaseColumn(MovementType movementType) {
    if (movementType == null) {
      return null;
    }
    return movementType.getType();
  }

  @Override
  public MovementType convertToEntityAttribute(String s) {
    if (s == null) {
      return null;
    }
    return Arrays.stream(MovementType.values())
        .filter(movementType -> movementType.getType().equals(s))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
