package com.example.hogwarts_artifacts_online.wizard;

import com.example.hogwarts_artifacts_online.artifact.Artifact;
import com.example.hogwarts_artifacts_online.artifact.converter.ArtifactToArtifactDtoConverter;
import com.example.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.example.hogwarts_artifacts_online.system.Result;
import com.example.hogwarts_artifacts_online.system.StatusCode;
import com.example.hogwarts_artifacts_online.wizard.converter.WizardDtoToWizardConverter;
import com.example.hogwarts_artifacts_online.wizard.converter.WizardtoWizardDtoConverter;
import com.example.hogwarts_artifacts_online.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class WizardController {

    private final WizardService wizardService;
    private final WizardtoWizardDtoConverter wizardtoWizardDtoConverter;
    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;

    public WizardController(WizardService wizardService, WizardtoWizardDtoConverter wizardtoWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter) {
        this.wizardService = wizardService;
        this.wizardtoWizardDtoConverter = wizardtoWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
    }

    @GetMapping("/api/v1/wizards/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId){
        Wizard wiz = this.wizardService.getWizardById(wizardId);
        //convert wizard entity to wizard dto
        WizardDto wizardDto = this.wizardtoWizardDtoConverter.convert(wiz);
       // System.out.println(wiz.toString());
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDto);
    }

    @PostMapping("/api/v1/wizards")
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto){
        //convert dto to entity before passing it to service
        Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = wizardService.saveWizard(wizard);
        //Convert back the wizard to wizard dto for the end user
        WizardDto savedWizardDto = this.wizardtoWizardDtoConverter.convert(wizard);
        return new Result(true,StatusCode.SUCCESS, "Add Success", savedWizardDto);

    }

    @PutMapping("/api/v1/wizards/{wizardId}/artifacts/{artifactId}")
    public Result addArtifactToWizard(@PathVariable("wizardId") Integer wizardId,
                                      @PathVariable("artifactId") String artifactId
                                      ){
        //first fetch the artifact using artifact id & wizard using wizard id
        Artifact changedArtifact = wizardService.addArtifactToWizard(wizardId, artifactId);
        //Artifact to Dto
        ArtifactDto changedArtifactDto = this.artifactToArtifactDtoConverter.convert(changedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Artifact Assignment Success",changedArtifactDto);
    }

    @DeleteMapping("/api/v1/wizards/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId){
        wizardService.removeWizard(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete One Success");

    }
}
