package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class GasControlUnit extends FireUnit{
	
	public GasControlUnit(String id, Address location, int stepsPerCycle,WorldListener wl){
		super(id,location,stepsPerCycle,wl);
	}
	public void treat(){
		super.treat();
		((ResidentialBuilding)getTarget()).setGasLevel(((ResidentialBuilding)getTarget()).getGasLevel()-10);
		if(((ResidentialBuilding)getTarget()).getGasLevel()==0) {
			jobsDone();
		}	
	}
}
