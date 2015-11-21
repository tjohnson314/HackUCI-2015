
import java.util.Scanner;
import java.util.LinkedHashMap;

public class Main
{

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int startHeight = sc.nextInt();
		sc.close();
		
		//We make an array that stores the number of layers with:
		//All three pieces
		//Missing one side piece
		//Missing one center piece
		//Missing both side pieces
		//State of the partial top layer (complete, one piece, or two pieces)
		
		//The maximum number of layers is around 3*startHeight,
		//so the total number of possibilities is at most the number
		//of solutions of x1 + x2 + x3 + x4 <= 3*startHeight.

		//A linked hash map stores the order in which we added values, which is useful for debugging.
		LinkedHashMap<JengaState, Boolean> saveWin = new LinkedHashMap<JengaState, Boolean>();
		JengaState startState = new JengaState(startHeight);
		if(findWin(startState, saveWin))
		{
			System.out.println("Win");
		}
		else
		{
			System.out.println("Loss");
		}
		
		//System.out.println(saveWin.size());
		
		/*for(JengaState checkState : saveWin.keySet())
		{
			checkState.printState();
			System.out.println(saveWin.get(checkState));
		}*/
	}
	
	public static boolean findWin(JengaState currState, LinkedHashMap<JengaState, Boolean> saveWin)
	{
		//First check our cache of positions we have already seen.
		if(saveWin.containsKey(currState))
		{
			return saveWin.get(currState);
		}
		//Otherwise, search for a winning move.
		//A move is winning if it leads to a losing position for the other player.
		else
		{
			boolean currWin = false;
			
			//Case 1: We take from an accessible full layer.
			if(currState.fullLayers > 0)
			{
				//Case 1.1: We take from the center.				
				//We copy the current state, and increase the number of moves.
				JengaState newState1 = new JengaState(currState);
				newState1.numMoves++;
				
				//Next we increment the number of layers with the center piece missing,
				//and decrement the number of full layers.
				newState1.missCenter++;
				newState1.fullLayers--;
				
				//We add the block we removed to the top.
				newState1.addToTop();
				currWin |= !findWin(newState1, saveWin);
				
				
				//Case 1.2: We take from one of the sides.
				//We copy the current state, and increase the number of moves.
				JengaState newState2 = new JengaState(currState);
				newState2.numMoves++;
				
				//We increment the number of layers with one side missing,
				//and decrement the number of full layers.
				newState2.missOneSide++;
				newState2.fullLayers--;
				
				//We add the block we removed to the top.
				newState2.addToTop();
				currWin |= !findWin(newState2, saveWin);
			}
			
			//Case 2: We take from a layer that is missing one side piece.
			if(currState.missOneSide > 0)
			{
				//We copy the current state, and increase the number of moves.
				JengaState newState3 = new JengaState(currState);
				newState3.numMoves++;
				
				//We increment the number of layers with two sides missing,
				//and decrement the number of layers with one side missing.
				newState3.missTwoSides++;
				newState3.missOneSide--;
				
				//We add the block we removed to the top.
				newState3.addToTop();
				currWin |= !findWin(newState3, saveWin);
			}
		
			saveWin.put(currState, currWin);
			return currWin;
		}
	}
}

class JengaState
{
	int fullLayers;
	int missOneSide;
	int missCenter;
	int missTwoSides;
	int topLayer;
	int numMoves;
	
	public JengaState(int startHeight)
	{
		//We only count the layers from which we can take blocks.
		//The top layer is off limits.
		fullLayers = startHeight - 1;
		missOneSide = 0;
		missCenter = 0;
		missTwoSides = 0;
		topLayer = 3;
		numMoves = 0;
	}
	
	//Copy constructor
	public JengaState(JengaState oldState)
	{
		this.fullLayers = oldState.fullLayers;
		this.missOneSide = oldState.missOneSide;
		this.missCenter = oldState.missCenter;
		this.missTwoSides = oldState.missTwoSides;
		this.topLayer = oldState.topLayer;
		this.numMoves = oldState.numMoves;
	}
	
	//Place a block back on the top layer.
	public void addToTop()
	{
		if(topLayer == 3)
		{
			topLayer = 1;
		}
		else
		{
			topLayer++;
			//If we finished a new layer, then the layer below it becomes available.
			if(topLayer == 3)
			{
				fullLayers++;
			}
		}
	}
	
	public void printState()
	{
		System.out.println("\n### New Jenga State ###");
		System.out.println("Full layers: " + fullLayers);
		System.out.println("Missing one side: " + missOneSide);
		System.out.println("Missing both sides: " + missTwoSides);
		System.out.println("Missing center: " + missCenter);
		System.out.println("Top layer: " + topLayer);
		System.out.println("Total moves: " + numMoves);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof JengaState))
			return false;
		else
		{
			JengaState otherState = (JengaState) obj;
			return((this.fullLayers == otherState.fullLayers)
				&& (this.missCenter == otherState.missCenter)
				&& (this.missOneSide == otherState.missOneSide)
				&& (this.missTwoSides == otherState.missTwoSides));
		}
	}
	
	@Override
	public int hashCode()
	{
		//We'll only go up to an initial size of 50, so that the total number of
		//layers is at most 150. Then the largest possible value is below 150^4,
		//which still fits in a 32-bit integer.
		int base = 151;
		int result = fullLayers;
		result = result*base + missCenter;
		result = result*base + missOneSide;
		result = result*base + missTwoSides;
		
		return result;
	}
}
