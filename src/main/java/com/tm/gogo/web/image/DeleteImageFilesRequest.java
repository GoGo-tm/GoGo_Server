package com.tm.gogo.web.image;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DeleteImageFilesRequest {
    private List<String> imageNames;
}
