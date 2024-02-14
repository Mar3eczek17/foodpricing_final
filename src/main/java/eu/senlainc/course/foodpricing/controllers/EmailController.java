package eu.senlainc.course.foodpricing.controllers;

import eu.senlainc.course.foodpricing.dto.ApiResponse;
import eu.senlainc.course.foodpricing.dto.EmailRequest;
import eu.senlainc.course.foodpricing.service.EmailSenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private EmailSenderService senderService;

    private static final String EMAIL_SENT_MESSAGE = "Lost password email sent successfully!";

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> sendLostPasswordEmail(@RequestBody @Valid EmailRequest emailRequest) {
        String emailAddress = emailRequest.getEmail();
        senderService.sendLostPasswordEmail(emailAddress);
        ApiResponse response = new ApiResponse(EMAIL_SENT_MESSAGE);
        return ResponseEntity.ok(response);
    }
}