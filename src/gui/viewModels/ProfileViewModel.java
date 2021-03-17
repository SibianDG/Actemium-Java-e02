package gui.viewModels;

import domain.facades.UserFacade;
import exceptions.InformationRequiredException;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProfileViewModel extends ViewModel {

    private final UserFacade userFacade;

    public ProfileViewModel(UserFacade userFacade) {
        super();
        this.userFacade = userFacade;
    }

    public Map<String, Map<Boolean, String>> getDetailsSignedInUser(){
        Map<String, Map<Boolean, String>> detailsMap = new LinkedHashMap<>();
        detailsMap.put("Employee nr:", Collections.singletonMap(true, userFacade.giveUserEmployeeId()));
        detailsMap.put("Username:", Collections.singletonMap(true, userFacade.giveUserUsername()));
        detailsMap.put("Password:", Collections.singletonMap(false, "*".repeat(userFacade.giveUserPassword().length())));
        detailsMap.put("Firstname:", Collections.singletonMap(false, userFacade.giveUserFirstName()));
        detailsMap.put("Lastname:", Collections.singletonMap(false, userFacade.giveUserLastName()));
        detailsMap.put("Address:", Collections.singletonMap(false, userFacade.giveUserAddress()));
        detailsMap.put("Phone number:", Collections.singletonMap(false, userFacade.giveUserPhoneNumber()));
        detailsMap.put("Email:", Collections.singletonMap(false, userFacade.giveUserEmailAddress()));
        detailsMap.put("Company Seniority:", Collections.singletonMap(true, userFacade.giveUserSeniority()));
        detailsMap.put("Role:", Collections.singletonMap(true, userFacade.giveUserRole().toLowerCase()));
        detailsMap.put("Status:", Collections.singletonMap(true, userFacade.giveUserStatus().toLowerCase()));
        return detailsMap;
    }

    public void modifyProfile(String username, String password, String firstName, String lastName, String address, String phoneNumber, String emailAddress) throws InformationRequiredException {
        userFacade.modifyProfileOfEmployee(username, password, firstName, lastName, address, phoneNumber, emailAddress);
    }


    @Override
    public void delete() throws InformationRequiredException {

    }
}
