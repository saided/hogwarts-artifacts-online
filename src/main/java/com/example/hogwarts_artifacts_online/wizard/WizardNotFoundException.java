package com.example.hogwarts_artifacts_online.wizard;

public class WizardNotFoundException extends RuntimeException{

    public WizardNotFoundException(Integer wizardId){
        super("Could not find wizard with Id :" + wizardId.toString());
    }
}
