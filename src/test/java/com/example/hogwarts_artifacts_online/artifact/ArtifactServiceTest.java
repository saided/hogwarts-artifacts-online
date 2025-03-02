package com.example.hogwarts_artifacts_online.artifact;

import com.example.hogwarts_artifacts_online.artifact.converter.ArtifactToArtifactDtoConverter;
import com.example.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.hogwarts_artifacts_online.system.utils.IdWorker;
import com.example.hogwarts_artifacts_online.wizard.Wizard;
import com.example.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;

    @Mock
    IdWorker idWorker;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    ArtifactService artifactService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {

        //Given (Here we prepare the fake data and arrange inputs and targets . Define the behaviour of mock object artifactRepository)
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setName("Invisibility Cloak");
        a.setImageURL("ImageUrl");
        a.setOwner(w);


       // a.setOwner(w);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));


        //When( Here we define the actual method in service class that needs to be tested)

        Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        System.out.println(returnedArtifact.toString());


        //Then (Assert expected outcomes)
        assertEquals(returnedArtifact.getId(), a.getId());
        assertEquals(returnedArtifact.getName(), a.getName());
        assertEquals(returnedArtifact.getDescription(), a.getDescription());
        assertEquals(returnedArtifact.getImageURL(), a.getImageURL());
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdFailed(){
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        });

        assertInstanceOf(ArtifactNotFoundException.class, thrown);
        assertEquals("Could not find artifact with the id:1250808601744904192", thrown.getMessage());

    }

    @Test
    void testFindAllArtifactsSuccess(){
        //given
        List<ArtifactDto> artifactsList = new ArrayList<>();
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setName("Invisibility Cloak");
        a.setImageURL("ImageUrl");
        a.setOwner(w);
        System.out.println(a.toString());
        WizardDto wizDto = new WizardDto(1,"Harry Potter", 1);
        ArtifactDto expectedDto = new ArtifactDto(a.getId(),a.getName(),a.getDescription(),a.getImageURL(),wizDto);
        //ArtifactDto afDto = artifactToArtifactDtoConverter.convert(a);

        System.out.println(expectedDto);
        artifactsList.add(expectedDto);
        System.out.println(artifactsList.get(0).description());
       when( artifactRepository.findAll()).thenReturn(List.of(a));
        when(artifactToArtifactDtoConverter.convert(a)).thenReturn(expectedDto);


       // call the service method
        List<ArtifactDto> artifactDtoList = artifactService.findAllArtifacts();
        System.out.println(artifactDtoList.toString());

        //check the assert equals
        //assertEquals(artifactDtoList.size(),1);
        assertEquals(artifactDtoList.get(0).id(), "1250808601744904192");
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveArtifactSuccess(){
        Artifact a = new Artifact();
        a.setName("TestArtifact");
        a.setDescription("Artifact testing");
        a.setImageURL("image.jpg");

        //repository calling and also id worker generation
        when(idWorker.nextId()).thenReturn(123456L);
        when(artifactRepository.save(a)).thenReturn(a);

        //call the service method
        Artifact savedArtifact = artifactService.saveArtifact(a);

        //asserts and equals
        assertEquals(savedArtifact.getId(), "123456");
        assertEquals(savedArtifact.getName(), a.getName());
        assertEquals(savedArtifact.getDescription(), a.getDescription());
        assertEquals(savedArtifact.getImageURL(), a.getImageURL());
    }

    @Test
    void testUpdateArtifactSuccess(){
        //Mock the data
        ArtifactDto aDto = new ArtifactDto(null,"Deluminator-update","A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update","ImageUrl-update",null);

        Artifact existingArtifact = new Artifact();
        existingArtifact.setId("1250808601744904191");
        existingArtifact.setName("Deluminator");
        existingArtifact.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        existingArtifact.setImageURL("ImageUrl");

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("1250808601744904191");
        updatedArtifact.setName(aDto.name());
        updatedArtifact.setDescription(aDto.description());
        updatedArtifact.setImageURL(aDto.imageURL());

        //call the repository method
        when(artifactRepository.findById("1250808601744904191")).thenReturn(Optional.of(existingArtifact));
        when(artifactRepository.save(Mockito.any(Artifact.class))).thenReturn(updatedArtifact);

        //call the service method
        Artifact artifact = artifactService.updateArtifact(aDto, "1250808601744904191");
        //asserts and equals
        assertNotNull(artifact);
        assertEquals(artifact.getName(),aDto.name());
        assertEquals(artifact.getImageURL(), aDto.imageURL());
        verify(artifactRepository, times(1)).save(existingArtifact);
    }

    @Test
    void testUpdateArtifact_failed(){
        //mocks
        ArtifactDto aDto = new ArtifactDto(null,"Deluminator-update","A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update","ImageUrl-update",null);
        when(artifactRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());
        Throwable thrown = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.updateArtifact(aDto,"1250808601744904198");
        });

        assertInstanceOf(ArtifactNotFoundException.class, thrown);
        assertEquals("Could not find artifact with the id:1250808601744904198", thrown.getMessage());

    }

    @Test
    void testDeleteArtifactById_success(){
        //Mock data
        String artifactId = "1895942083416559616";
        Artifact a = new Artifact();
        a.setId("1895942083416559616");
        a.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
        a.setName("Remembrall");
        a.setImageURL("ImageUrl");
        //call the repo method
        when(artifactRepository.findById(artifactId)).thenReturn(Optional.of(a));

        //call the service method
        artifactService.deleteArtifactById(artifactId);

        //asserts and equals
        verify(artifactRepository, times(1)).deleteById(artifactId);
    }

    @Test
    void testDeleteArtifactById_NotFound(){
        String artifactId = "1895942083416559618";

        when(artifactRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());
        Throwable thrown = catchThrowable(()->{
             artifactService.deleteArtifactById(artifactId);
        });

        assertInstanceOf(ArtifactNotFoundException.class, thrown);
        assertEquals("Could not find artifact with the id:"+artifactId, thrown.getMessage());


    }

}