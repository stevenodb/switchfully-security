package com.cegeka.switchfully.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = ArmyResource.ARMY_RESOURCE_PATH)
public class ArmyResource {

    public static final String ARMY_RESOURCE_PATH = "/armies";

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE ,path = "/{country}")
    @PreAuthorize("hasAnyRole('ROLE_PRIVATE','ROLE_GENERAL')")
    public ArmyInfoDto getDeployedArmyInfo(@PathVariable(value = "country") String country){
        return ArmyInfoDto.armyInfoDto()
                .withCountry(country)
                .withNumberOfTroops(2000)
                .withxCoordinateOfBase(15)
                .withyCoordinateOfBase(20);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('CIVILIAN')")
    public void joinArmy(){
        //TODO
    }

    @RequestMapping(method = RequestMethod.POST, path = "/promote/{name}")
    @PreAuthorize("hasRole('ROLE_HUMAN_RELATIONS')")
    public void promotePrivate(@PathVariable(value = "name") String name){
        //TODO
    }

    @RequestMapping(method = RequestMethod.POST, path = "/discharge/{name}")
    @PreAuthorize("hasRole('ROLE_HUMAN_RELATIONS')")
    public void dischargeSoldier(@PathVariable(value = "name") String name){
        //TODO
    }

    @RequestMapping(method = RequestMethod.GET, path = "/nuke")
    //    @PreAuthorize("hasRole('ROLE_GENERAL')")
    public String launchNukes(){
        return "The world ends. Not with a bang but a whimper";
    }

    @RequestMapping(method = RequestMethod.GET, path= "/tanks")
    public String getTanksInfo() {
        return "Tanks rule!";
    }

    @RequestMapping(method = RequestMethod.POST, path= "/tanks")
    public String addTanks() {
        return "Tanks rule!";
    }

    @RequestMapping(method = RequestMethod.DELETE, path= "/tanks")
    public String blowUpTanksAndEnjoyTheFireworks() {
        return "Tanks rule!";
    }

    @RequestMapping(method = RequestMethod.PUT, path= "/tanks")
    public String addNewTanks() {
        return "Tanks rule!";
    }

}
