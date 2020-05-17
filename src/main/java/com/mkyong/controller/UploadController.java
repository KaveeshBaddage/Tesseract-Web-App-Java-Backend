package com.mkyong.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/tmp/";

    @PostMapping("/getEnglishText") // //new annotation since 4.3
    @ResponseBody
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        String extractedText = "";

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            // Get the file and save it temporarily
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            System.out.println("File name is" + file.getOriginalFilename());
            Files.write(path, bytes);

            //Run Tessseract Command to extract the text
            ProcessBuilder builder = new ProcessBuilder(
                    "/bin/sh", "-c", " tesseract " + UPLOADED_FOLDER + file.getOriginalFilename() + " stdout -l eng");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                extractedText = extractedText + System.lineSeparator() + line;
                System.out.println(line);

            }
            //Delete temporarily created file
            File ToDelete = new File(String.valueOf(path));
            ToDelete.delete();
/*            redirectAttributes.addFlashAttribute("message",
                    "'" + extractedText + "'");*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractedText;

    }

    @PostMapping("/getSinhalaText") // //new annotation since 4.3
    @ResponseBody
    public String singleFileUploadTwo(@RequestParam("file") MultipartFile file) {
        String extractedText = "";

        if (file.isEmpty()) {
            //redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "Please select a file and upload";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            System.out.println("File name is" + file.getOriginalFilename());
            Files.write(path, bytes);

            //Run Tessseract Command to extract the text
            ProcessBuilder builder = new ProcessBuilder(
                    "/bin/sh", "-c", " tesseract " + UPLOADED_FOLDER + file.getOriginalFilename() + " stdout -l sin");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                extractedText = extractedText + System.lineSeparator() + line;
                System.out.println(line);
            }

            //redirectAttributes.addFlashAttribute("message","'" + extractedText + "'");
            //Delete temporarily created file
            File ToDelete = new File(String.valueOf(path));
            ToDelete.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractedText;
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

}