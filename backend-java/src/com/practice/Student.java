package com.practice;

public interface Student {
	
	void work();  //abstract by default
	
	

}

class College implements Student{
	@Override
	public void work() {
		System.out.println("student is in college");
	}
}

