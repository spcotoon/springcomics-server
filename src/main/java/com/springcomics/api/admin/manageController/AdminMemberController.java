package com.springcomics.api.admin.manageController;

import com.springcomics.api.admin.adminDto.MemberDto;
import com.springcomics.api.domain.member.User;
import com.springcomics.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminMemberController {

    private final UserRepository userRepository;

    @GetMapping("/admin/manage/member")
    public ResponseEntity<PagedModel<MemberDto>> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);

        Page<User> userPages = userRepository.findAll(pageable);

        Page<MemberDto> memberPage = userPages.map(user -> MemberDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickname())
                .createdDate(user.getCreatedDate())
                .build()
        );

        return ResponseEntity.ok().body(new PagedModel<>(memberPage));

    }

    @DeleteMapping("/admin/manage/member/{id}/delete")
    public ResponseEntity<String> deleteMember(@PathVariable("id") String requestId) {
        long userId = Long.parseLong(requestId);
        userRepository.deleteById(userId);
        return ResponseEntity.ok().body("success");
    }


}
