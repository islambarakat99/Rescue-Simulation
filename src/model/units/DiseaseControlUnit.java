package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public class DiseaseControlUnit extends MedicalUnit{

	public DiseaseControlUnit(String id, Address location, int stepsPerCycle,WorldListener wl){
		super(id, location, stepsPerCycle,wl);
	}
	public void treat(){
		super.treat();
		if(((Citizen)getTarget()).getToxicity()>0)
			((Citizen)getTarget()).setToxicity(((Citizen)getTarget()).getToxicity()-getTreatmentAmount());
		if(((Citizen)getTarget()).getToxicity()==0)
			((Citizen)getTarget()).setState(CitizenState.RESCUED);
		else{
			
			this.heal();
		}
	}

}
