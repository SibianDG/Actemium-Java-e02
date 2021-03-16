package domain.enums;

import languages.LanguageResource;

public enum RequiredElement {
    UsernameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("username_invalid");
        }
    }
    , LastnameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("lastname_invalid");
        }
    }
    , FirstnameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("firstname_invalid");
        }
    }

    , AddressRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("address_invalid");
        }
    }
    , EmailRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("email_invalid");
        }
    }
    , PhoneRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("phonenumber_invalid");
        }
    }
    , PasswordRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("password_invalid");
        }
    }

    , CompanyNameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("companyName_invalid");
        }
    }
    , CompanyAddressRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("address_invalid");
        }
    }
    , CompanyPhoneRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("phonenumber_invalid");
        }
    }

    , TicketPriorityRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketPriority_invalid");
        }
    }
    , TicketTypeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketType_invalid");
        }
    }
    , TicketTitleRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketTitle_invalid");
        }
    }
    , TicketCustomerIDRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketCustomerID_invalid");
        }
    }
    , TicketDescriptionRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("description_invalid");
        }
    }

    , ContractTypeNameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("username_invalid");
        }
    }
    , ContractTypeWayRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketCreation_invalid");
        }
    }
    , ContractTypeMaxHandlingTimeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("maxHandlingTime_invalid");
        }
    }

    , ContractTypeLinTroughPutTimeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("minThroughputTime_invalid");
        }
    }
    , ContractTypePriceRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("price_invalid");
        }
    }
}
