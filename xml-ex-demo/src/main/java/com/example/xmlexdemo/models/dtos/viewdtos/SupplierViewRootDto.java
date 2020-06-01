package com.example.xmlexdemo.models.dtos.viewdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "suppliers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SupplierViewRootDto {

    @XmlElement(name = "supplier")
    private List<SupplierViewDto> suppliers;

    public SupplierViewRootDto() {
    }

    public List<SupplierViewDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierViewDto> suppliers) {
        this.suppliers = suppliers;
    }
}
