package exceptions;

import domain.enums.RequiredElement;
import languages.LanguageResource;

import java.util.Collections;
import java.util.Set;

public class InformationRequiredException extends Exception{
    private static final String MESSAGE = 
    LanguageResource.getString("information_required_exception_message");
    private Set<RequiredElement> informationRequired;
    
    public InformationRequiredException(Set<RequiredElement> itemsRequired){
        super(MESSAGE);
        informationRequired = itemsRequired;
    }
    
    public Set<RequiredElement> getInformationRequired(){
        return Collections.unmodifiableSet(informationRequired); 
    }
}

