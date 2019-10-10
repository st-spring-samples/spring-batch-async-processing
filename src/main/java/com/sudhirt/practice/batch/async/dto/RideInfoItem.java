package com.sudhirt.practice.batch.async.dto;

import lombok.Data;

@Data
public class RideInfoItem {

	private String vendorId;

	private String startTime;

	private String endTime;

	private int passengerCount;

	private float tripDistance;

	private int rateCode;

	private char storeAndFwdFlag;

	private int pickupLocationId;

	private int dropoffLocationId;

	private int paymentType;

	private float fareAmount;

	private float extra;

	private float mtaTax;

	private float tipAmount;

	private float tollAmount;

	private float improvementSurcharge;

	private float totalAmount;

	private float congestionSurcharge;

}