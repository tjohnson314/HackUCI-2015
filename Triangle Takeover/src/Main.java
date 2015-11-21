
import java.util.*;

public class Main
{

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int numPoints = sc.nextInt();
		int xPoints[] = new int[numPoints];
		int yPoints[] = new int[numPoints];
		
		for(int i = 0; i < numPoints; i++)
		{
			xPoints[i] = sc.nextInt();
			yPoints[i] = sc.nextInt();
		}

		ArrayList<HashSet<Integer>> blueLinks = new ArrayList<HashSet<Integer>>(numPoints);
		ArrayList<HashSet<Integer>> greenLinks = new ArrayList<HashSet<Integer>>(numPoints);
		//Initialize empty list of links for each point
		for(int i = 0; i < numPoints; i++)
		{
			HashSet<Integer> empty = new HashSet<Integer>();
			blueLinks.add(empty);
			
			empty = new HashSet<Integer>();
			greenLinks.add(empty);
		}
		
		int numLinks = sc.nextInt();
		double blueArea = 0, greenArea = 0;
		for(int i = 0; i < numLinks; i++)
		{
			int start = sc.nextInt() - 1;
			int end = sc.nextInt() - 1;
			String color = sc.nextLine().trim();
			if(color.equals("Green"))
			{
				greenLinks.get(start).add(end);
				greenLinks.get(end).add(start);
				
				//See if there are any mutually connected points between start and end
				//that will form a triangle.
				for(int neighbor : greenLinks.get(start))
				{
					if(greenLinks.get(end).contains(neighbor))
					{
						//System.out.println(color + " triangle: " + start + ", " + end + ", " + neighbor);
						int x1 = xPoints[start];
						int x2 = xPoints[end];
						int x3 = xPoints[neighbor];
						int y1 = yPoints[start];
						int y2 = yPoints[end];
						int y3 = yPoints[neighbor];
						
						double area = calcArea(x1, x2, x3, y1, y2, y3);
						greenArea += area;
					}
				}
			}
			else if(color.equals("Blue"))
			{
				blueLinks.get(start).add(end);
				blueLinks.get(end).add(start);
				
				//See if there are any mutually connected points between start and end
				//that will form a triangle.
				for(int neighbor : blueLinks.get(start))
				{
					if(blueLinks.get(end).contains(neighbor))
					{
						//System.out.println(color + " triangle: " + start + ", " + end + ", " + neighbor);
						int x1 = xPoints[start];
						int x2 = xPoints[end];
						int x3 = xPoints[neighbor];
						int y1 = yPoints[start];
						int y2 = yPoints[end];
						int y3 = yPoints[neighbor];
						
						double area = calcArea(x1, x2, x3, y1, y2, y3);
						blueArea += area;
					}
				}
			}
			else
			{
				System.out.println("ERROR! Invalid color " + color);
			}
		}
		sc.close();
		
		if(greenArea > blueArea)
		{
			System.out.println("Green");
			System.out.format("%.4f\n", greenArea);
		}
		else if(blueArea > greenArea)
		{
			System.out.println("Blue");
			System.out.format("%.4f\n", blueArea);
		}
		else
		{
			System.out.println("Tie");
			System.out.format("%.4f\n", greenArea);
		}
	}
	
	public static double calcArea(int x1, int x2, int x3, int y1, int y2, int y3)
	{
		return Math.abs(0.5*((x1 - x3)*(y2 - y1) - (x1 - x2)*(y3 - y1)));
	}
}
