package domain.facades;

import domain.ActemiumKbItem;
import domain.KbItem;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import javafx.collections.ObservableList;

/**
 * The type Knowledge base facade.
 */
public class KnowledgeBaseFacade extends Facade {

    /**
     * Instantiates a new Knowledge base facade.
     *
     * @param actemium the actemium
     */
    public KnowledgeBaseFacade(Actemium actemium) {
        super(actemium);
    }

    /**
     * Register kb item.
     *
     * @param title    the title
     * @param type     the type
     * @param keywords the keywords
     * @param text     the text
     * @throws InformationRequiredException the information required exception
     */
    public void registerKbItem(String title, KbItemType type, String keywords, String text) throws InformationRequiredException {
		// check to see if signed in user is Support Manager
		actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);
    	ActemiumKbItem kbItem = new ActemiumKbItem.KbItemBuilder()
                .title(title)
                .type(type)
                .keywords(keywords)
                .text(text)
                .build();
        actemium.registerKbItem(kbItem);
    }

    /**
     * Modify kb item.
     *
     * @param kbItem   the kb item
     * @param title    the title
     * @param type     the type
     * @param keywords the keywords
     * @param text     the text
     * @throws InformationRequiredException the information required exception
     */
    public void modifyKbItem(ActemiumKbItem kbItem, String title, KbItemType type, String keywords, String text) throws InformationRequiredException {

        try {
            ActemiumKbItem kbItemClone = kbItem.clone();

            // check to see if signed in user is Support Manager
            actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);

            kbItemClone.setTitle(title);
            kbItemClone.setType(type);
            kbItemClone.setKeywords(keywords);
            kbItemClone.setText(text);

            kbItemClone.checkAttributes();
            
            kbItem.setTitle(title);
            kbItem.setType(type);
            kbItem.setKeywords(keywords);
            kbItem.setText(text);

            actemium.modifyKbItem(kbItem);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Delete.
     *
     * @param kbItem the kb item
     */
    public void delete(ActemiumKbItem kbItem) {
		// check to see if signed in user is Support Manager
		actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);
		// physical delete for kb item?
        actemium.modifyKbItem(kbItem);
    }

}
