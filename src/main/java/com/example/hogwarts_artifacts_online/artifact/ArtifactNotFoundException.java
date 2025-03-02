package com.example.hogwarts_artifacts_online.artifact;

public class ArtifactNotFoundException extends RuntimeException{

    public ArtifactNotFoundException(String id){
        super("Could not find artifact with the id:" + id );
    }

}
