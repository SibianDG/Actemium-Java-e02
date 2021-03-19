package gui;

import languages.LanguageResource;

public enum GUIEnum {
    CUSTOMER {
        @Override
        public String toString() {
            return LanguageResource.getString("customer");
        }
    }
    , EMPLOYEE {
        @Override
        public String toString() {
            return LanguageResource.getString("employee");
        }
    }
    , TICKET {
        @Override
        public String toString() {
            return LanguageResource.getString("ticket");
        }
    }
    , CONTRACT {
        @Override
        public String toString() {
            return LanguageResource.getString("contract");
        }
    }
    , CONTRACTTYPE {
        @Override
        public String toString() {
            return LanguageResource.getString("contracttype");
        }
    }
    , KNOWLEDGEBASE {
        @Override
        public String toString() {
            return LanguageResource.getString("knowledge_base");
        }
    } //Knowledge Base
}
