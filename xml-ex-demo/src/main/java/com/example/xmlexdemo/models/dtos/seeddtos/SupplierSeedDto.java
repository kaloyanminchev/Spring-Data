package com.example.xmlexdemo.models.dtos.seeddtos;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "supplier")
@XmlAccessorType(XmlAccessType.FIELD)
public class SupplierSeedDto {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "is-importer")
    private boolean isImporter;

    public SupplierSeedDto() {
    }

    @NotNull(message = "Supplier's name cannot be null!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImporter() {
        return isImporter;
    }

    public void setImporter(boolean importer) {
        isImporter = importer;
    }
}
