package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exceptions.ResourceNotFoundException;
import com.playtomic.tests.wallet.respository.CurrencyRepository;
import com.playtomic.tests.wallet.respository.MovementRepository;
import com.playtomic.tests.wallet.respository.WalletRepository;
import com.playtomic.tests.wallet.respository.entity.CurrencyEntity;
import com.playtomic.tests.wallet.respository.entity.MovementEntity;
import com.playtomic.tests.wallet.respository.entity.WalletEntity;
import com.playtomic.tests.wallet.service.WalletService;
import com.playtomic.tests.wallet.service.stripe.Payment;
import com.playtomic.tests.wallet.service.stripe.StripeService;
import com.playtomic.tests.wallet.util.enums.MovementStatus;
import com.playtomic.tests.wallet.util.enums.MovementType;
import com.playtomic.tests.wallet.util.specs.CurrencySpecs;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  private final CurrencyRepository currencyRepository;
  private final MovementRepository movementRepository;
  private final StripeService stripeService;

  @Override
  public WalletEntity createWallet(Long userId, String currency) {
    List<CurrencyEntity> currencyEntityList =
        currencyRepository.findAll(
            CurrencySpecs.filterByCode(currency).or(CurrencySpecs.filterByNumber(currency)));
    if (currencyEntityList.isEmpty()) {
      throw new ResourceNotFoundException("currency not found");
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
        .orElseThrow(() -> new ResourceNotFoundException("wallet not found"));
  }

  @Override
  public WalletEntity fundWallet(UUID id, BigDecimal amount, String card) {
    WalletEntity wallet =
        walletRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("wallet not found"));

    Payment payment = stripeService.charge(card, amount);

    MovementEntity movementEntity = createMovementEntity(wallet, payment, amount);

    try {
      updateBalance(wallet, amount);
      return wallet;
    } catch (Exception ex) {
      log.error("Error updating balance", ex);
      stripeService.refund(payment.getId());
      movementEntity.setStatus(MovementStatus.DECLINED);
      movementRepository.save(movementEntity);
      throw ex;
    }
  }

  private MovementEntity createMovementEntity(
      WalletEntity wallet, Payment payment, BigDecimal amount) {
    MovementEntity movement =
        MovementEntity.builder()
            .amount(amount)
            .externalId(payment.getId())
            .type(MovementType.CREDIT)
            .status(MovementStatus.APPROVED)
            .wallet(wallet)
            .build();
    return movementRepository.save(movement);
  }

  private void updateBalance(WalletEntity walletEntity, BigDecimal amount) {
    walletEntity.setBalance(walletEntity.getBalance().add(amount));
    walletEntity.setAvailableBalance(walletEntity.getAvailableBalance().add(amount));
    walletEntity.setUpdatedDate(Instant.now());
    walletRepository.save(walletEntity);
  }
}
