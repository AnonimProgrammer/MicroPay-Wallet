package com.docker.mswallet.service;

import com.docker.mswallet.dto.credit.CreditWalletEvent;
import com.docker.mswallet.dto.debit.DebitWalletEvent;
import com.docker.mswallet.dto.refund.RefundWalletEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface MessagingService {

    Logger logger = LoggerFactory.getLogger(MessagingService.class);

    void handleDebitWalletEvent(DebitWalletEvent event);

    void handleCreditWalletEvent(CreditWalletEvent event);

    void handleRefundWalletEvent(RefundWalletEvent event);
}
