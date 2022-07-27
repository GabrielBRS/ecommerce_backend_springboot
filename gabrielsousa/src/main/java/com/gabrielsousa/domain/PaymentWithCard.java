package com.gabrielsousa.domain;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gabrielsousa.domain.enums.PaymentType;

@Entity
@JsonTypeName("PaymentWithCard")
public class PaymentWithCard extends Payment{
	private static final long serialVersionUID = 1L;

	private Integer numeroDeParcelas;
	
	public PaymentWithCard() {
		
	}

	public PaymentWithCard(Integer id, PaymentType paymentType, Request request, Integer numeroDeParcelas) {
		super(id, paymentType, request);
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}
	
}
