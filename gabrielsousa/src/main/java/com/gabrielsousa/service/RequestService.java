package com.gabrielsousa.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.gabrielsousa.domain.Client;
import com.gabrielsousa.domain.ItemRequest;
import com.gabrielsousa.domain.PaymentWithBill;
import com.gabrielsousa.domain.Request;
import com.gabrielsousa.domain.enums.PaymentType;
import com.gabrielsousa.repository.ItemRequestRepository;
import com.gabrielsousa.repository.PaymentRepository;
import com.gabrielsousa.repository.RequestRepository;
import com.gabrielsousa.security.UserSS;
import com.gabrielsousa.service.exception.AuthorizationException;
import com.gabrielsousa.service.exception.ObjectNotFoundException;

@Service
public class RequestService {
	
	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private ItemRequestRepository itemRequestRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private EmailService emailService;

	public Request find(Integer id) {
		Optional<Request> obj = requestRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Request.class.getName()));
	}
	
	public Request insert(Request obj) {
		obj.setId(null);
		obj.setInstant(new Date());
		obj.setClient(clientService.find(obj.getClient().getId()));
		obj.getPayment().setPaymentType(PaymentType.PENDENTE);
		obj.getPayment().setRequest(obj);
		if (obj.getPayment() instanceof PaymentWithBill) {
			PaymentWithBill pagto = (PaymentWithBill) obj.getPayment();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstant());
		}
		obj = requestRepository.save(obj);
		paymentRepository.save(obj.getPayment());
		for (ItemRequest ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduct(productService.find(ip.getProduct().getId()));
			ip.setPreco(ip.getProduct().getPrice());
			ip.setRequest(obj);
		}
		itemRequestRepository.saveAll(obj.getItens());
		System.out.println(obj);
//		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Request> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Client client =  clientService.find(user.getId());
		return requestRepository.findByClient(client, pageRequest);
	}
}
