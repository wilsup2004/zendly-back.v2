package com.weight.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PaypalService {
    private final APIContext apiContext;

    public PaypalService(@Value("${paypal.client.id}") String clientId,
                         @Value("${paypal.client.secret}") String clientSecret,
                         @Value("${paypal.mode}") String mode) {
        this.apiContext = new APIContext(clientId, clientSecret, mode);
    }

    public Payment createPayment(Double total, String currency, String method,
                                 String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }
}