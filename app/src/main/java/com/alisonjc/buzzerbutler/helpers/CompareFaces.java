package com.alisonjc.buzzerbutler.helpers;


import android.media.Image;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class CompareFaces {
//
//    public static boolean compareFaces() {
//        Float similarityThreshold = 70F;
//        String sourceImage = "source.jpg";
//        String targetImage = "target.jpg";
//        ByteBuffer sourceImageBytes = null;
//        ByteBuffer targetImageBytes = null;
//
//        AWSCredentials credentials;
//        try {
//            credentials = new ProfileCredentialsProvider("AdminUser").getCredentials();
//        } catch (Exception e) {
//            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
//                    + "Please make sure that your credentials file is at the correct "
//                    + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
//        }
//
//        EndpointConfiguration endpoint = new EndpointConfiguration("endpoint",
//                "us-east-1");
//
//        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
//                .standard()
//                .withEndpointConfiguration(endpoint)
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .build();
//
//        //Load source and target images and create input parameters
//        try (InputStream inputStream = new FileInputStream(new File(sourceImage))) {
//            sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
//        } catch (Exception e) {
//            System.out.println("Failed to load source image " + sourceImage);
//            System.exit(1);
//        }
//        try (InputStream inputStream = new FileInputStream(new File(targetImage))) {
//            targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
//        } catch (Exception e) {
//            System.out.println("Failed to load target images: " + targetImage);
//            System.exit(1);
//        }
//
//        Image source = new Image()
//                .withBytes(sourceImageBytes);
//        Image target = new Image()
//                .withBytes(targetImageBytes);
//
//        CompareFacesRequest request = new CompareFacesRequest()
//                .withSourceImage(source)
//                .withTargetImage(target)
//                .withSimilarityThreshold(similarityThreshold);
//
//        // Call operation
//        CompareFacesResult compareFacesResult = rekognitionClient.compareFaces(request);
//
//        // Display results
//        List<CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
//        for (CompareFacesMatch match : faceDetails) {
//            ComparedFace face = match.getFace();
//            BoundingBox position = face.getBoundingBox();
//            Float confidence = face.getConfidence();
//            System.out.println("Face at " + position.getLeft().toString()
//                    + " " + position.getTop()
//                    + " matches with " + confidence.toString()
//                    + "% confidence.");
//
//            if (confidence > .9) {
//                return true;
//            }
//        }
//        return false;
    }

}

