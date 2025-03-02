package com.example.hogwarts_artifacts_online.artifact;

import com.example.hogwarts_artifacts_online.artifact.converter.ArtifactDtotoArtifactConverter;
import com.example.hogwarts_artifacts_online.artifact.converter.ArtifactToArtifactDtoConverter;
import com.example.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.example.hogwarts_artifacts_online.system.Result;
import com.example.hogwarts_artifacts_online.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArtifactController {
    private final ArtifactService artifactService;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private  final ArtifactDtotoArtifactConverter artifactDtotoArtifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtotoArtifactConverter artifactDtotoArtifactConverter){
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtotoArtifactConverter = artifactDtotoArtifactConverter;
    }

    @GetMapping("/api/v1/artifacts")
    public Result findAllArtifacts(){
        List<ArtifactDto> artifactsList = this.artifactService.findAllArtifacts();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", artifactsList);
    }

    @GetMapping("/api/v1/artifacts/{artifactId}")
    public Result findArtifactsById(@PathVariable String artifactId){
        Artifact foundArtifact = artifactService.findById(artifactId);
        ArtifactDto foundArtifactDTO = this.artifactToArtifactDtoConverter.convert(foundArtifact);
        System.out.println("Artifact:"+ foundArtifactDTO.toString());
        return new Result(true, StatusCode.SUCCESS, "Find One Success", foundArtifactDTO);
    }

    @PostMapping("/api/v1/artifacts")
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        System.out.println(artifactDto.toString());
        //convert dto to entity before sending it to service
        Artifact newArtifact  = this.artifactDtotoArtifactConverter.convert(artifactDto);
        //send this to service method to save in db
        Artifact savedArtifact = artifactService.saveArtifact(newArtifact);
        //convert it back to dto to send it to user
        ArtifactDto savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Add Success",savedArtifactDto );
    }

    @PutMapping("/api/v1/artifacts/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId,@Valid @RequestBody ArtifactDto artifactDto){
        System.out.println(artifactId + "-" +artifactDto.toString());
        Artifact updatedArtifact = artifactService.updateArtifact(artifactDto, artifactId);
        //convert artifact to dto for the end user
        ArtifactDto updatedArtifactDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifactDto);
    }

    @DeleteMapping("/api/v1/artifacts/{artifactId}")
    public Result deleteArtifactById(@PathVariable String artifactId){
        artifactService.deleteArtifactById(artifactId);
        return new Result(true,StatusCode.SUCCESS, "Delete Success", null);
    }
}
