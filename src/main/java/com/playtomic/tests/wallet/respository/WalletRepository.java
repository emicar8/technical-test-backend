package com.playtomic.tests.wallet.respository;

import com.playtomic.tests.wallet.respository.entity.WalletEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {}
