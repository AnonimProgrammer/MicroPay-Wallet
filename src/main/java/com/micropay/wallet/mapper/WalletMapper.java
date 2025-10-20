package com.micropay.wallet.mapper;

import com.micropay.wallet.dto.response.WalletResponse;
import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.model.WalletModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletModel toModel(Wallet wallet);

    WalletResponse toResponse(Wallet wallet);
}
