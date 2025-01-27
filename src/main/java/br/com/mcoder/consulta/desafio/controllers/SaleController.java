package br.com.mcoder.consulta.desafio.controllers;


import br.com.mcoder.consulta.desafio.dto.SaleMinDTO;
import br.com.mcoder.consulta.desafio.dto.SalesSummaryDTO;
import br.com.mcoder.consulta.desafio.entities.Sale;
import br.com.mcoder.consulta.desafio.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

    @Autowired
    private SaleService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
        SaleMinDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<SaleMinDTO>> findAll(Pageable pageable) {
        Page<SaleMinDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

	/*@GetMapping(value = "/report")
	public ResponseEntity<?> getReport() {
		// TODO
		return null;
	}*/

	/*@GetMapping(value = "/summary")
	public ResponseEntity<?> getSummary() {
		// TODO
		return null;
	}*/


    @GetMapping(value = "/report")
    public ResponseEntity<Page<SaleMinDTO>> getReport(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String minDate,
            @RequestParam(required = false) String maxDate,
            Pageable pageable) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Se minDate ou maxDate não forem fornecidos, calcula os valores padrão
            LocalDate now = LocalDate.now();
            if (name == null || name.trim().isEmpty()) {
                name = null;
            }

            minDate = (minDate == null || minDate.isBlank())
                    ? now.minusMonths(12).format(formatter)
                    : minDate.trim();
            maxDate = (maxDate == null || maxDate.isBlank())
                    ? now.format(formatter)
                    : maxDate.trim();

            Page<SaleMinDTO> report = service.getSalesReport(minDate, maxDate, name, pageable);
            return ResponseEntity.ok(report);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/summary")
    public ResponseEntity<List<SalesSummaryDTO>> getSummary(
            @RequestParam(value = "minDate", required = false) String minDate,
            @RequestParam(value = "maxDate", required = false) String maxDate) {

        List<SalesSummaryDTO> summary = service.getSalesSummary(minDate, maxDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/lastname")
    public ResponseEntity<List<SaleMinDTO>> findByLastnameAndDate(
            @RequestParam String name,
            @RequestParam(required = false) String minDate,
            @RequestParam(required = false) String maxDate) {
        try {
            List<SaleMinDTO> result = service.getSalesByLastNameAndDate(name, minDate, maxDate);
            return ResponseEntity.ok(result);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


}
