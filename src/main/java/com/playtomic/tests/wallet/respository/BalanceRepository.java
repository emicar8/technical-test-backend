package com.playtomic.tests.wallet.respository;

import java.util.UUID;

import com.playtomic.tests.wallet.respository.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<BalanceEntity, UUID> {}
