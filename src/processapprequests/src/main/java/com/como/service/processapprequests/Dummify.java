package com.como.service.processapprequests;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class Dummify {
    public JSONObject dummifyPointRedemption(String memberNumber, int amount) {
        Map<String, Object> body = new HashMap<>();
        body.put("MemberNo", memberNumber);
        body.put("PointType", "C21 Points");
        body.put("TransactionDate", "2013-02-28T00:00:00+08:00");
        body.put("Location", "LHQS");
        body.put("Amount", amount);
        body.put("RedemptionCode", "RD001");
        body.put("Description", "Instant Redemption");
        body.put("ReceiptNo", "00234598");

        return new JSONObject(body);
    }
}
