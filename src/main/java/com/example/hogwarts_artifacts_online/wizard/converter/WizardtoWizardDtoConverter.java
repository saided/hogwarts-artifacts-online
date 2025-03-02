package com.example.hogwarts_artifacts_online.wizard.converter;

import com.example.hogwarts_artifacts_online.wizard.Wizard;
import com.example.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardtoWizardDtoConverter implements Converter<Wizard, WizardDto> {
    @Override
    public WizardDto convert(Wizard source) {
            WizardDto wizardDto = new WizardDto(source.getId(), source.getName(), source.getNumofArtifacts());
            return wizardDto;
    }
}
