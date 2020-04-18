package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public class Ambulance extends MedicalUnit{
	public Ambulance(String id, Address location, int stepsPerCycle,WorldListener wl) {
		super(id,location,stepsPerCycle,wl);
	}
	public static void main(String[]args) {
		
	}
	
	public void treat(){
		super.treat();
		if(((Citizen)getTarget()).getBloodLoss()>0) {
			((Citizen)getTarget()).setBloodLoss(((Citizen)getTarget()).getBloodLoss()-getTreatmentAmount());
			if(((Citizen)getTarget()).getBloodLoss()==0)
				((Citizen)getTarget()).setState(CitizenState.RESCUED);
		}
		else{
			this.heal();
		}
	}
}
