//package com.econovation.teamponyo.common.utils;
//
//import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.util.Base64;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import software.amazon.awssdk.utils.IoUtils;
//
//@JsonNaming(SnakeCaseStrategy.class)
//public record ImageData(
//        String base64Image,
//        String mimeType
//){
//    public static ImageData of(File file) {
//        try (InputStream inputStream = new FileInputStream(file)) {
//            byte[] bytes = IoUtils.toByteArray(inputStream);
//            String mimeType = Files.probeContentType(file.toPath());
//            String base64 = Base64.getEncoder().encodeToString(bytes);
//            return new ImageData(base64, mimeType);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to encode file to Base64", e);
//        }
//    }
//    public static List<ImageData> of(List<File> files) {
//        List<CompletableFuture<ImageData>> futures = files.stream()
//                .map(file -> CompletableFuture.supplyAsync(() -> of(file)))
//                .toList();
//        return futures.stream()
//                .map(CompletableFuture::join)
//                .toList();
//    }
//}
