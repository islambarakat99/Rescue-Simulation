package simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;

import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;

public class Simulator implements WorldListener {

	private int currentCycle;
	private ArrayList<ResidentialBuilding> buildings;
	private ArrayList<Citizen> citizens;
	private ArrayList<Unit> emergencyUnits;
	private ArrayList<Disaster> plannedDisasters;
	private ArrayList<Disaster> executedDisasters;
	private Address[][] world;
	private SOSListener emergencyService;

	public Simulator(SOSListener emergencyService) throws Exception {
		this.currentCycle = 0;
		this.emergencyService = emergencyService;
		buildings = new ArrayList<ResidentialBuilding>();
		citizens = new ArrayList<Citizen>();
		emergencyUnits = new ArrayList<Unit>();
		plannedDisasters = new ArrayList<Disaster>();
		executedDisasters = new ArrayList<Disaster>();
		world = new Address[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				world[i][j] = new Address(i, j);
			}
		}
		loadCitizens("citizens.csv");
		loadBuildings("buildings.csv");
		loadUnits("units.csv");
		loadDisasters("disasters.csv");

	}

	private void loadUnits(String filePath) throws IOException {
		String currentLine = "";
		FileReader fileReader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine()) != null) {
			String[] S = currentLine.split(",");
			String x = S[0];
			switch (x) {
			case "AMB":
				Ambulance y = new Ambulance(S[1], world[0][0], Integer.parseInt(S[2]), this);
				y.setWorldListener(this);
				assignAddress(y, 0, 0);
				emergencyUnits.add(y);
				break;
			case "DCU":
				DiseaseControlUnit y1 = new DiseaseControlUnit(S[1], world[0][0], Integer.parseInt(S[2]), this);
				y1.setWorldListener(this);
				assignAddress(y1, 0, 0);
				emergencyUnits.add(y1);
				break;
			case "EVC":
				Evacuator y2 = new Evacuator(S[1], world[0][0], Integer.parseInt(S[2]), this, Integer.parseInt(S[3]));
				y2.setWorldListener(this);
				assignAddress(y2, 0, 0);
				emergencyUnits.add(y2);
				break;
			case "FTK":
				FireTruck y3 = new FireTruck(S[1], world[0][0], Integer.parseInt(S[2]), this);
				y3.setWorldListener(this);
				assignAddress(y3, 0, 0);
				emergencyUnits.add(y3);
				break;
			case "GCU":
				GasControlUnit y4 = new GasControlUnit(S[1], world[0][0], Integer.parseInt(S[2]), this);
				y4.setWorldListener(this);
				assignAddress(y4, 0, 0);
				emergencyUnits.add(y4);
				break;
			}
		}
	}

	private void loadBuildings(String filePath) throws IOException {
		String currentLine = "";
		FileReader fileReader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine()) != null) {
			String[] s = currentLine.split(",");
			int xx = Integer.parseInt(s[0]);
			int xy = Integer.parseInt(s[1]);
			ResidentialBuilding x = new ResidentialBuilding(world[xx][xy]);
			x.setEmergencyService(this.emergencyService);
			buildings.add(x);
			for (Citizen i : citizens) {
				if (i.getLocation().getX() == Integer.parseInt(s[0])
						&& i.getLocation().getY() == Integer.parseInt(s[1]))
					x.getOccupants().add(i);
			}
		}
	}

	private void loadCitizens(String filePath) throws IOException {
		String currentLine = "";
		FileReader fileReader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine()) != null) {
			String[] s = currentLine.split(",");
			int x1 = Integer.parseInt(s[0]);
			int y1 = Integer.parseInt(s[1]);
			Citizen x = new Citizen(world[x1][y1], s[2], s[3], Integer.parseInt(s[4]), this);
			assignAddress(x, x1, y1);
			x.setEmergencyService(this.emergencyService);
			citizens.add(x);
		}
	}

	private void loadDisasters(String filePath) throws IOException {
		String currentLine = "";
		FileReader fileReader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine()) != null) {
			String[] s = currentLine.split(",");
			String x = s[1];

			Citizen c = null;
			ResidentialBuilding r = null;

			switch (x) {
			case "INJ":
				for (Citizen y : citizens) {
					if (y.getNationalID().equals(s[2])) {
						c = y;
						break;
					}
				}
				plannedDisasters.add(new Injury(Integer.parseInt(s[0]), c));
				break;
			case "INF":
				for (Citizen y : citizens) {
					if (y.getNationalID().equals(s[2])) {
						c = y;
						break;
					}
				}
				plannedDisasters.add(new Infection(Integer.parseInt(s[0]), c));
				break;
			case "FIR":
				for (ResidentialBuilding z : buildings) {
					if (z.getLocation().getX() == Integer.parseInt(s[2])
							&& z.getLocation().getY() == Integer.parseInt(s[3])) {
						r = z;
						break;
					}
				}
				plannedDisasters.add(new Fire(Integer.parseInt(s[0]), r));
				break;
			case "GLK":
				for (ResidentialBuilding z : buildings) {
					if (z.getLocation().getX() == Integer.parseInt(s[2])
							&& z.getLocation().getY() == Integer.parseInt(s[3])) {
						r = z;
						break;
					}
				}

				plannedDisasters.add(new GasLeak(Integer.parseInt(s[0]), r));
			}
		}
	}

	public boolean checkGameOver() {
		for (Disaster x : executedDisasters) {
				if (x.isActive()) {
				if (x.getTarget() instanceof Citizen && ((Citizen) x.getTarget()).getState() != CitizenState.DECEASED) {
					return false;
				} else {
					if (x.getTarget() instanceof ResidentialBuilding
							&& ((ResidentialBuilding) x.getTarget()).getStructuralIntegrity() > 0) {
						return false;
					}
				}
			}
		}
		for (Unit x : emergencyUnits) {
			if (x.getState() != UnitState.IDLE) {
				return false;
			}
		}

		if (plannedDisasters.size() != 0)
			return false;
		return true;
	}

	public int calculateCasualties() {
		int n = 0;
		for (Citizen x : citizens) {
			if (x.getState() == CitizenState.DECEASED)
				n++;
		}
		return n;
	}

	public void nextCycle() {
		this.currentCycle++;
		for (int i = plannedDisasters.size() - 1; i >= 0; i--) {
			if (plannedDisasters.get(i).getStartCycle() == currentCycle) {
				Disaster x = plannedDisasters.remove(i);
				if (x instanceof Fire &&  x.getTarget().getDisaster() instanceof GasLeak) {
					if (((ResidentialBuilding) x.getTarget()).getGasLevel() == 0) {
						executedDisasters.add(x);
						x.strike();
					} else {
						if (((ResidentialBuilding) x.getTarget()).getGasLevel() > 0 && ((ResidentialBuilding) x.getTarget()).getGasLevel() < 70) {
							Collapse c = new Collapse(currentCycle, ((ResidentialBuilding) x.getTarget()));
							for (int j = executedDisasters.size() - 1; j >= 0; j--) {
								if (((ResidentialBuilding) executedDisasters.get(j)
										.getTarget()) == ((ResidentialBuilding) x.getTarget())) {
									executedDisasters.remove(j);
								}
							}
							((ResidentialBuilding) x.getTarget()).setFireDamage(0);
							executedDisasters.add(x);executedDisasters.add(c);
							c.strike();
						} else {
							if (((ResidentialBuilding) x.getTarget()).getGasLevel() >= 70) {
								((ResidentialBuilding) x.getTarget()).setStructuralIntegrity(0);
								executedDisasters.add(x);
								x.strike();
							}
						}
					}
				} else {
					if (x instanceof GasLeak && x.getTarget().getDisaster() instanceof Fire) {
						Collapse c = new Collapse(currentCycle, ((ResidentialBuilding) x.getTarget()));
						for (int j = executedDisasters.size() - 1; j >= 0; j--) {
							if (((ResidentialBuilding) executedDisasters.get(j).getTarget()) == ((ResidentialBuilding) x
									.getTarget())) {
								executedDisasters.remove(j);
							}
						}
						((ResidentialBuilding) x.getTarget()).setFireDamage(0);
						executedDisasters.add(x);executedDisasters.add(c);
						c.strike();
					} else {
						executedDisasters.add(x);
						x.strike();
					}
				}
			}
		}
		for (ResidentialBuilding i : buildings) {
			if (i.getFireDamage() == 100) {
				Collapse c = new Collapse(currentCycle, i);
				for (int j = executedDisasters.size() - 1; j >= 0; j--) {
					if (((ResidentialBuilding) executedDisasters.get(j).getTarget()) == i) {
						executedDisasters.remove(j);
					}
				}
				i.setFireDamage(0);
				executedDisasters.add(c);
				c.strike();
			}
		}

		for (Unit i : emergencyUnits) {
			i.cycleStep();
		}
		for (Disaster i : executedDisasters) {
			if (i.isActive() && i.getStartCycle()<this.currentCycle) {
				i.cycleStep();
			}
		}
		for (ResidentialBuilding i : buildings) {
			i.cycleStep();
		}
		for (Citizen i : citizens) {
			i.cycleStep();
		}
	}

	public ArrayList<Unit> getEmergencyUnits() {
		return emergencyUnits;
	}

	public void assignAddress(Simulatable sim, int x, int y) {
		if (sim instanceof Citizen) {
			((Citizen) sim).setLocation(world[x][y]);
		} else {
			if(sim instanceof Unit) {
				((Unit) sim).setLocation(world[x][y]);
			}
		}
	}

	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}
}
