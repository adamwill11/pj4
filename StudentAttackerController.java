package edu.ufl.cise.cs1.controllers;

import game.controllers.AttackerController;
import game.models.Attacker;
import game.models.Defender;
import game.models.Game;
import game.models.Node;

import java.util.List;

public final class StudentAttackerController implements AttackerController
{
	public static Node betterGetTarget(List<Node> node, Game game) {
		if (node.size() == 0) {
			return null;
		}
		int minDistance = Integer.MAX_VALUE;
		int minIndex = 0;
		for (int i = 0; i < node.size(); i++) {
			int currentDistance = node.get(i).getPathDistance(game.getAttacker().getLocation());
			if (currentDistance < minDistance) {
				minDistance = node.get(i).getPathDistance(game.getAttacker().getLocation());
				minIndex = i;
			}
		}
		return node.get(minIndex);
	}

	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int update(Game game,long timeDue)
	{
		int action = -1;

		Attacker attacker = game.getAttacker();


		// go for closest pill
		Node closestPill = betterGetTarget(game.getPillList(), game);
		Node closestPowerPill = betterGetTarget(game.getPowerPillList(), game);


		//NEW COMMENT
		//runs from defenders if they come within a certain distance of attacker
		for (int i = 0; i < game.getDefenders().size(); i++) {
			if (attacker.getLocation().getPathDistance(game.getDefenders().get(i).getLocation()) != -1 &&
					attacker.getLocation().getPathDistance(game.getDefenders().get(i).getLocation()) < 6 &&
					!game.getDefenders().get(i).isVulnerable()) {
				action = attacker.getNextDir(game.getDefenders().get(i).getLocation(), false);
				return action;
			}
			else if (attacker.getLocation().getPathDistance(game.getDefenders().get(i).getLocation()) != -1 &&
					game.getDefenders().get(i).isVulnerable()) {
				action = attacker.getNextDir(game.getDefenders().get(i).getLocation(), true);
				return action;
			}
		}

		if (closestPowerPill != null && attacker.getLocation().getPathDistance(closestPowerPill) < 5) {
			action = attacker.getNextDir(closestPowerPill, false);
		}
		else if(closestPowerPill != null && attacker.getLocation().getPathDistance(closestPowerPill) == 5) {
			action = attacker.getNextDir(closestPowerPill, true);
		}
		else if (closestPill != null) {
			action = attacker.getNextDir(closestPill, true);
		}
		
		return action;
	}
}