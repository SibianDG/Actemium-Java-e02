package domain.enums;

import languages.LanguageResource;

/**
 * The enum Required element.
 */
public enum RequiredElement {
    /**
     * The Username required.
     */
    UsernameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("username_invalid");
        }
    }
    ,
    /**
     * The Lastname required.
     */
    LastnameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("lastname_invalid");
        }
    }
    ,
    /**
     * The Firstname required.
     */
    FirstnameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("firstname_invalid");
        }
    }

    ,
    /**
     * The Address required.
     */
    AddressRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("address_invalid");
        }
    }
    ,
    /**
     * The Email required.
     */
    EmailRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("email_invalid");
        }
    }
    ,
    /**
     * The Phone required.
     */
    PhoneRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("phonenumber_invalid");
        }
    }
    ,
    /**
     * The Password required.
     */
    PasswordRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("password_invalid");
        }
    }

    ,
    /**
     * The Company name required.
     */
    CompanyNameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("companyName_invalid");
        }
    }
    ,
    /**
     * The Company country required.
     */
    CompanyCountryRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("country_invalid");
        }
    }
    ,
    /**
     * The Company cirty required.
     */
    CompanyCirtyRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("city_invalid");
        }
    }
    ,
    /**
     * The Company address required.
     */
    CompanyAddressRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("address_invalid");
        }
    }
    ,
    /**
     * The Company phone required.
     */
    CompanyPhoneRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("phonenumber_invalid");
        }
    }

    ,
    /**
     * The Ticket priority required.
     */
    TicketPriorityRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketPriority_invalid");
        }
    }
    ,
    /**
     * The Ticket type required.
     */
    TicketTypeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketType_invalid");
        }
    }
    ,
    /**
     * The Ticket title required.
     */
    TicketTitleRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketTitle_invalid");
        }
    }
    ,
    /**
     * The Ticket customer id required.
     */
    TicketCustomerIDRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketCustomerID_invalid");
        }
    }
    ,
    /**
     * The Ticket description required.
     */
    TicketDescriptionRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("description_invalid");
        }
    }

    ,
    /**
     * The Contract type name required.
     */
    ContractTypeNameRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("contractTypeName_invalid");
        }
    }
    ,
    /**
     * The Contract type status required.
     */
    ContractTypeStatusRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("contractTypeTimestamp_invalid");
        }
    }
    ,
    /**
     * The Contract type required.
     */
    ContractTypeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("contractType_invalid");
        }
    }
    ,
    /**
     * The Company required.
     */
    CompanyRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("company_invalid");
        }
    }
    ,
    /**
     * The Contract status required.
     */
    ContractStatusRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("contractStatus_invalid");
        }
    }
    ,
    /**
     * The Contract start date required.
     */
    ContractStartDateRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("startDate_invalid");
        }
    }
    ,
    /**
     * The Contract end date required 1.
     */
    ContractEndDateRequired1 {
        @Override
        public String toString() {
            return LanguageResource.getString("endDate_invalid1");
        }
    }
    ,
    /**
     * The Contract end date required 2.
     */
    ContractEndDateRequired2 {
        @Override
        public String toString() {
            return LanguageResource.getString("endDate_invalid2");
        }
    }
    ,
    /**
     * The Contract type way required.
     */
    ContractTypeWayRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("ticketCreation_invalid");
        }
    }
    ,
    /**
     * The Contract type max handling time required.
     */
    ContractTypeMaxHandlingTimeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("maxHandlingTime_invalid");
        }
    }

    ,
    /**
     * The Contract type min trough put time required.
     */
    ContractTypeMinTroughPutTimeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("minThroughputTime_invalid");
        }
    }
    ,
    /**
     * The Contract type price required.
     */
    ContractTypePriceRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("price_invalid");
        }
    }
    ,
    /**
     * The Employee role required.
     */
    EmployeeRoleRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("employeeRoleRequired");
        }
    }
    ,
    /**
     * The Registration date required.
     */
    RegistrationDateRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("registrationDateRequired");
        }
    }    
    ,
    /**
     * The Kb item title required.
     */
    KbItemTitleRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("kbItemTitle_invalid");
        }
    }
    ,
    /**
     * The Kb item type required.
     */
    KbItemTypeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("kbItemType_invalid");
        }
    }
    ,
    /**
     * The Kb item keywords required.
     */
    KbItemKeywordsRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("kbItemKeywords_invalid");
        }
    }
    ,
    /**
     * The Kb item text required.
     */
    KbItemTextRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("kbItemText_invalid");
        }
    }
    ,
    /**
     * The User required.
     */
    UserRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("userRequired");
        }
    }
    ,
    /**
     * The Date timeof change required.
     */
    DateTimeofChangeRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("dateTimeofChangeRequired");
        }
    }
    ,
    /**
     * The Change description required.
     */
    ChangeDescriptionRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("changeDescriptionRequired");
        }
    }
    ,
    /**
     * The Date time of comment required.
     */
    DateTimeOfCommentRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("dateTimeOfCommentRequired");
        }
    }
    ,
    /**
     * The Comment text required.
     */
    CommentTextRequired {
        @Override
        public String toString() {
            return LanguageResource.getString("commentTextRequired");
        }
    }
}
