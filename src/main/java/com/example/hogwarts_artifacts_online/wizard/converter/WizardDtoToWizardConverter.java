package com.example.hogwarts_artifacts_online.wizard.converter;

import com.example.hogwarts_artifacts_online.wizard.Wizard;
import com.example.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {
    @Override
    public Wizard convert(WizardDto source) {
        Wizard w = new Wizard();
        w.setId(source.id());
        w.setName(source.name());
        return w;
    }
}
