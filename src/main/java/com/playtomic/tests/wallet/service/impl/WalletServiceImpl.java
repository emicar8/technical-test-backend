package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exceptions.ResourceNotFoundException;
import com.playtomic.tests.wallet.respository.CurrencyRepository;
import com.playtomic.tests.wallet.respository.WalletRepository;
import com.playtomic.tests.wallet.respository.entity.CurrencyEntity;
import com.playtomic.tests.wallet.respository.entity.WalletEntity;
import com.playtomic.tests.wallet.service.WalletService;
import com.playtomic.tests.wallet.util.specs.CurrencySpecs;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  private final CurrencyRepository currencyRepository;

  @Override
  public WalletEntity createWallet(Long userId, String currency) {
    List<CurrencyEntity> currencyEntityList =
        currencyRepository.findAll(
            CurrencySpecs.filterByCode(currency).or(CurrencySpecs.filterByNumber(currency)));
    if (currencyEntityList.isEmpty()) {
      throw new ResourceNotFoundException("Currency not found");
    }

    WalletEntity wallet =
        WalletEntity.builder()
            .balance(BigDecimal.ZERO)
            .availableBalance(BigDecimal.ZERO)
            .currency(currencyEntityList.get(0))
            .userId(userId)
            .createdDate(Instant.now())
            .build();
    return walletRepository.save(wallet);
  }

  @Override
  public WalletEntity getWallet(UUID id) {
    return walletRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
  }
}
