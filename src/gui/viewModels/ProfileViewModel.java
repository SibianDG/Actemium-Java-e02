package gui.viewModels;

import domain.facades.UserFacade;
import exceptions.InformationRequiredException;
import languages.LanguageResource;

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
        detailsMap.put(String.format("%s:", LanguageResource.getString("employee_nr")), Collections.singletonMap(true, userFacade.giveUserEmployeeId()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("username")), Collections.singletonMap(true, userFacade.giveUserUsername()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("password")), Collections.singletonMap(false, "*".repeat(userFacade.giveUserPassword().length())));
        detailsMap.put(String.format("%s:", LanguageResource.getString("firstname")), Collections.singletonMap(false, userFacade.giveUserFirstName()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("lastname")), Collections.singletonMap(false, userFacade.giveUserLastName()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("address")), Collections.singletonMap(false, userFacade.giveUserAddress()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("phone_number")), Collections.singletonMap(false, userFacade.giveUserPhoneNumber()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("email")), Collections.singletonMap(false, userFacade.giveUserEmailAddress()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("company_seniority")), Collections.singletonMap(true, userFacade.giveUserSeniority()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("role")), Collections.singletonMap(true, userFacade.giveUserRole().toLowerCase()));
        detailsMap.put(String.format("%s:", LanguageResource.getString("status")), Collections.singletonMap(true, userFacade.giveUserStatus().toLowerCase()));
        return detailsMap;
    }

    public void modifyProfile(String username, String password, String firstName, String lastName, String address, String phoneNumber, String emailAddress) throws InformationRequiredException {
        userFacade.modifyProfileOfEmployee(username, password, firstName, lastName, address, phoneNumber, emailAddress);
        fireInvalidationEvent();
    }

    @Override
    public void delete() throws InformationRequiredException {

    }
}
