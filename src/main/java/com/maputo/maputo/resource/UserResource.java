package com.maputo.maputo.resource;

import com.maputo.maputo.domain.HttpResponse;
import com.maputo.maputo.domain.User;
import com.maputo.maputo.domain.UserPrincipal;
import com.maputo.maputo.exception.domain.*;
import com.maputo.maputo.service.UserService;
import com.maputo.maputo.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.maputo.maputo.constant.FileConstant.*;
import static com.maputo.maputo.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = {"/user","/"})
public class UserResource extends ExceptionHandling {
    public static final String PASSWORD_SENT_TO = "An email with new password sent to: ";
    public static final String USER_WAS_DELETED_SUCCESSFULLY = "User was deleted successfully";
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider tokenProvider;

    @Autowired
    public UserResource(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(newUser, OK);

    }
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
        //If authentication will not proceed I throw an exception
        authenticate(user.getUsername(), user.getPassword());
        //If everything is ok I get that user by username
        User loginUser = userService.findUserByUsername(user.getUsername());
        //Then pass userPrincipal
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        //And pass the jwtHeader
        HttpHeaders jwtHeader=getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);

    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive, //"true"
                                           @RequestParam("isNonLocked") String isNonLocked, //"true"
                                           @RequestParam(value = "profileImg", required = false) MultipartFile profileImg) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
    User user = userService.addNewUser(firstName,lastName,username,email,role,Boolean.parseBoolean(isNonLocked),Boolean.parseBoolean(isActive),profileImg);
    return new ResponseEntity<>(user, OK);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam("currentUsername") String currentUsername,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive, //"true"
                                           @RequestParam("isNonLocked") String isNonLocked, //"true"
                                           @RequestParam(value = "profileImg", required = false) MultipartFile profileImg) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        User user = userService.updateUser(currentUsername,firstName,lastName,username,email,role,Boolean.parseBoolean(isNonLocked),Boolean.parseBoolean(isActive),profileImg);
        return new ResponseEntity<>(user, OK);
    }
    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username,@RequestParam(value = "profileImg") MultipartFile profileImg) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        User user = userService.updateProfileImage(username, profileImg);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping(path="/image/{username}/{filename}",produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username,@PathVariable("filename") String filename) throws IOException {
        //go to the specific folder read its bytes and return it to the browser
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + filename));
        //"user.home" + "supportportal/user/rick/rick.jpg"
    }

    @GetMapping(path="/image/profile/{username}",produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileTempImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();//capture stream in bytes
        try (InputStream inputStream = url.openStream()) {
            //from the opened url stream
            int bytesRead;
            //read that many bytes at a time
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                //give me that many time bytes until we are done
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username){
        User user=userService.findUserByUsername(username);
        return new ResponseEntity<>(user,OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users=userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws EmailNotFoundException, MessagingException {
        userService.resetPassword(email);
        return response(OK, PASSWORD_SENT_TO + email);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")//alowing to delete users to only those who have this authority like super admin
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") long id){
        userService.deleteUser(id);
        return response(NO_CONTENT, USER_WAS_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        //returning our custom http response with this method
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(),message.toUpperCase()),httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, tokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }
}
