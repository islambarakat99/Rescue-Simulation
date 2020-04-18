package model.units;

import java.util.ArrayList;

import model.events.WorldListener;
import model.people.Citizen;
import simulation.Address;

abstract public class PoliceUnit extends Unit{
	private ArrayList<Citizen> passengers;
	private int maxCapacity;
	private int distanceToBase;
	
	public PoliceUnit(String id, Address location, int stepsPerCycle,WorldListener wl, int maxCapacity) {
		super(id,location,stepsPerCycle,wl);
		this.maxCapacity=maxCapacity;
		passengers = new ArrayList<Citizen>();
	}
	
	public int getDistanceToBase() {
		return distanceToBase;
	}
	public void setDistanceToBase(int distanceToBase) {
		this.distanceToBase = distanceToBase;
	}
	public int getMaxCapacity() {
		return maxCapacity;
	}

	public ArrayList<Citizen> getPassengers() {
		return passengers;
	}
	public void treat(){
		super.treat();
	}

}
