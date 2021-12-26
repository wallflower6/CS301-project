var form = document.getElementById("redeem-item-form");
// var baseurl = "http://localhost:8081"
var baseurl = "http://ec2-18-138-153-99.ap-southeast-1.compute.amazonaws.com:8080";

function redirectRedeemItem(){
    var itemId = document.getElementById("item_id").value;
    var url =  baseurl + "/items/" + itemId + "/redeem"

    console.log(url)
    window.open(url, "_self")
}

form.addEventListener('submit', (e) => {
    e.preventDefault();
    console.log("Form submitted");

    var itemId = document.getElementById("item_id").value;

    var redeemItemDetails = {
        "item_id": itemId,
    }

});