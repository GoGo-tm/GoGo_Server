package com.tm.gogo.web.image;

import com.tm.gogo.helper.ImageUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "/image", description = "이미지 업로드 API")
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageUploadController {
    private final ImageUploadService imageUploadService;

    @Operation(summary = "이미지 업로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 URL 리턴"),
    })
    @PostMapping(path = "/{dir}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@PathVariable String dir, @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageUploadService.upload(dir, file));
    }
}
