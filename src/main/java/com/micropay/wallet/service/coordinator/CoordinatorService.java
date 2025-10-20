package com.micropay.wallet.service.coordinator;

import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CoordinatorService {

    Logger logger = LoggerFactory.getLogger(CoordinatorService.class);

    void handleDebitWalletEvent(DebitWalletEvent event);

    void handleCreditWalletEvent(CreditWalletEvent event);

    void handleRefundWalletEvent(RefundWalletEvent event);
}
