package com.micropay.wallet.service.messaging.outbox;

import com.micropay.wallet.dto.credit.WalletCreditFailedEvent;
import com.micropay.wallet.dto.credit.WalletCreditedEvent;
import com.micropay.wallet.dto.debit.WalletDebitFailedEvent;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.dto.refund.WalletRefundedEvent;

public interface MessageDispatcher {

    void publishWalletCreditedEvent(WalletCreditedEvent event);

    void publishWalletCreditFailedEvent(WalletCreditFailedEvent event);

    void publishWalletDebitedEvent(WalletDebitedEvent event);

    void publishWalletDebitFailedEvent(WalletDebitFailedEvent event);

    void publishWalletRefundedEvent(WalletRefundedEvent event);

}
