package com.ohbs.payments.exception;

public class PaymentCancellationException extends RuntimeException {
    public PaymentCancellationException(String message) {
        super(message);
    }
}
