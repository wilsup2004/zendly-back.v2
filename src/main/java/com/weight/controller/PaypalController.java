package com.weight.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.weight.services.PaypalService;

@RestController
@RequestMapping("/paypal")
public class PaypalController {

	 private PaypalService paypalService;

    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    @PostMapping("/pay")
    public String pay() {
        try {
            Payment payment = paypalService.createPayment(20.0, "USD", "paypal",
                    "sale", "Achat de produit", "http://localhost:8081/paypal/cancel",
                    "http://localhost:8081/paypal/success");
            return payment.getLinks().get(1).getHref(); // Lien de paiement
        } catch (PayPalRESTException e) {
            return "Erreur : " + e.getMessage();
        }
    }
}
