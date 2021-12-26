package com.como.service.receiveapprequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Dummify {

    @Autowired
    private JSONService jsonService;

    public JSONObject dummifyCreateMember(String title, String firstName, String lastName, String mobileCountryCode, String mobile, String email, String username) {
        String member = "{\"Profile\":{\"Title\":\"" + title + "\",\"IC\":\"S20160309A\",\"FirstName\":\"" + firstName + "\",\"LastName\":\"" + lastName + "\",\"NickName\":\"" + username + "\",\"DOB\":\"1980-03-15T00:00:00\",\"Mobile\":\"" + mobile + "\",\"MobileCountryCode\":\"" + mobileCountryCode + "\",\"Email\":\"" + email + "\",\"GenderCode\":\"M\",\"NationalityCode\":\"SG\",\"JoinLocation\":\"WEB\",\"DateJoined\":\"2021-03-08T15:58:20.8749009+08:00\",\"Addresses\":[{\"AddressType\":\"HOME\",\"Address1\":\"101PASIRPANJANGROAD\",\"State\":\"SINGAPORE\",\"PostalCode\":\"118520\",\"CountryCode\":\"SG\"}]},\"Interests\":[{\"Code\":\"INTE003\",\"DisplayText\":\"IT/Electronics/Telecommunications\",\"Value\":\"Y\"},{\"Code\":\"INTE004\",\"DisplayText\":\"Movies/Entertainment/Music\",\"Value\":\"Y\"},{\"Code\":\"INTE005\",\"DisplayText\":\"Household&Lifestyle\",\"Value\":\"N\"}],\"ContactPreferences\":[{\"Code\":\"CLUB21_MAIL\",\"DisplayText\":\"\",\"Value\":\"NA\"},{\"Code\":\"CLUB21_EMAIL\",\"DisplayText\":\"\",\"Value\":\"NA\"},{\"Code\":\"CLUB21_SMS\",\"DisplayText\":\"\",\"Value\":\"NA\"}],\"Membership\":{},\"Purchase\":{\"TransactionDate\":\"2016-03-08T11:29:47+08:00\",\"StoreCode\":\"HQ\",\"DeviceType\":\"\",\"DeviceId\":\"\",\"POSId\":\"\",\"OperatorId\":\"\",\"Description\":\"\",\"ReceiptNumber\":\"ORDER123456\",\"PromotionCodes\":[{\"Code\":\"PROMO01\"}],\"Items\":[{\"Currency\":\"SGD\",\"ItemCode\":\"PUR01\",\"Amount\":0}],\"Payments\":[{\"PaymentType\":\"CASH\",\"Currency\":\"SGD\",\"Amount\":0}]}}";

        return jsonService.convertStringToJSONObj(member);
    }

    public JSONObject dummifyItemRedemption() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", "2000-01-23T04:56:07.000+00:00");
        map.put("deviceType", "deviceType");
        map.put("rewardNumber", "rewardNumber");
        map.put("deviceId", "deviceId");
        map.put("memberNumber", "CT9001178M");
        map.put("salePerson", "salePerson");
        map.put("disableCheckStock", false);
        map.put("registrator", "registrator");

        Map<String, Object> payment_1 = new HashMap<>();
        payment_1.put("purchaseTransactionId", "15bc3f1d-43a1-4023-8f1b-f5a49e0afbf8");
        payment_1.put("costValue", 8);

        map.put("payment", payment_1);
        map.put("locationCode", "locationCode");
        map.put("allowHistoryRedemption", false);
        map.put("receiptNumber", "receiptNumber");

        Map<String, Object> dataInput_1 = new HashMap<>();
        dataInput_1.put("Value", "Value");
        dataInput_1.put("Name", "Name");
        List<JSONObject> dataInput = new ArrayList<>();
        dataInput.add(new JSONObject(dataInput_1));

        map.put("dataInput", dataInput);

        return new JSONObject(map);
    }
}
