package br.com.mcoder.consulta.desafio.services;


import br.com.mcoder.consulta.desafio.dto.SaleMinDTO;
import br.com.mcoder.consulta.desafio.dto.SalesSummaryDTO;
import br.com.mcoder.consulta.desafio.entities.Sale;
import br.com.mcoder.consulta.desafio.entities.Seller;
import br.com.mcoder.consulta.desafio.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	//@Transactional(readOnly = true)
	public Page<SaleMinDTO> findAll(Pageable pageable) {
		Page<Sale> saleList = repository.findAll(pageable);
		return saleList.map(x -> new SaleMinDTO(x));
	}

	public Page<SaleMinDTO> getSalesReport(String minDate, String maxDate, String sellerName, Pageable pageable) {
		minDate = minDate != null ? minDate.trim() : null;
		maxDate = maxDate != null ? maxDate.trim() : null;

		LocalDate min = (minDate != null && !minDate.isBlank()) ? LocalDate.parse(minDate) : LocalDate.MIN;
		LocalDate max = (maxDate != null && !maxDate.isBlank()) ? LocalDate.parse(maxDate) : LocalDate.MAX;

		return repository.findSales(min, max, sellerName, pageable);
	}

	public List<SalesSummaryDTO> getSalesSummary(String minDateStr, String maxDateStr) {

		LocalDate maxDate = Optional.ofNullable(maxDateStr)
				.filter(str -> !str.isEmpty())
				.map(LocalDate::parse)
				.orElse(LocalDate.now());

		LocalDate minDate = Optional.ofNullable(minDateStr)
				.filter(str -> !str.isEmpty())
				.map(LocalDate::parse)
				.orElse(maxDate.minusYears(1));

		return repository.findSalesSummary(minDate, maxDate);
	}

	public List<SaleMinDTO> getSalesByLastNameAndDate(String name, String minDate, String maxDate) {
		minDate = minDate != null ? minDate.trim() : null;
		maxDate = maxDate != null ? maxDate.trim() : null;

		LocalDate min = (minDate != null && !minDate.isBlank()) ? LocalDate.parse(minDate) : LocalDate.MIN;
		LocalDate max = (maxDate != null && !maxDate.isBlank()) ? LocalDate.parse(maxDate) : LocalDate.MAX;

		return repository.findByLastNameAndDate(name, min, max);
	}


}
