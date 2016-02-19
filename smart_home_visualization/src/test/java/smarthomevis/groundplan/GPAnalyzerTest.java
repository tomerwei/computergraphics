package smarthomevis.groundplan;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.Vector;
import smarthomevis.groundplan.config.GPLine;

public class GPAnalyzerTest
{
	
	@Test
	public void parallelOverlapTest1()
	{
	GPLine line = new GPLine("line1", new Vector(3.0, 0.5, 0.0),
		new Vector(1.0, 0.5, 0.0));
	GPLine other = new GPLine("line2", new Vector(0.5, 1.5, 0.0),
		new Vector(2.0, 1.5, 0.0));
		
	GPAnalyzer analyzer = new GPAnalyzer();
	double overlap = analyzer.calculateParallelOverlapOf(line, other);
	
	assertEquals("overlap test 1 failed", 2.0, overlap, 0.0);
	}
	
	@Test
	public void parallelOverlapTest2()
	{
	GPLine line = new GPLine("line1", new Vector(0.5, 0.5, 0.0),
		new Vector(2.5, 1.0, 0.0));
	GPLine other = new GPLine("line2", new Vector(1.0, -0.5, 0.0),
		new Vector(3.0, 0.0, 0.0));
		
	GPAnalyzer analyzer = new GPAnalyzer();
	double overlap = analyzer.calculateParallelOverlapOf(line, other);
	
	assertEquals("overlap test 2 failed", 3.75, overlap, 0.0);
	}
	
	@Test
	public void parallelOverlapTest3()
	{
	GPLine line = new GPLine("line1", new Vector(1.0, 1.5, 0.0),
		new Vector(1.0, 2.5, 0.0));
	GPLine other = new GPLine("line2", new Vector(2.0, 0.5, 0.0),
		new Vector(2.0, 3.0, 0.0));
		
	GPAnalyzer analyzer = new GPAnalyzer();
	double overlap = analyzer.calculateParallelOverlapOf(line, other);
	
	assertEquals("overlap test 3 failed", 1.0, overlap, 0.0);
	}
	
	@Test
	public void parallelOverlapTest4()
	{
	GPLine line = new GPLine("line1", new Vector(1.0, 1.5, 0.0),
		new Vector(1.0, 2.5, 0.0));
	GPLine other = new GPLine("line2", new Vector(2.0, 0.5, 0.0),
		new Vector(2.0, 0.7, 0.0));
		
	GPAnalyzer analyzer = new GPAnalyzer();
	double overlap = analyzer.calculateParallelOverlapOf(line, other);
	
	assertEquals("overlap test 3 failed", 0.0, overlap, 0.0);
	}
	
	@Test
	public void distanceBetweenTest1()
	{
	GPLine line1 = new GPLine("line1", new Vector(0.0, 0.0, 0.0),
		new Vector(0.0, 2.0, 0.0));
	GPLine line2 = new GPLine("line2", new Vector(2.35, 0.0, 0.0),
		new Vector(2.35, 2.0, 0.0));
		
	GPAnalyzer analyzer = new GPAnalyzer();
	double distance = analyzer.distanceBetween(line1, line2);
	
	assertEquals("distanceBetween test 1 failed", 2.35, distance, 0.0);
	}
	
	@Test
	public void distanceBetweenTest2()
	{
	GPLine line1 = new GPLine("line1", new Vector(-1.32, 0.9, 0.0),
		new Vector(4.32, 0.9, 0.0));
	GPLine line2 = new GPLine("line2", new Vector(-3.4535, -0.1, 0.0),
		new Vector(-0.87, -0.1, 0.0));
		
	GPAnalyzer analyzer = new GPAnalyzer();
	double distance = analyzer.distanceBetween(line1, line2);
	
	assertEquals("distanceBetween test 2 failed", 1.0, distance, 0.0);
	}
	
}
