package model.infrastructure;
 
import java.util.ArrayList;
 

import model.disasters.Disaster;
import model.events.SOSListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;
 
public class ResidentialBuilding implements Rescuable, Simulatable {
 
	private Address location;
	private int structuralIntegrity;
	private int fireDamage;
	private int gasLevel;
	private int foundationDamage;
	private ArrayList<Citizen> occupants;
	private Disaster disaster;
	private SOSListener emergencyService;
 
	public ResidentialBuilding(Address location) {
 
		this.location = location;
		this.structuralIntegrity = 100;
		occupants = new ArrayList<Citizen>();
 
	}
 
	public int getStructuralIntegrity() {
		return structuralIntegrity;
	}
 
	public void setStructuralIntegrity(int structuralIntegrity) {
		if(structuralIntegrity<0) {
			this.structuralIntegrity=0;
		}
		else{
			if(structuralIntegrity>100) {
				this.structuralIntegrity=100;
			}
			else {
				this.structuralIntegrity = structuralIntegrity;
			}
		}
		if(this.structuralIntegrity==0) {
			for(Citizen i:this.occupants) {
				i.setHp(0);
				i.setState(CitizenState.DECEASED);
			}
		}
	}
 
	public int getFireDamage() {
		return fireDamage;
	}
 
	public void setFireDamage(int fireDamage) {
		if(fireDamage<0) {
			this.fireDamage=0;
		}
		else {
			if(fireDamage>100) {
				this.fireDamage=100;
			}
			else {
				this.fireDamage = fireDamage;
			}
		}
	}
 
	public int getGasLevel() {
		return gasLevel;
	}
 
	public void setGasLevel(int gasLevel) {
		if(gasLevel<0) {
			this.fireDamage=0;
		}
		else {
			if(gasLevel>100) {
				this.fireDamage=100;
			}
			else {
				this.gasLevel = gasLevel;
			}
		}
		if(this.gasLevel==100) {
			for(Citizen i:this.occupants) {
				i.setHp(0);
				i.setState(CitizenState.DECEASED);
			}
		}
	}
 
	public int getFoundationDamage() {
		return foundationDamage;
	}
 
	public void setFoundationDamage(int foundationDamage) {
		this.foundationDamage = foundationDamage;
		if(this.foundationDamage>=100) {
			this.structuralIntegrity=0;
		}
	}
 
	public Address getLocation() {
		return location;
	}
 
	public ArrayList<Citizen> getOccupants() {
		return occupants;
	}
 
	public Disaster getDisaster() {
		return disaster;
	}
 
	public void struckBy(Disaster d) {
		this.disaster=d;
		this.emergencyService.receiveSOSCall(this)	;
	}
 
	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}
	public void cycleStep() {
		if(foundationDamage>0)
			structuralIntegrity-=((int)Math.random()*6)+5;
		if(fireDamage>0&&fireDamage<30)
			structuralIntegrity-=3;
		else if(fireDamage>=30&&fireDamage<70)
			structuralIntegrity-=5;
		else
			structuralIntegrity-=7;
	}
 
}