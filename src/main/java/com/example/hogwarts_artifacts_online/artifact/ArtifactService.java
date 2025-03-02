package com.example.hogwarts_artifacts_online.artifact;

import com.example.hogwarts_artifacts_online.artifact.converter.ArtifactToArtifactDtoConverter;
import com.example.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.example.hogwarts_artifacts_online.system.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, IdWorker idWorker){
        this.artifactRepository = artifactRepository;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.idWorker = idWorker;

    }

    public List<ArtifactDto> findAllArtifacts(){
        //return null;
        List<Artifact> artifactsList =  artifactRepository.findAll();
        List<ArtifactDto> artifactsListDTO =artifactsList.stream().map(this.artifactToArtifactDtoConverter::convert).collect(Collectors.toList());
        return artifactsListDTO;
    }

    public Artifact findById(String artifactId){
        return artifactRepository.findById(artifactId).
                orElseThrow(()-> new ArtifactNotFoundException(artifactId));
    }

    public Artifact saveArtifact(Artifact artifact){
        //we need to generate an id for the newly saving artifact, this is done using id worker
        artifact.setId(idWorker.nextId() + "");
        return artifactRepository.save(artifact);
    }

    public Artifact updateArtifact(ArtifactDto artifactDto, String artifactId){
        //fetch the existing artifact by id or else throw exception
        Artifact exisitingArtifact = this.findById(artifactId);
        //update the existing artifact with the data from dto
        exisitingArtifact.setName(artifactDto.name() != null ? artifactDto.name() : exisitingArtifact.getName());
        exisitingArtifact.setDescription(artifactDto.description() != null ? artifactDto.description() : exisitingArtifact.getDescription());
        exisitingArtifact.setImageURL(artifactDto.imageURL() != null ? artifactDto.imageURL() : exisitingArtifact.getImageURL());
        //save the  modified artifact to db
        return artifactRepository.save(exisitingArtifact);
    }

    public void deleteArtifactById(String artifactId){
        Artifact artifact = this.findById(artifactId);
         artifactRepository.deleteById(artifactId);
    }
}
