package com.playtomic.tests.wallet.respository;

import com.playtomic.tests.wallet.respository.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {}
