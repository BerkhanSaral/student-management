
package com.tpe.contactmessage.controller;

import com.tpe.contactmessage.dto.ContactMessageRequest;
import com.tpe.contactmessage.dto.ContactMessageResponse;
import com.tpe.contactmessage.entity.ContactMessage;
import com.tpe.contactmessage.service.ContactMessageService;
import com.tpe.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping("/save") // http://localhost:8080/contactMessages/save  + POST
    public ResponseMessage<ContactMessageResponse> saveContact(@RequestBody @Valid ContactMessageRequest contactMessageRequest){
        return contactMessageService.save(contactMessageRequest);
    }

    @GetMapping("/getAll") // http://localhost:8080/contactMessages/getAll + GET
    public Page<ContactMessageResponse> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return contactMessageService.getAll(page,size,sort,type);
    }

    @GetMapping("/searchByEmail") // http://localhost:8080/contactMessages/searchByEmail?email=aaa@bbb.com
    public Page<ContactMessageResponse> searchByEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return contactMessageService.searchByEmail(email,page,size,sort,type);
    }

    // Not: *************************************** searchBySubject *******************************
    @GetMapping("/searchBySubject")// http://localhost:8080/contactMessages/searchBySubject?subject=deneme
    public Page<ContactMessageResponse> searchBySubject(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "sort",defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){
        return contactMessageService.searchBySubject(subject,page,size,sort,type);
    }
    /*  @GetMapping("/searchBySubject")
    public Page<ContactMessageResponse> searchBySubject(@RequestParam(value = "subject") String subject,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                        @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
                                                        @RequestParam(value = "type", defaultValue = "desc") String type) {
        return contactMessageService.searchBySubject(subject, page, size, sort, type);
    }*/

    // Not: ODEVVV    searchByDateBetween ***************************************
    @GetMapping("/searchBetweenDates") // http://localhost:8080/contactMessages/searchBetweenDates?beginDate=2023-09-13&endDate=2023-09-15
    public ResponseEntity<List<ContactMessage>> searchByDateBetween(
            @RequestParam(value = "beginDate") String beginDateString,
            @RequestParam(value = "endDate") String endDateString){
        List<ContactMessage>contactMessages = contactMessageService.searchByDateBetween(beginDateString, endDateString);
        return ResponseEntity.ok(contactMessages);
    }
    /*  @GetMapping("/searchByDateBetween")
//http://localhost:8080/contactMessages/searchByDateBetween?startDateTime=2024-09-06 14:16&endDateTime=2024-09-06 14:17
    public Page<ContactMessageResponse> searchByDateBetween(@RequestParam("startDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate,
                                                            @RequestParam("endDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate,
                                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
                                                            @RequestParam(value = "type", defaultValue = "desc") String type) {
        return contactMessageService.searchByDateBetween(startDate, endDate, page, size, sort, type);
    }*/

    // Not: *********************************** getByIdWithParam ***************************************
    @GetMapping("/getByIdParam") //http://localhost:8080/contactMessages/getByIdParam?contactMessageId=1
    public ResponseEntity<ContactMessage> getById(@RequestParam(value = "contactMessageId") Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }

    /* @GetMapping("/getByIdWithParam")
    public ResponseMessage<ContactMessageResponse> getByIdWithParam(@RequestParam("id") Long id) {
        return contactMessageService.getByIdWithParam(id);
    }
*/

    // Not: ************************************ getByIdWithPath ***************************************
    @GetMapping("/getById/{contactMessageId}")//http://localhost:8080/contactMessages/getById/1
    public ResponseEntity<ContactMessage> getByIdPath(@PathVariable Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }

    // Not: *********************************** deleteByIdParam ***************************************
    @DeleteMapping("/deleteByIdParam")  //http://localhost:8080/contactMessages/deleteByIdParam?contactMessageId=1
    public ResponseEntity<String> deleteById(@RequestParam(value = "contactMessageId") Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

  /*  @DeleteMapping("/deleteByIdParam")//    //http://localhost:8080/contactMessages/deleteByIdParam?id=1
    public ResponseMessage<String> deleteByIdParam(@RequestParam("id") Long id) {
        return contactMessageService.deleteByIdParam(id);
    }
*/



    // Not: ***************************************** deleteById ***************************************
    @DeleteMapping("/deleteById/{contactMessageId}")//http://localhost:8080/contactMessages/deleteById/2
    public ResponseEntity<String> deleteByIdPath(@PathVariable Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

      /*    @DeleteMapping("/deleteById/{id}")//    //http://localhost:8080/contactMessages/deleteById/1
    public ResponseMessage<String> deleteById(@PathVariable Long id) {
        return contactMessageService.deleteById(id);
    }*/
}