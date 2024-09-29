package br.com.mcoder.consulta.desafio.dto;

public class SalesSummaryDTO {

    private String sellerName;
    private Double total;

    public SalesSummaryDTO(String sellerName, Double total) {
        this.sellerName = sellerName;
        this.total = total;
    }

    public SalesSummaryDTO(){

    }

    public String getSellerName() {
        return sellerName;
    }

    public Double getTotal() {
        return total;
    }
}
