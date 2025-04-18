package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.respository.entity.WalletEntity;
import java.util.UUID;

public interface WalletService {

  public WalletEntity createWallet(Long userId, String currency);

  public WalletEntity getWallet(UUID id);
}
