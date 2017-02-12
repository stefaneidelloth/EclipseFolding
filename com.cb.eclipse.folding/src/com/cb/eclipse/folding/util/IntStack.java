/*
 * Created on Aug 17, 2004
 */
package com.cb.eclipse.folding.util;

/**
 * A simple primitive 'int' value stack (NOT thread safe)
 * @author R.J. Lorimer
 */
public class IntStack {

	private static final int DEFAULT_START_SIZE = 10;
	private static final int DEFAULT_INCREMENT = DEFAULT_START_SIZE;
	
	private int[] values;
	private int cursor;
	private int increment;
	
	
	public IntStack() {
		this(DEFAULT_START_SIZE, DEFAULT_INCREMENT);
	}
	
	public IntStack(int startSize, int increment) {
		this.increment = increment;
		if(startSize < 0) throw new IllegalArgumentException("Starting size must be at least 0");
		if(increment <= 0) throw new IllegalArgumentException("Increment must be a positive integer");
		values = new int[startSize];
	}
	
	public IntStack(int startSize) {
		this(startSize, startSize);
	}
	
	public boolean isEmpty() {
		return (cursor == 0);
	}
	
	public int size() {
		return cursor;
	}
	
	public int pop() {
		if(isEmpty()) throw new ArrayIndexOutOfBoundsException("Can't pop int from empty int stack");
		
		return values[--cursor];
	}
	
	public void push(int newVal) {
		if(values.length == cursor) {
			int[] newArry = new int[values.length + increment];
			for(int i=0; i<values.length;i++) {
				newArry[i] = values[i];
			}
			values = newArry;
		}
		
		values[cursor++] = newVal;
	}
	
}
