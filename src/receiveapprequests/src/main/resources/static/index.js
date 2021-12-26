var form = document.getElementById("login-form");
// var baseurl = "http://localhost:8081";
var baseurl = "http://ec2-18-138-153-99.ap-southeast-1.compute.amazonaws.com:8080";

function redirectLogin(){
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var email = document.getElementById("email").value;
    console.log(window.location.href)
    var url =  baseurl + "/login/" + username + "/password/" + password 
 
    window.open(url, "_self")
}

//LISTENS TO USERNAME AND PASSWORD SUBMISSION
form.addEventListener('submit', (e) => {
    e.preventDefault();

    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var password = document.getElementById("email").value;

    var loginDetails = {
        "username": username,
        "password": password,
        "email": email
    }

    console.log(loginDetails);

    login(loginDetails).then(response => {
        console.log(response);
    })
        
});