package com.sudhirt.practice.batch.async.mapper;

import com.sudhirt.practice.batch.async.dto.RideInfoItem;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class RideInfoFieldSetMapper implements FieldSetMapper<RideInfoItem> {

	@Override
	public RideInfoItem mapFieldSet(FieldSet fieldSet) throws BindException {
		RideInfoItem item = new RideInfoItem();
		item.setVendorId(fieldSet.readString("vendorId"));
		item.setStartTime(fieldSet.readString("startTime"));
		item.setEndTime(fieldSet.readString("endTime"));
		item.setPassengerCount(fieldSet.readInt("passengerCount", 0));
		item.setTripDistance(readFloat(fieldSet.readString("tripDistance"), 0f));
		item.setRateCode(fieldSet.readInt("rateCode", 0));
		item.setStoreAndFwdFlag(fieldSet.readChar("storeAndFwdFlag"));
		item.setPickupLocationId(fieldSet.readInt("pickupLocationId", 0));
		item.setDropoffLocationId(fieldSet.readInt("dropoffLocationId", 0));
		item.setPaymentType(fieldSet.readInt("paymentType", 0));
		item.setFareAmount(readFloat(fieldSet.readString("fareAmount"), 0f));
		item.setExtra(readFloat(fieldSet.readString("extra"), 0f));
		item.setMtaTax(readFloat(fieldSet.readString("mtaTax"), 0f));
		item.setTipAmount(readFloat(fieldSet.readString("tipAmount"), 0f));
		item.setTollAmount(readFloat(fieldSet.readString("tollAmount"), 0f));
		item.setImprovementSurcharge(readFloat(fieldSet.readString("improvementSurcharge"), 0f));
		item.setTotalAmount(readFloat(fieldSet.readString("totalAmount"), 0f));
		item.setCongestionSurcharge(readFloat(fieldSet.readString("congestionSurcharge"), 0f));
		return item;
	}

	private float readFloat(String value, float defaultValue) {
		if (value == null || value.isEmpty()) {
			return defaultValue;
		}
		else {
			return Float.valueOf(value);
		}
	}

}
