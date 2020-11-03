package com.bpic.common.utils.id;


public class Idfactory {

	private static Sequence sequence;
	
	static {
		Long workerId = 0L;
		
		Long dataCenterId = workerId/32;
		Long realWorkerId = workerId%32;
		sequence = new Sequence(realWorkerId,dataCenterId);
	}
	public static Long generate() {
		return sequence.nextId();
	}
}
