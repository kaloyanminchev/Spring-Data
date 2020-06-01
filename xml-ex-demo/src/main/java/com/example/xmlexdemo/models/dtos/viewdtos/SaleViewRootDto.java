package com.example.xmlexdemo.models.dtos.viewdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sales")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleViewRootDto {

    @XmlElement(name = "sale")
    private List<SalesViewDto> sales;

    public SaleViewRootDto() {
    }

    public List<SalesViewDto> getSales() {
        return sales;
    }

    public void setSales(List<SalesViewDto> sales) {
        this.sales = sales;
    }
}
