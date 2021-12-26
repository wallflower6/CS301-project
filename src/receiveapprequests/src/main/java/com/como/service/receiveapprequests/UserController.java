package com.como.service.receiveapprequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService = new UserService();

    @Autowired
    private RARListener listener;

    @Autowired
    private JSONService jsonService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private Dummify dummify;

    //this was for testing
    @RequestMapping("/login/{username}/password/{password}")
    public String loginUser(@PathVariable(name ="username") String username, @PathVariable(name="password") String password){
        if(userService.checkUsername(username)&&userService.checkPassword(username, password)){
            return "choose";
        }
        //add password check

        return "errorLogin";
    }

    @RequestMapping("/errorLogin")
    public String errorLogin(){
        return "errorLogin";
    }
    
    @RequestMapping("/choose")
    public String chooseBookingOrRedeeming(){
        return "choose";
    }

    @GetMapping("/users/{username}")
    public String getUsername(@PathVariable(name ="username") String username){
        System.out.println(username);
        return username;
    }

    @PostMapping("/process-registration")
    public String addUser(@RequestBody String regDetails, Model model) {

        JSONObject newUser = jsonService.convertStringToJSONObj(regDetails);
        
        // Use email to check profile doesn't already exist
        /*
            { 
                "EmailAddress": "abc@abc.com" 
            }
            https://c21sguat.memgate.com/api/profile/search-simple POST
        */
        Map<String, Object> dataToForward = new HashMap<>();
        dataToForward.put("EmailAddress", newUser.get("email"));
        Map<String, Object> requestToForward = new HashMap<>();
        requestToForward.put("identifier", "checkEmail");
        requestToForward.put("action", "POST");
        requestToForward.put("uri", "/api/profile/search-simple");
        requestToForward.put("data", dataToForward);

        List<JSONObject> user = memberService.checkMemberExist_Memberson(newUser.get("email").toString());

        if (user.isEmpty()) {
            System.out.println("User does not exist");
            ResponseEntity<String> newUserCreated = memberService.createNewUser(newUser);
            if (newUserCreated.getStatusCode() == HttpStatus.OK) {
                System.out.println(newUserCreated.getBody());
                return "successRegister";
            }

        } else {
            System.out.println("User exists!");

            // Return its profile
            // https://c21sguat.memgate.com/api/profile/<customerNumber> 
            String customerNumber = memberService.getCustomerNumber_Memberson(user);

            String msg = "This is your current user id: "+ customerNumber;
            model.addAttribute("UserExist", msg);

            // listener.forwardToPAR(request)
            return "errorAlreadyRegister";
        }

        return "error";
    }

    @RequestMapping("registration/user/{username}/password/{password}/email/{email}/title/{title}/firstName/{firstName}/lastName/{lastName}/mobileCountryCode/{mobileCountryCode}/mobile/{mobile}")
    public String addUser(@PathVariable(name = "username") String username,
            @PathVariable(name = "password") String password,
            @PathVariable(name = "email") String email,
            @PathVariable(name = "title") String title,
            @PathVariable(name = "firstName") String firstName,
            @PathVariable(name = "lastName") String lastName,
            @PathVariable(name = "mobileCountryCode") String mobileCountryCode,
            @PathVariable(name = "mobile") String mobile) {
        JSONObject users = new JSONObject();
        users.put("username", username);
        users.put("email", email);
        users.put("title", title);
        users.put("firstName", firstName);
        users.put("lastName", lastName);
        users.put("mobileCountryCode", mobileCountryCode);
        users.put("mobile", mobile);

        String uri = String.format("registration/user/%s/password/%s/email/%s/title/%s/firstName/%s/lastName/%s/mobileCountryCode/%s/mobile/%s", 
        username, password, email, title, firstName, lastName, mobileCountryCode, mobile );

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "POST");
        jsonObject.put("identifier", "registerUser");
        jsonObject.put("users", users);
        jsonObject.put("uri", uri);
        System.out.println("registerUser: " + jsonObject);

        String jsonString = jsonObject.toString();
        // listener.forwardToPAR(jsonString);

        
        // Add user into service
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMobileCountryCode(mobileCountryCode);
        user.setMobile(mobile);
        user.setRedemptionPoints(0);
        userService.addUser(user);
        System.out.println("Added with user service:" + user);

        return "successRegister";
    }
}
