package com.example.hogwarts_artifacts_online.artifact.converter;

import com.example.hogwarts_artifacts_online.artifact.Artifact;
import com.example.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.example.hogwarts_artifacts_online.wizard.Wizard;
import com.example.hogwarts_artifacts_online.wizard.converter.WizardtoWizardDtoConverter;
import com.example.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {
    private WizardtoWizardDtoConverter wizardtoWizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizardtoWizardDtoConverter wizardtoWizardDtoConverter) {
        this.wizardtoWizardDtoConverter = wizardtoWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        Wizard ownerWizard  = source.getOwner();
        WizardDto ownerDto = ownerWizard !=null ? this.wizardtoWizardDtoConverter.convert(ownerWizard) : null;
        ArtifactDto artifactDto = new ArtifactDto(source.getId(), source.getName(), source.getDescription(), source.getImageURL(), ownerDto);
        return artifactDto;
    }
}
