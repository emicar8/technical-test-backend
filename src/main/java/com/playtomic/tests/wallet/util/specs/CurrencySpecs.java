package com.playtomic.tests.wallet.util.specs;

import com.playtomic.tests.wallet.respository.entity.CurrencyEntity;
import org.springframework.data.jpa.domain.Specification;

public class CurrencySpecs {

  public static Specification<CurrencyEntity> filterByCode(String code) {
    return (((root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("code").as(String.class), code)));
  }

  public static Specification<CurrencyEntity> filterByNumber(String number) {
    return (((root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("number").as(String.class), number)));
  }
}
