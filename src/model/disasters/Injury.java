package model.disasters;

import model.people.Citizen;

public class Injury extends Disaster{
	public Injury(int cycle, Citizen target) {
		super(cycle,target);
	}
	public void strike(){
		super.cycleStep();
		((Citizen)getTarget()).setBloodLoss(30);
	}
	public void cycleStep(){
		super.cycleStep();
		((Citizen)getTarget()).setBloodLoss(((Citizen)getTarget()).getBloodLoss()+30);
	}

}
