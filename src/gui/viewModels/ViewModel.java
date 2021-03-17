package gui.viewModels;

import exceptions.InformationRequiredException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;

public abstract class ViewModel implements Observable {
	
	private boolean fieldModified = false;

    private ArrayList<InvalidationListener> listeners = new ArrayList<>();

    protected void fireInvalidationEvent() {
        for (InvalidationListener listener : listeners) {
            listener.invalidated(this);
        }
    }
    

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        listeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        listeners.remove(invalidationListener);
    }

	public boolean isFieldModified() {
		return fieldModified;
	}

	public void setFieldModified(boolean fieldModified) {
		this.fieldModified = fieldModified;
	}

    public abstract void delete() throws InformationRequiredException;

}
