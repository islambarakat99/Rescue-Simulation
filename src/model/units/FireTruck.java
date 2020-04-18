package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class FireTruck extends FireUnit {
	
	public FireTruck (String id, Address location, int stepsPerCycle,WorldListener wl)
	{
		super(id,location, stepsPerCycle,wl);
	}
	public void treat(){
		super.treat();
		((ResidentialBuilding)getTarget()).setFireDamage(((ResidentialBuilding)getTarget()).getFireDamage()-10);
		if(((ResidentialBuilding)getTarget()).getFireDamage()==0) {
			jobsDone();
		}
	}
}
