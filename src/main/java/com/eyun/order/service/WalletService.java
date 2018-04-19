package com.eyun.order.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codahale.metrics.annotation.Timed;
import com.eyun.order.client.AuthorizedFeignClient;
import com.eyun.order.domain.Wallet;
import com.eyun.order.service.dto.BalanceDTO;
import com.eyun.order.service.dto.PayOrderDTO;

@AuthorizedFeignClient(name="wallet",decode404=true)
public interface WalletService {

	@PutMapping("/api/wallets/balance")
	public Wallet updateBalance(@RequestBody BalanceDTO balanceDTO);
	
	@GetMapping("/api/wallets/user")
	public Wallet getUserWallet();
	
	@GetMapping("/pay-orders/{payNo}")
    public ResponseEntity<PayOrderDTO> getPayOrderByPayNo(@PathVariable("payNo") String payNo);
	
}
