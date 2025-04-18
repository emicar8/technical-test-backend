package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.request.CreateWalletRequest;
import com.playtomic.tests.wallet.api.response.WalletResponseDto;
import com.playtomic.tests.wallet.respository.entity.WalletEntity;
import com.playtomic.tests.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;
  private Logger log = LoggerFactory.getLogger(WalletController.class);

  @RequestMapping("/")
  void log() {
    log.info("Logging from /");
  }

  @PostMapping("/wallet")
  ResponseEntity<WalletResponseDto> createWallet(
      @RequestBody CreateWalletRequest createWalletRequest) {
    WalletEntity wallet =
        walletService.createWallet(
            createWalletRequest.getUserId(), createWalletRequest.getCurrency());
    return ResponseEntity.ok(buildWalletResponseDto(wallet));
  }

  private WalletResponseDto buildWalletResponseDto(WalletEntity walletEntity) {
    return WalletResponseDto.builder()
        .id(walletEntity.getId())
        .availableBalance(walletEntity.getAvailableBalance())
        .userId(walletEntity.getUserId())
        .balance(walletEntity.getBalance())
        .createdDate(walletEntity.getCreatedDate())
        .updatedDate(walletEntity.getUpdatedDate())
        .currency(walletEntity.getCurrency().getCode())
        .build();
  }
}
