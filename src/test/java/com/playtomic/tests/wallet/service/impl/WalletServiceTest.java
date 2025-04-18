package com.playtomic.tests.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.exceptions.ResourceNotFoundException;
import com.playtomic.tests.wallet.respository.CurrencyRepository;
import com.playtomic.tests.wallet.respository.MovementRepository;
import com.playtomic.tests.wallet.respository.WalletRepository;
import com.playtomic.tests.wallet.respository.entity.CurrencyEntity;
import com.playtomic.tests.wallet.respository.entity.MovementEntity;
import com.playtomic.tests.wallet.respository.entity.WalletEntity;
import com.playtomic.tests.wallet.service.stripe.Payment;
import com.playtomic.tests.wallet.service.stripe.StripeService;
import com.playtomic.tests.wallet.service.stripe.StripeServiceException;
import com.playtomic.tests.wallet.util.enums.MovementStatus;
import com.playtomic.tests.wallet.util.enums.MovementType;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

  @InjectMocks WalletServiceImpl walletService;
  @Captor ArgumentCaptor<WalletEntity> walletCaptor;
  @Captor ArgumentCaptor<MovementEntity> movementCaptor;
  @Mock private WalletRepository walletRepositoryMock;
  @Mock private CurrencyRepository currencyRepositoryMock;
  @Mock private MovementRepository movementRepositoryMock;
  @Mock private StripeService stripeServiceMock;

  @Test
  public void whenCreateWallet_givenNotFoundCurrency_thenException() {
    // Setup
    when(currencyRepositoryMock.findAll(any(Specification.class)))
        .thenReturn(Collections.emptyList());
    ResourceNotFoundException expected = new ResourceNotFoundException("currency not found");

    // Execute and Verify
    ResourceNotFoundException result =
        assertThrows(ResourceNotFoundException.class, () -> walletService.createWallet(1L, "ASD"));
    assertEquals(expected.getMessage(), result.getMessage());
  }

  @Test
  public void whenCreateWallet_givenCorrectData_thenOk() {
    // Setup
    CurrencyEntity currency = CurrencyEntity.builder().id(1L).code("EUR").number("978").build();
    when(currencyRepositoryMock.findAll(any(Specification.class))).thenReturn(List.of(currency));
    when(walletRepositoryMock.save(any())).thenReturn(WalletEntity.builder().build());

    // Execute
    walletService.createWallet(1L, "EUR");

    // Validate
    verify(walletRepositoryMock).save(walletCaptor.capture());
    WalletEntity capturedValue = walletCaptor.getValue();
    assertEquals(1L, capturedValue.getUserId());
    assertEquals(BigDecimal.ZERO, capturedValue.getBalance());
    assertEquals(BigDecimal.ZERO, capturedValue.getAvailableBalance());
    assertEquals(currency, capturedValue.getCurrency());
  }

  @Test
  public void whenGetWallet_givenNotFoundWallet_thenException() {
    // Setup
    when(walletRepositoryMock.findById(any())).thenReturn(Optional.empty());
    ResourceNotFoundException expected = new ResourceNotFoundException("wallet not found");

    // Execute and Verify
    ResourceNotFoundException result =
        assertThrows(
            ResourceNotFoundException.class, () -> walletService.getWallet(UUID.randomUUID()));
    assertEquals(expected.getMessage(), result.getMessage());
  }

  @Test
  public void whenFundWallet_givenNotFoundWallet_thenException() {
    // Setup
    when(walletRepositoryMock.findById(any())).thenReturn(Optional.empty());
    ResourceNotFoundException expected = new ResourceNotFoundException("wallet not found");

    // Execute and Verify
    ResourceNotFoundException result =
        assertThrows(
            ResourceNotFoundException.class,
            () -> walletService.fundWallet(UUID.randomUUID(), BigDecimal.ONE, ""));
    assertEquals(expected.getMessage(), result.getMessage());
  }

  @Test
  public void whenFundWallet_givenStripeServiceException_thenException() {
    // Setup
    WalletEntity wallet = WalletEntity.builder().build();
    when(walletRepositoryMock.findById(any())).thenReturn(Optional.of(wallet));
    when(stripeServiceMock.charge(any(), any())).thenThrow(new StripeServiceException());

    // Execute and Verify
    assertThrows(
        StripeServiceException.class,
        () -> walletService.fundWallet(UUID.randomUUID(), BigDecimal.ONE, ""));
  }

  @Test
  public void whenFundWallet_givenCreateMovementException_thenRefundAndException() {
    // Setup
    UUID uuid = UUID.randomUUID();
    String paymentId = "1";
    WalletEntity wallet = WalletEntity.builder().id(uuid).build();
    when(walletRepositoryMock.findById(any())).thenReturn(Optional.of(wallet));
    when(stripeServiceMock.charge(any(), any()))
        .thenReturn(Payment.builder().id(paymentId).build());
    when(movementRepositoryMock.save(any())).thenThrow(new RuntimeException());

    // Execute and Verify
    assertThrows(RuntimeException.class, () -> walletService.fundWallet(uuid, BigDecimal.ONE, ""));
    verify(stripeServiceMock, times(1)).refund(paymentId);
    verify(movementRepositoryMock).save(movementCaptor.capture());
    MovementEntity capturedValue = movementCaptor.getValue();
    assertEquals(BigDecimal.ONE, capturedValue.getAmount());
    assertEquals(paymentId, capturedValue.getExternalId());
    assertEquals(MovementType.CREDIT, capturedValue.getType());
    assertEquals(MovementStatus.APPROVED, capturedValue.getStatus());
    assertEquals(wallet, capturedValue.getWallet());
  }

  @Test
  public void whenFundWallet_givenUpdateWalletException_thenRefundUpdateMovementAndException() {
    // Setup
    UUID uuid = UUID.randomUUID();
    String paymentId = "1";
    WalletEntity wallet =
        WalletEntity.builder()
            .id(uuid)
            .balance(new BigDecimal("10.32"))
            .availableBalance(new BigDecimal("5.71"))
            .build();
    when(walletRepositoryMock.findById(any())).thenReturn(Optional.of(wallet));
    when(stripeServiceMock.charge(any(), any()))
        .thenReturn(Payment.builder().id(paymentId).build());
    when(movementRepositoryMock.save(any())).thenReturn(MovementEntity.builder().build());
    when(walletRepositoryMock.save(any())).thenThrow(new RuntimeException());

    // Execute and Verify
    assertThrows(
        RuntimeException.class, () -> walletService.fundWallet(uuid, new BigDecimal("1.44"), ""));
    verify(stripeServiceMock, times(1)).refund(paymentId);
    verify(movementRepositoryMock, times(2)).save(movementCaptor.capture());
    verify(walletRepositoryMock).save(walletCaptor.capture());
    MovementEntity movementCaptorValue = movementCaptor.getValue();
    assertEquals(MovementStatus.DECLINED, movementCaptorValue.getStatus());
    WalletEntity walletCaptorValue = walletCaptor.getValue();
    assertEquals(new BigDecimal("11.76"), walletCaptorValue.getBalance());
    assertEquals(new BigDecimal("7.15"), walletCaptorValue.getAvailableBalance());
    assertNotNull(walletCaptorValue.getUpdatedDate());
  }

  @Test
  public void whenFundWallet_givenUpdateWalletSuccessful_thenOk() {
    // Setup
    UUID uuid = UUID.randomUUID();
    String paymentId = "1";
    WalletEntity wallet =
        WalletEntity.builder()
            .id(uuid)
            .balance(new BigDecimal("10.32"))
            .availableBalance(new BigDecimal("5.71"))
            .build();
    when(walletRepositoryMock.findById(any())).thenReturn(Optional.of(wallet));
    when(stripeServiceMock.charge(any(), any()))
        .thenReturn(Payment.builder().id(paymentId).build());
    when(movementRepositoryMock.save(any())).thenReturn(MovementEntity.builder().build());
    when(walletRepositoryMock.save(any())).thenReturn(WalletEntity.builder().build());

    // Execute
    walletService.fundWallet(uuid, new BigDecimal("1.44"), "");

    // Verify
    verify(movementRepositoryMock, times(1)).save(movementCaptor.capture());
    verify(walletRepositoryMock).save(walletCaptor.capture());
    MovementEntity movementCaptorValue = movementCaptor.getValue();
    assertEquals(new BigDecimal("1.44"), movementCaptorValue.getAmount());
    assertEquals(paymentId, movementCaptorValue.getExternalId());
    assertEquals(MovementType.CREDIT, movementCaptorValue.getType());
    assertEquals(MovementStatus.APPROVED, movementCaptorValue.getStatus());
    assertEquals(wallet, movementCaptorValue.getWallet());
    WalletEntity walletCaptorValue = walletCaptor.getValue();
    assertEquals(new BigDecimal("11.76"), walletCaptorValue.getBalance());
    assertEquals(new BigDecimal("7.15"), walletCaptorValue.getAvailableBalance());
    assertNotNull(walletCaptorValue.getUpdatedDate());
  }
}
