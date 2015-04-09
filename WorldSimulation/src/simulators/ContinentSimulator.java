package simulators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import datastructures.Continent;
import datastructures.Coordinate;
import world.World;

public class ContinentSimulator {
	
	//---
	private class Expander {
		
		Set<Coordinate> mCandidates;
		Coordinate mCenter;
		
		public Expander(Coordinate center) {
			this.mCandidates = new HashSet<Coordinate>();
			this.mCenter = center;
		}
		
		public void add(Coordinate pos) {
			if (!mCandidates.contains(pos)) mCandidates.add(pos);
		}
		
		public Coordinate expand() {
			Map<Coordinate, Double> probs = new HashMap<Coordinate, Double>();
			double sum = 0.0;
			double maxChance = 0.0;
			for (Coordinate pos : mCandidates) {
				double chance = 1.0 / pos.distanceTo(mCenter);
				if (chance > maxChance) maxChance = chance;
				sum += chance;
				probs.put(pos, chance);
			}
			for (Coordinate pos : mCandidates) {
				probs.put(pos, probs.get(pos) / sum);
			}
			double chanceSum = 0.0;
			double roll = Math.random();
			for (Map.Entry<Coordinate, Double> entry : probs.entrySet()) {
				chanceSum += entry.getValue();
				if (roll < chanceSum) {
					Coordinate pos = entry.getKey();
					mCandidates.remove(pos);
					return pos;
				}
			}
			return null;
		}
	}
	
	//---Object Data
	World mWorld;
	Continent mContinent;
	static final int MAX_SIZE = 20;
	
	//---Constructors
	public ContinentSimulator(World world) {
		this.mWorld = world;
		//buildContinent(4, 4);
	}
	
	//---Methods
	/*
	private void buildContinent(int x, int y) {
		// Get world size and ensure continent starts in bounds
		int w = mWorld.getWidth();
		int h = mWorld.getHeight();
		if (x < 0 || y < 0 || x >= w || y >= h) return;
		
		// Start continent at x,y; cancel operation and return on failure
		mContinent = new Continent();
		if (!mContinent.addSquare(x, y)) { mContinent = null; return; }
		// Set elevation of initial square; cancel operation and return on failure
		World.Square sq = mWorld.getSquare(x, y);
		if (sq == null) { mContinent = null; return; }
		sq.setElevation(1.0);
		// 
		Expander expansion = new Expander(new Coordinate(x, y));
		expansion.add(new Coordinate(x+1, y));
		expansion.add(new Coordinate(x-1, y));
		expansion.add(new Coordinate(x, y+1));
		expansion.add(new Coordinate(x, y-1));
		while (mContinent.getSize() < MAX_SIZE) {
			Coordinate newPos = expansion.expand();
			if (newPos == null) continue;
			if (!mContinent.addSquare(newPos.x, newPos.y)) { mContinent = null; return; }
			sq = mWorld.getSquare(newPos.x, newPos.y);
			if (sq == null) { mContinent = null; return; }
			sq.setElevation(1.0);
			if (!mContinent.contains(newPos.x+1, newPos.y)) expansion.add(new Coordinate(newPos.x+1, newPos.y));
			if (!mContinent.contains(newPos.x-1, newPos.y)) expansion.add(new Coordinate(newPos.x-1, newPos.y));
			if (!mContinent.contains(newPos.x, newPos.y+1)) expansion.add(new Coordinate(newPos.x, newPos.y+1));
			if (!mContinent.contains(newPos.x, newPos.y-1)) expansion.add(new Coordinate(newPos.x, newPos.y-1));
		}
		
		mWorld.rebuildDisplay();
	}
	*/
}
