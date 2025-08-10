package com.docker.mswallet.config;

public class RabbitConstants {

    public static final String WALLET_DEBIT_EXCHANGE = "wallet.debit.event";
    public static final String WALLET_CREDIT_EXCHANGE = "wallet.credit.event";
    public static final String WALLET_REFUND_EXCHANGE = "wallet.refund.event";

    public static final String WALLET_DEBIT_QUEUE = "wallet.debit.queue";
    public static final String WALLET_DEBITED_QUEUE = "wallet.debited.queue";
    public static final String WALLET_DEBIT_FAILED_QUEUE = "wallet.debit.failed.queue";
    public static final String WALLET_CREDIT_QUEUE = "wallet.credit.queue";
    public static final String WALLET_CREDITED_QUEUE = "wallet.credited.queue";
    public static final String WALLET_CREDIT_FAILED_QUEUE = "wallet.credit.failed.queue";
    public static final String WALLET_REFUND_QUEUE = "wallet.refund.queue";
    public static final String WALLET_REFUNDED_QUEUE = "wallet.refunded.queue";

    public static final String WALLET_DEBIT_ROUTING_KEY = "wallet.debit";
    public static final String WALLET_DEBITED_ROUTING_KEY = "wallet.debited";
    public static final String WALLET_DEBIT_FAILED_ROUTING_KEY = "wallet.debit.failed";
    public static final String WALLET_CREDIT_ROUTING_KEY = "wallet.credit";
    public static final String WALLET_CREDITED_ROUTING_KEY = "wallet.credited";
    public static final String WALLET_CREDIT_FAILED_ROUTING_KEY = "wallet.credit.failed";
    public static final String WALLET_REFUND_ROUTING_KEY = "wallet.refund";
    public static final String WALLET_REFUNDED_ROUTING_KEY = "wallet.refunded";

}
