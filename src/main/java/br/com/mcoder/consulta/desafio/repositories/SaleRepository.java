package br.com.mcoder.consulta.desafio.repositories;


import br.com.mcoder.consulta.desafio.dto.SaleMinDTO;
import br.com.mcoder.consulta.desafio.dto.SalesSummaryDTO;
import br.com.mcoder.consulta.desafio.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new br.com.mcoder.consulta.desafio.dto.SaleMinDTO(s.id, s.amount, s.date, s.seller.name) "
            + "FROM Sale s "
            + "WHERE s.date BETWEEN :minDate AND :maxDate "
            + "AND LOWER(s.seller.name) LIKE LOWER(CONCAT('%', :sellerName, '%'))")
    Page<SaleMinDTO> findSales(LocalDate minDate, LocalDate maxDate, String sellerName, Pageable pageable);


    @Query("SELECT new br.com.mcoder.consulta.desafio.dto.SalesSummaryDTO(s.seller.name, SUM(s.amount)) "
            + "FROM Sale s "
            + "WHERE s.date BETWEEN :minDate AND :maxDate "
            + "GROUP BY s.seller.name")
    List<SalesSummaryDTO> findSalesSummary(LocalDate minDate, LocalDate maxDate);


}
