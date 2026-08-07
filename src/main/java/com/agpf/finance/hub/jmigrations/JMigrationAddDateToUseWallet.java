package com.agpf.finance.hub.jmigrations;

import com.agpf.finance.hub.models.wallet.Wallet;
import com.agpf.finance.hub.repositories.wallet.WalletRepository;
import com.agpf.finance.hub.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JMigrationAddDateToUseWallet {

    private final WalletRepository walletRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void execute() {
        addDateToUseWallet();
    }

    private void addDateToUseWallet() {
        var existsDateToUseNull = walletRepository.existsDateToUseNull();

        if (existsDateToUseNull) {
            log.info("Ignorando Migration addDateToUseWallet - Ja foi executada.");
            return;
        }

        log.info("Executando Migration: addDateToUseWallet");

        var wallets = walletRepository.findAll();

        var walletsToSave = new ArrayList<Wallet>();

        for (Wallet currentWallet : wallets) {
            var createdAt = currentWallet.getCreatedAt();

            var dateToUse = LocalDate.ofInstant(createdAt, ZoneId.of(DateUtils.ZONE_ID));

            currentWallet.setDateToUse(dateToUse.with(TemporalAdjusters.firstDayOfMonth()));

            walletsToSave.add(currentWallet);
        }

        walletRepository.saveAll(walletsToSave);
    }

}
