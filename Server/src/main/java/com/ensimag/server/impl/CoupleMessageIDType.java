package com.ensimag.server.impl;

import com.ensimag.services.message.EnumMessageType;

public class CoupleMessageIDType {

	private long id;
	private EnumMessageType type;

	public CoupleMessageIDType(long id, EnumMessageType type) {
		this.id = id;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public EnumMessageType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "(id: " + this.id + ", type: " + this.type + ")";
	}

	@Override
	public boolean equals(Object o) {
		CoupleMessageIDType couple = (CoupleMessageIDType) o;
		if (this.id == couple.getId() && this.type == couple.getType()) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result += prime*result + (int) (id);
		result += prime*result + type.hashCode();
		return result;
	}
}
