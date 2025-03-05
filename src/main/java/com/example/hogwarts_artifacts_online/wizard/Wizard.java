package com.example.hogwarts_artifacts_online.wizard;

import com.example.hogwarts_artifacts_online.artifact.Artifact;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {

    @Id
    private Integer id;

    private String name;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public void addArtifact(Artifact artifact){
        artifact.setOwner(this);
        this.artifacts.add(artifact);
    }

    public Integer getNumofArtifacts() {
        return this.artifacts.size();
    }

    @Override
    public String toString() {
        return "Wizard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artifacts=" + artifacts +
                '}';
    }

    public void removeArtifact(Artifact returnedArtifact) {
        returnedArtifact.setOwner(null);
        this.artifacts.remove(returnedArtifact);
    }

    public void removeAllArtifacts() {
        this.artifacts.stream().forEach(artifact -> {
            artifact.setOwner(null);
        });
        this.artifacts = new ArrayList<>();

    }
}
