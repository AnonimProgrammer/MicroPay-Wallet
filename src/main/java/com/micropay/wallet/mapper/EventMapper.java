package com.micropay.wallet.mapper;

import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.credit.WalletCreditFailedEvent;
import com.micropay.wallet.dto.credit.WalletCreditedEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.debit.WalletDebitFailedEvent;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import com.micropay.wallet.dto.refund.WalletRefundedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(expression = "java(UUID.randomUUID())", target = "eventId")
    WalletDebitedEvent mapToWalletDebitedEvent(DebitWalletEvent event);

    @Mapping(expression = "java(UUID.randomUUID())", target = "eventId")
    WalletDebitFailedEvent mapToWalletDebitFailedEvent(DebitWalletEvent event, String failureReason);

    @Mapping(expression = "java(UUID.randomUUID())", target = "eventId")
    WalletCreditedEvent mapToWalletCreditedEvent(CreditWalletEvent event);

    @Mapping(expression = "java(UUID.randomUUID())", target = "eventId")
    WalletCreditFailedEvent mapToWalletCreditFailedEvent(CreditWalletEvent event, String failureReason);

    @Mapping(expression = "java(UUID.randomUUID())", target = "eventId")
    WalletRefundedEvent mapToWalletRefundedEvent(RefundWalletEvent event);

}
