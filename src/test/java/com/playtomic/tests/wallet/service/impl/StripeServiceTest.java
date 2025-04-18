package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.service.stripe.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.stripe.StripeService;
import com.playtomic.tests.wallet.service.stripe.StripeServiceException;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

/**
 * This test is failing with the current implementation.
 *
 * <p>How would you test this?
 */
public class StripeServiceTest {

  URI testUri = URI.create("https://sandbox.playtomic.io/v1/stripe-simulator/charges");
  StripeService s = new StripeService(testUri, testUri, new RestTemplateBuilder());

  @Test
  public void test_exception() {
    Assertions.assertThrows(
        StripeAmountTooSmallException.class,
        () -> {
          s.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
  }

  @Test
  public void test_ok() throws StripeServiceException {
    s.charge("4242 4242 4242 4242", new BigDecimal(15));
  }
}
