package domain.facades;

import domain.ActemiumKbItem;
import domain.KbItem;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import javafx.collections.ObservableList;

public class KnowledgeBaseFacade implements Facade {

    private Actemium actemium;

    public KnowledgeBaseFacade(Actemium actemium) {
        this.actemium = actemium;
    }

    public void registerKbItem(String title, KbItemType type, String text) throws InformationRequiredException {
		// check to see if signed in user is Support Manager
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
    	ActemiumKbItem kbItem = new ActemiumKbItem.KbItemBuilder()
                .title(title)
                .type(type)
                .text(text)
                .build();
        actemium.registerKbItem(kbItem);
    }

    public void modifyKbItem(ActemiumKbItem kbItem, String title, KbItemType type, String text) throws InformationRequiredException {

        try {
            ActemiumKbItem kbItemClone = kbItem.clone();

            // check to see if signed in user is Support Manager
            actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);

            kbItemClone.setTitle(title);
            kbItemClone.setType(type);
            kbItemClone.setText(text);

            kbItemClone.checkAttributes();
            
            kbItem.setTitle(title);
            kbItem.setType(type);
            kbItem.setText(text);

            actemium.modifyKbItem(kbItem);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    public void delete(ActemiumKbItem kbItem) {
		// check to see if signed in user is Support Manager
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
		// physical delete for kb item?
        actemium.modifyKbItem(contractType);
    }

    public ObservableList<KbItem> giveActemiumKbItems() {
        return actemium.giveActemiumKbItems();
    }

    public KbItem getLastAddedKbItem() {
        return actemium.getLastAddedKbItem();
    }

}
