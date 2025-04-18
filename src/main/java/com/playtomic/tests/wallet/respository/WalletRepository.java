package com.playtomic.tests.wallet.respository;

import java.util.UUID;

import com.playtomic.tests.wallet.respository.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {}
