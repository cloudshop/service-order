package com.eyun.order.service;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eyun.order.client.AuthorizedFeignClient;
import com.eyun.order.domain.Wallet;
import com.eyun.order.service.dto.BalanceDTO;

@AuthorizedFeignClient(name="wallet")
public interface WalletService {

	@PutMapping("/api/wallets/balance")
	public Wallet updateBalance(@RequestBody BalanceDTO balanceDTO);
	
}
