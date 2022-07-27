package com.gabrielsousa.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.gabrielsousa.domain.PaymentWithBill;

@Service
public class BoletoService {

	public void preencherPagamentoComBoleto(PaymentWithBill pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}
}
