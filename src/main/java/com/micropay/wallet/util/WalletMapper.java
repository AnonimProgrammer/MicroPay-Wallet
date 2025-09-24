package com.micropay.wallet.util;

import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.model.WalletModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletModel toModel(Wallet wallet);

    Wallet toEntity(WalletModel model);
}
