package bt_team.player;

import behavior_tree_lib.BTNode;
import behavior_tree_lib.Selector;
import behavior_tree_lib.Sequence;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;


public class BTreePlayer extends Thread {
	final PlayerCommander commander;
	
	// ideia para melhorar: percepções mais detalhadas, como no utility_team.Player
	PlayerPerception selfPerc;
	FieldPerception  fieldPerc;
	
	Vector2D homePosition;
	Vector2D goalPosition;
	
	BTNode<BTreePlayer> btree;
	
	
	public BTreePlayer(PlayerCommander player, Vector2D home) {
		commander = player;
		homePosition = home;
		
		btree = buildTree();
	}

	private BTNode<BTreePlayer> buildTree() {
		Selector<BTreePlayer> raiz = new Selector<BTreePlayer>("RAIZ");
		
		Sequence<BTreePlayer> attackTree = new Sequence<BTreePlayer>("Avanca-para-Gol");
		attackTree.add(new ConditionClosestToBall());
		attackTree.add(new ActionAdvanceWithBall());
		attackTree.add(new ActionKickToGoal());

		Sequence<BTreePlayer> deffensiveTree = new Sequence<BTreePlayer>("Rouba-Bola");
		deffensiveTree.add(new ConditionClosestToBall());
		deffensiveTree.add(new ActionGoGetBall());
		
		raiz.add(attackTree);
		raiz.add(deffensiveTree);
		//raiz.add(new ReturnToHome());  //atenção: falta implementar
		
		return raiz;
	}

	@Override
	public void run() {
		System.out.println(">> 1. Waiting initial perceptions...");
		selfPerc  = commander.perceiveSelfBlocking();
		fieldPerc = commander.perceiveFieldBlocking();

		selfPerc  = commander.perceiveSelfBlocking();
		fieldPerc = commander.perceiveFieldBlocking();
		
		if (selfPerc.getFieldSide() == EFieldSide.LEFT) {
			goalPosition = new Vector2D(52.0d, 0);
		} else {
			goalPosition = new Vector2D(-52.0d, 0);
			homePosition.setX(- homePosition.getX()); 
			homePosition.setY(- homePosition.getY());
		}

		System.out.println(">> 2. Moving to initial position...");
		commander.doMoveBlocking(this.homePosition);
		
		System.out.println(">> 3. Now starting...");
		while (commander.isActive()) {
			
			btree.tick(this);
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			updatePerceptions(); //non-blocking
		}
		
		System.out.println(">> 4. Terminated!");
	}

	private void updatePerceptions() {
		PlayerPerception newSelf = commander.perceiveSelf();
		FieldPerception newField = commander.perceiveField();
		
		if (newSelf != null) {
			this.selfPerc = newSelf;
		}
		if (newField != null) {
			this.fieldPerc = newField;
		}
	}
	
	/** Algumas funcoes auxiliares que mais de um tipo de no da arvore pode precisar **/
	
	boolean closeTo(Vector2D pos) {
		return isCloseTo(pos, 1.5);
	}
	
	boolean isCloseTo(Vector2D pos, double minDistance) {
		Vector2D myPos = selfPerc.getPosition();
		return pos.distanceTo(myPos) < minDistance;
	}

	boolean isAlignedTo(Vector2D position) {
		return isAlignedTo(position, 12.0);
	}
	
	boolean isAlignedTo(Vector2D position, double minAngle) {
		if (minAngle < 0) minAngle = -minAngle;
		
		Vector2D myPos = selfPerc.getPosition();
		
		if (position == null || myPos == null) {
			return false;			
		}
		
		double angle = selfPerc.getDirection().angleFrom(position.sub(myPos));
		return angle < minAngle && angle > -minAngle;
	}

}
