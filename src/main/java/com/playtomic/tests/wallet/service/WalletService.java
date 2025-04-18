package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.respository.entity.WalletEntity;
import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {

  WalletEntity createWallet(Long userId, String currency);

  WalletEntity getWallet(UUID id);

  WalletEntity fundWallet(UUID id, BigDecimal amount, String card);
}
