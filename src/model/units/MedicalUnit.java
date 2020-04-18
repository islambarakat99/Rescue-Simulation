package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import simulation.Address;

abstract public class MedicalUnit extends Unit {
	
	private int healingAmount;
	private int treatmentAmount;
	
	public MedicalUnit(String id, Address location, int stepsPerCycle,WorldListener wl){
		super(id,location,stepsPerCycle,wl);
		healingAmount =10;
		treatmentAmount = 10;
	}

	public int getTreatmentAmount() {
		return treatmentAmount;
	}
	public void heal(){
		((Citizen)getTarget()).setHp(((Citizen)getTarget()).getHp()+healingAmount);
		if(((Citizen)getTarget()).getHp()==100) {
			this.jobsDone();
		}
	}
}
