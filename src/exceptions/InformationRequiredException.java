package exceptions;

import domain.enums.RequiredElement;

import java.util.Collections;
import java.util.Set;

public class InformationRequiredException extends Exception{
    private static final String MESSAGE = 
    "Further information is required";
    private Set<RequiredElement> informationRequired;
    
    public InformationRequiredException(Set<RequiredElement> itemsRequired){
        super(MESSAGE);
        informationRequired = itemsRequired;
    }
    
    public Set<RequiredElement> getInformationRequired(){
        return Collections.unmodifiableSet(informationRequired); 
    }
}

