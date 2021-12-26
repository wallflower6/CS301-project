package com.como.service.receiveapprequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private static final Map<String, Item> items = new HashMap<>();
    private static final ArrayList<Item> listOfItem = new ArrayList<Item>();

    private static List<JSONObject> itemsLocal;
    
    public ItemService(){
        Item melatiJiggerGiftSet = new Item();
        melatiJiggerGiftSet.setItemName("Melati Jigger Gift Set");
        melatiJiggerGiftSet.setItemId("1");
        melatiJiggerGiftSet.setItemPointCost(4000);
        items.put(melatiJiggerGiftSet.getItemId(), melatiJiggerGiftSet);
        listOfItem.add(melatiJiggerGiftSet);

        Item kaviariKristalCaviar = new Item();
        kaviariKristalCaviar.setItemName("Kaviari Kristal Caviar");
        kaviariKristalCaviar.setItemId("2");
        kaviariKristalCaviar.setItemPointCost(5000);
        items.put(kaviariKristalCaviar.getItemId(), kaviariKristalCaviar);
        listOfItem.add(kaviariKristalCaviar);

        Item paulSmithShoe = new Item();
        paulSmithShoe.setItemName("Paul Smith Shoe");
        paulSmithShoe.setItemId("3");
        paulSmithShoe.setItemPointCost(6000);
        items.put(paulSmithShoe.getItemId(), paulSmithShoe);
        listOfItem.add(paulSmithShoe);
    }

    public static void setItemsLocal(List<JSONObject> itemsLocal) {
        ItemService.itemsLocal = itemsLocal;
    }

    public static List<JSONObject> getItemsLocal() {
        return itemsLocal;
    }

    public List<Item> getAllItems() {
		return new ArrayList(items.values());
	}

	public Item getItem(String itemId) {
		return items.get(itemId);
	}

	public void addItem(Item item) {
	 	items.put(item.getItemId(), item);
	}

    public List<Item> getListOfItems(){
        ArrayList<Item> listOfItem2 = new ArrayList<Item>();
        for (int i = 0; i < listOfItem.size()/2; i++) {
            listOfItem2.add(listOfItem.get(i));
        }
        return listOfItem2;
    }

}
