package com.example.hogwarts_artifacts_online.wizard;

import com.example.hogwarts_artifacts_online.artifact.Artifact;
import com.example.hogwarts_artifacts_online.artifact.ArtifactNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetWizardById_Success() {
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setName("Invisibility Cloak");
        a.setImageURL("ImageUrl");
        a.setOwner(w);
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904194");
        a1.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a1.setName("The Marauder's Map");
        a1.setImageURL("ImageUrl");
        a1.setOwner(w);
        //Mock the wizard data
        List<Artifact> artifactList = List.of(a,a1);
        w.setArtifacts(artifactList);

        //call the repo
        when(wizardRepository.findById(2)).thenReturn(Optional.of(w));

        //call the service method
        Wizard foundWizard = wizardService.getWizardById(2);

        //asserts and equals
        assertEquals(foundWizard.getId(), w.getId());
        assertEquals(foundWizard.getName(), w.getName());
        assertEquals(foundWizard.getNumofArtifacts(), w.getNumofArtifacts());

        verify(wizardRepository, times(1)).findById(2);
    }

    @Test
    void testGetWizardById_NotFound(){
        //throw error
        when(wizardRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(()->{
            Wizard returnedWizard = wizardService.getWizardById(200);
        });

        assertInstanceOf(WizardNotFoundException.class, thrown);
        assertEquals("Could not find wizard with Id :200", thrown.getMessage());
    }
}