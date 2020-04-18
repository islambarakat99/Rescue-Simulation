package model.units;

import model.events.WorldListener;
import simulation.Address;

abstract public class FireUnit extends Unit{
	
	public FireUnit(String id, Address location, int stepsPerCycle,WorldListener wl){
		super( id, location ,stepsPerCycle,wl );	
	}
	
}
