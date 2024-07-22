package com.econovation.teamponyo.common.utils;

import java.util.Arrays;
import java.util.List;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
            "jpeg",
            "png",
            "gif",
            "bmp",
            "webp",
            "jpg"
    );

    public static void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new  IllegalArgumentException("이미지 파일이 비어있음");
        }

        if (!IMAGE_EXTENSIONS.contains(StringUtils.getFilenameExtension(file.getOriginalFilename())))
            throw new IllegalArgumentException("지원하는 이미지 파일이 아님");
    }
}