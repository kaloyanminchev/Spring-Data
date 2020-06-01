package demos.springdata.advanced.services;

import demos.springdata.advanced.entities.Label;

public interface LabelService {
    Label findById(Long id);
}
