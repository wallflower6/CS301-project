var form = document.getElementById("book-venue-form");
// var baseurl = "http://localhost:8081"
var baseurl = "http://ec2-18-138-153-99.ap-southeast-1.compute.amazonaws.com:8080";

function redirectBookVenue(){
    var venue_id = document.getElementById("venue_id").value;
    var date = document.getElementById("date").value;
    var time = document.getElementById("time").value;
    var party_size = document.getElementById("party_size").value;
    var first_name = document.getElementById("first_name").value;
    var last_name = document.getElementById("last_name").value;
    var phone = document.getElementById("phone").value;

    var url =  baseurl + "/venues/" + venue_id + "/book/" + date + "/time/" + time + "/party_size/" + party_size+ "/first_name/" + first_name+ "/last_name/" + last_name+ "/phone/" + phone

    console.log(url)
    window.open(url, "_self")
}


form.addEventListener('submit', (e) => {
    e.preventDefault();

    var venue_id = document.getElementById("venue_id").value;
    var date = document.getElementById("date").value;
    var time = document.getElementById("time").value;
    var party_size = document.getElementById("party_size").value;
    var first_name = document.getElementById("first_name").value;
    var last_name = document.getElementById("last_name").value;
    var phone = document.getElementById("phone").value;

    var bookVenueDetails = {
        "venue_id": venue_id,
        "date": date,
        "time": time,
        "party_size": party_size,
        "first_name": first_name,
        "last_name": last_name,
        "phone": phone
    }
        
});