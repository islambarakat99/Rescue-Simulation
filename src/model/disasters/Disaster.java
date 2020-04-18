package model.disasters;

import java.time.Instant;

import javax.management.InstanceNotFoundException;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;

import org.junit.experimental.categories.Categories.IncludeCategory;

import simulation.Rescuable;
import simulation.Simulatable;

abstract public class Disaster implements Simulatable {
	private int startCycle;
	private Rescuable target;
	private boolean active;

	public Disaster(int startCycle , Rescuable target){
	this.startCycle=startCycle;
	this.target=target;
	active=false;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getStartCycle() {
		return startCycle;
	}

	public Rescuable getTarget() {
		return target;
	}
	public void strike(){
		active=true;this.target.struckBy(this);
	}
	public void cycleStep(){
		
	}
}


