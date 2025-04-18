package com.playtomic.tests.wallet.respository;

import com.playtomic.tests.wallet.respository.entity.MovementEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<MovementEntity, UUID> {}
