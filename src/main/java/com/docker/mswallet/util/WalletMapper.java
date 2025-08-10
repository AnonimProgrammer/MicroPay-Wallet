package com.docker.mswallet.util;

import com.docker.mswallet.entity.Wallet;
import com.docker.mswallet.model.WalletModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletModel toModel(Wallet wallet);

    Wallet toEntity(WalletModel model);
}
