package com.example.hogwarts_artifacts_online.wizard;

import com.example.hogwarts_artifacts_online.artifact.Artifact;
import com.example.hogwarts_artifacts_online.artifact.ArtifactNotFoundException;
import com.example.hogwarts_artifacts_online.artifact.ArtifactRepository;
import com.example.hogwarts_artifacts_online.system.utils.IdWorker;
import org.springframework.stereotype.Service;

@Service
public class WizardService {

    private final WizardRepository wizardRepository;
    private final ArtifactRepository artifactRepository;
    private final IdWorker idWorker;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Wizard getWizardById(Integer wizardId){
        return wizardRepository.findById(wizardId).orElseThrow(()-> new WizardNotFoundException(wizardId));
    }

    public Wizard saveWizard(Wizard wizard){
        long idVal = Math.abs(idWorker.nextId());
        System.out.println(idVal);
        int intIdVal = (int) idVal;
        System.out.println(intIdVal);
        wizard.setId(intIdVal);
        return wizardRepository.save(wizard);
    }

    public Artifact addArtifactToWizard(Integer wizardId, String artifactId){
        //fetch artifact
        Artifact returnedArtifact = artifactRepository.findById(artifactId)
                .orElseThrow(()-> new ArtifactNotFoundException(artifactId));

        //fetch wizard
        Wizard returnedWizard = wizardRepository.findById(wizardId)
                .orElseThrow(()-> new WizardNotFoundException(wizardId));

        //add artifact to wizard
//        first make set owner as null for the artifact if the owner already exists
//        remove the artifact from that wizards list of the old wizard
//        assign  it to new wizard (set artifact owner to new wizard owner& add this artifact to the wizards list of artifcats)
        //Check whether the artifact already has an owner
        if(returnedArtifact.getOwner() != null){
            returnedArtifact.getOwner().removeArtifact(returnedArtifact);
        }
        //returnedArtifact.setOwner(returnedWizard);
        returnedWizard.addArtifact(returnedArtifact);
        artifactRepository.save(returnedArtifact);
        return returnedArtifact;
    }

    public void removeWizard(Integer wizardID){
        Wizard returnedWizard = this.wizardRepository.findById(wizardID).orElseThrow(()-> new WizardNotFoundException(wizardID));
        //delete a wizard ->delete the wizard and remove all the associated artifacts from the wizard
        //remove all artifacts i,e setting the owner as null and empty the array
        returnedWizard.removeAllArtifacts();
        wizardRepository.deleteById(wizardID);
    }
}
