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

import java.util.List;

@Tag(name = "/images", description = "이미지 업로드 API")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {
    private final ImageUploadService imageUploadService;

    @Operation(summary = "이미지 업로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드한 이미지 URL 리턴"),
            @ApiResponse(responseCode = "500", description = "이미지 업로드 실패")
    })
    @PostMapping(path = "/list/{dirName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadAll(
            @PathVariable ImageDirectory dirName,
            @RequestParam List<MultipartFile> files
    ) {
        return ResponseEntity.ok(imageUploadService.uploadAll(dirName, files));
    }

    @Operation(summary = "이미지 이름을 받아 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공"),
    })
    @DeleteMapping("/list/{dirName}")
    public ResponseEntity<Void> delete(
            @PathVariable ImageDirectory dirName,
            @RequestBody DeleteImageFilesRequest request
    ) {
        imageUploadService.deleteAll(dirName, request.getImageNames());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "이미지 업로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드한 이미지 URL 리턴"),
            @ApiResponse(responseCode = "500", description = "이미지 업로드 실패")
    })
    @PostMapping(path = "/one/{dirName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadOne(
            @PathVariable ImageDirectory dirName,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(imageUploadService.upload(dirName, file));
    }

    @Operation(summary = "이미지 이름을 받아 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공"),
    })
    @DeleteMapping("/one/{dirName}/{imageName}")
    public ResponseEntity<Void> deleteOne(
            @PathVariable ImageDirectory dirName,
            @PathVariable String imageName
    ) {
        imageUploadService.delete(dirName, imageName);
        return ResponseEntity.ok().build();
    }
}
