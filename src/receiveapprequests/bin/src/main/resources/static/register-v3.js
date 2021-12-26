var form = document.getElementById("register-form");
var baseurl = "http://ec2-18-138-153-99.ap-southeast-1.compute.amazonaws.com:8080"
// async function login(loginDetails) {
//     let response = await fetch("http://localhost:8081/login", {
//         method: 'POST',
//         headers: {
//             'Accept': 'application/json',
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(loginDetails)
//     })

//     return response;
// }

function requestAuthorization(registerDetails) {
    // always try using const rather than let
    fetch("http://ec2-18-138-153-99.ap-southeast-1.compute.amazonaws.com:8080/process-registration", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(registerDetails),
    });
}



function redirectRegister(){
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var email = document.getElementById("email").value;
    var title = document.getElementById("title").value;
    var firstName = document.getElementById("firstName").value;
    var lastName = document.getElementById("lastName").value;
    var mobileCountryCode = document.getElementById("mobileCountryCode").value;
    var mobile = document.getElementById("mobile").value;

    var url =  baseurl + "/registration/user/" + username + "/password/" + password + "/email/" + email + "/title/" + title + "/firstName/" + firstName + "/lastName/" + lastName + "/mobileCountryCode/" + mobileCountryCode + "/mobile/" + mobile;
 
    window.open(url, "_self")
}

//LISTENS TO USERNAME AND PASSWORD SUBMISSION
form.addEventListener('submit', (e) => {
    e.preventDefault();

    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var email = document.getElementById("email").value;
    var title = document.getElementById("title").value;
    var firstName = document.getElementById("firstName").value;
    var lastName = document.getElementById("lastName").value;
    var mobileCountryCode = document.getElementById("mobileCountryCode").value;
    var mobile = document.getElementById("mobile").value;

    var registerDetails = {
        "username": username,
        "password": password,
        "email": email,
        "title": title,
        "firstName": firstName,
        "lastName": lastName,
        "mobileCountryCode": mobileCountryCode,
        "mobile": mobile
    }

    requestAuthorization(registerDetails);


    redirectRegister();

    // register(registerDetails).then(response => {
    //     console.log(response);
    // })
        
});

