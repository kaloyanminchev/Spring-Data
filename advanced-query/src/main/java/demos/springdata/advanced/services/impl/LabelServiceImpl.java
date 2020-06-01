package demos.springdata.advanced.services.impl;

import demos.springdata.advanced.entities.Label;
import demos.springdata.advanced.repositories.LabelRepository;
import demos.springdata.advanced.services.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    @Autowired
    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public Label findById(Long id) {
        return this.labelRepository.findOneById(id);
    }
}
