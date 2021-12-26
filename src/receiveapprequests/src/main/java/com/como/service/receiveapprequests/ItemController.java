package com.como.service.receiveapprequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService = new ItemService();

    @Autowired
    private JSONService jsonService;

    @Autowired
    private RARListener listener;

    @Autowired
    private Dummify dummify;

    @GetMapping("/items/{item_id}")
    public String getItem(@PathVariable String itemId){
        Item item = itemService.getItem(itemId);
        return item.getItemName();
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/items")
    public String getItemListing(Model model) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "GET");
        jsonObject.put("identifier", "getItemListing");
        jsonObject.put("uri", "/api/vouchertype/search?status=Active&type=THIRDPARTY");

        System.out.println("Forwarding to PAR: " + jsonObject.toString());
        ResponseEntity<String> response = listener.forwardToPAR(jsonObject.toString());
        if (response == null) return "error";

        List<JSONObject> list = jsonService.convertStringToJSONArray(response.getBody());
        itemService.setItemsLocal(list);

        // To display in item.html
        model.addAttribute("listOfItems", "View Available Items");
        model.addAttribute("getAllItems", list);

        return "item";
    }


    @SuppressWarnings("unchecked")
    @RequestMapping("/items/{item_id}/redeem")
    public String redeemItem (Model model, @PathVariable(name="item_id") String item_id) {

        List<JSONObject> listOfItems = null;
        if (itemService.getItemsLocal() != null) {
            listOfItems = itemService.getItemsLocal();
        } else {
            // Query PAR to obtain list of items
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "GET");
            jsonObject.put("identifier", "getItemListing");
            jsonObject.put("uri", "/api/vouchertype/search?status=Active&type=THIRDPARTY");

            System.out.println("Forwarding to PAR: " + jsonObject.toString());
            ResponseEntity<String> response = listener.forwardToPAR(jsonObject.toString());
            if (response == null) return "error";

            // Get list of items
            listOfItems = jsonService.convertStringToJSONArray(response.getBody());
        }

        model.addAttribute("CBV_item_id", "item_id: " + item_id);

        for (JSONObject item : listOfItems) {
            if (item.get("VoucherID").toString().equals(item_id)) {
                model.addAttribute("CBV_item_name", "Item Name:" + item.get("VoucherName"));
                return "confirmItem";
            }
        }
        
        return "error";
    }

    @RequestMapping("/items/{item_id}/redeem/confirm")
    public String confirmRedeemItem(Model model, @PathVariable(name="item_id") String item_id) {

        // List<String> itemsToRedeem = new ArrayList<>();
        // itemsToRedeem.add(item_id);

        JSONObject dataToForward = dummify.dummifyItemRedemption();
        Map<String, Object> requestToForward = new HashMap<>();
        requestToForward.put("uri", "/reward/redeem");
        requestToForward.put("identifier", "redeemItem");
        requestToForward.put("action", "POST");
        requestToForward.put("itemToRedeem", item_id);
        requestToForward.put("memberNumber", "CT9001178M"); // more dummy data
        requestToForward.put("data", dataToForward);

        ResponseEntity<String> response = listener.forwardToPAR(new JSONObject(requestToForward).toJSONString());

        if (response == null) {
            System.out.println("Unable to process redemption.");
            return "error";
        }

        if (response.getBody().equals("Insufficient points.")) {
            return "errorItemRedeem";
        } else if (response.getBody().equals("Transaction succeeded.")) {
            return "successRedeemItem";
        }

        return "error";
    }
}
