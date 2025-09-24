package com.micropay.wallet.service;

import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface MessagingService {

    Logger logger = LoggerFactory.getLogger(MessagingService.class);

    void handleDebitWalletEvent(DebitWalletEvent event);

    void handleCreditWalletEvent(CreditWalletEvent event);

    void handleRefundWalletEvent(RefundWalletEvent event);
}
